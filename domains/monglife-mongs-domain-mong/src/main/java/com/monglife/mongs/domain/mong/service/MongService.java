package com.monglife.mongs.domain.mong.service;

import com.monglife.mongs.domain.mong.dto.etc.GetFeedItemDto;
import com.monglife.mongs.domain.mong.dto.etc.GetMongDto;
import com.monglife.mongs.domain.mong.dto.etc.UpdateMongStatusDto;
import com.monglife.mongs.domain.mong.entity.FoodTypeEntity;
import com.monglife.mongs.domain.mong.entity.MongEntity;
import com.monglife.mongs.domain.mong.entity.MongFeedHistoryEntity;
import com.monglife.mongs.domain.mong.entity.MongTypeEntity;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.exception.*;
import com.monglife.mongs.domain.mong.repository.FoodTypeRepository;
import com.monglife.mongs.domain.mong.repository.MongFeedHistoryRepository;
import com.monglife.mongs.domain.mong.repository.MongRepository;
import com.monglife.mongs.domain.mong.repository.MongTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongService {

    private static final Random random = new Random();

    @Value("${application.service.mong.egg-mong-type-group-code}")
    private String  EGG_MONG_TYPE_GROUP_CODE;

    private static final Integer DEFAULT_EVOLUTION_SCORE = 200;
    private static final Integer DEFAULT_PAY_POINT = 50;
    private static final Double  DEFAULT_STROKE_EXP = 15D;
    private static final Double  DEFAULT_POOP_CLEAN_EXP = 15D;

    private final MongRepository mongRepository;
    private final MongFeedHistoryRepository mongFeedHistoryRepository;
    private final MongTypeRepository mongTypeRepository;
    private final FoodTypeRepository foodTypeRepository;


    /**
     * 몽 목록 조회
     * @param accountId 계정 ID
     * @return 몽 목록
     */
    @Transactional(readOnly = true)
    public List<GetMongDto> getMongs(Long accountId) {

        List<MongEntity> mongEntities = mongRepository.findByAccountIdAndStateIsActiveIsTrue(accountId);

        return mongEntities.stream()
                .map(GetMongDto::of)
                .toList();
    }

    /**
     * 몽 단건 조회
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @return 몽 정보
     */
    @Transactional(readOnly = true)
    public GetMongDto getMong(Long accountId, Long mongId) {

        MongEntity mongEntity = mongRepository.findByAccountIdAndMongIdAndStateIsActiveIsTrue(accountId, mongId)
                .orElseThrow(() -> new NotExistsMongException(accountId, mongId));

        return GetMongDto.of(mongEntity);
    }

    /**
     * 먹이 목록 조회
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @param foodTypeGroupCode 음식 or 간식 그룹 코드
     * @return 구매 가능 여부를 포함한 음식/간식 목록
     */
    @Transactional(readOnly = true)
    public List<GetFeedItemDto> getFeedItems(Long accountId, Long mongId, String foodTypeGroupCode) {

        mongRepository.findByAccountIdAndMongIdAndStateIsActiveIsTrue(accountId, mongId)
                .orElseThrow(() -> new NotExistsMongException(accountId, mongId));

        return foodTypeRepository.findByFoodCodeGroupCode(foodTypeGroupCode).stream()
                .map(foodTypeEntity -> {

                    String foodTypeCode = foodTypeEntity.getFoodCode().getComnCode();

                    Boolean isCanBuy = mongFeedHistoryRepository.findByMongIdAndFoodTypeCode(mongId, foodTypeCode).isEmpty();

                    return GetFeedItemDto.builder()
                            .isCanBuy(isCanBuy)
                            .foodTypeCode(foodTypeCode)
                            .price(foodTypeEntity.getPrice())
                            .foodTypeName(foodTypeEntity.getFoodCode().getComnName())
                            .foodTypeGroupCode(foodTypeEntity.getFoodCode().getGroupCode())
                            .addWeightValue(foodTypeEntity.getAddWeightValue())
                            .addStrengthValue(foodTypeEntity.getAddStrengthValue())
                            .addSatietyValue(foodTypeEntity.getAddSatietyValue())
                            .addHealthyValue(foodTypeEntity.getAddHealthyValue())
                            .addFatigueValue(foodTypeEntity.getAddFatigueValue())
                            .build();
                })
                .toList();
    }

    /**
     * 몽 생성
     * @param accountId 계정 ID
     * @param name 몽 이름
     * @param sleepAt 몽 정기 수면 시작 시간
     * @param wakeupAt 몽 정기 수면 종료 시간
     */
    @Transactional
    public Long createMong(Long accountId, String name, LocalTime sleepAt, LocalTime wakeupAt) {

        List<MongTypeEntity> mongTypeEntities = mongTypeRepository.findByMongCodeGroupCode(EGG_MONG_TYPE_GROUP_CODE);

        if (mongTypeEntities.isEmpty()) {
            throw new NotExistsMongTypeCodeException();
        }

        int mongTypeCodeIndex = random.nextInt(0, mongTypeEntities.size());
        MongTypeEntity mongTypeEntity = mongTypeEntities.get(mongTypeCodeIndex);

        MongEntity mongEntity = MongEntity.builder()
                .accountId(accountId)
                .mongName(name)
                .type(mongTypeEntity)
                .sleepAt(sleepAt)
                .wakeupAt(wakeupAt)
                .payPoint(DEFAULT_PAY_POINT)
                .build();

        mongRepository.save(mongEntity);

        return mongEntity.getMongId();
    }

    /**
     * 몽 삭제
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     */
    @Transactional
    public void deleteMong(Long accountId, Long mongId) {

        MongEntity mongEntity = mongRepository.findByAccountIdAndMongIdAndStateIsActiveIsTrue(accountId, mongId)
                .orElseThrow(() -> new NotExistsMongException(accountId, mongId));

        mongEntity.delete();
    }

    /**
     * 몽 먹이 주기
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @param foodTypeCode 음식/간식 타입 코드
     */
    @Transactional
    public void feedMong(Long accountId, Long mongId, String foodTypeCode) {

        mongFeedHistoryRepository.findByMongIdAndFoodTypeCode(mongId, foodTypeCode)
                .ifPresent(mongFeedHistory -> {
                    Long expirationSeconds = mongFeedHistory.getExpiration() - Duration.between(mongFeedHistory.getBuyAt(), LocalDateTime.now()).getSeconds();
                    throw new InvalidFeedException(mongId, foodTypeCode, expirationSeconds);
                });

        FoodTypeEntity foodTypeEntity = foodTypeRepository.findByFoodCodeComnCode(foodTypeCode)
                .orElseThrow(() -> new NotExistsFoodTypeCodeException(foodTypeCode));

        MongEntity mongEntity = mongRepository.findByAccountIdAndMongIdAndStateIsActiveIsTrue(accountId, mongId)
                .orElseThrow(() -> new NotExistsMongException(accountId, mongId));

        if (mongEntity.isEgg()) {
            throw new InvalidMongTypeLevelException(mongId, mongEntity.getType().getMongCode().getComnCode(), mongEntity.getType().getLevel());
        }

        if (mongEntity.getPayPoint() < foodTypeEntity.getPrice()) {
            throw new NotEnoughPayPointException(mongId, foodTypeCode, foodTypeEntity.getPrice(), mongEntity.getPayPoint());
        }

        UpdateMongStatusDto updateMongStatusDto = UpdateMongStatusDto.builder()
                .changeWeightValue(foodTypeEntity.getAddWeightValue())
                .changeStrengthValue(foodTypeEntity.getAddStrengthValue())
                .changeSatietyValue(foodTypeEntity.getAddSatietyValue())
                .changeHealthyValue(foodTypeEntity.getAddHealthyValue())
                .changeFatigueValue(foodTypeEntity.getAddFatigueValue())
                .build();

        mongEntity.feed(foodTypeEntity.getPrice(), updateMongStatusDto);

        MongFeedHistoryEntity mongFeedHistoryEntity = MongFeedHistoryEntity.builder()
                .mongFeedHistoryId(UUID.randomUUID().toString())
                .mongId(mongId)
                .foodTypeCode(foodTypeCode)
                .buyAt(LocalDateTime.now())
                .expiration(foodTypeEntity.getDelaySeconds().longValue())
                .build();

        mongFeedHistoryRepository.save(mongFeedHistoryEntity);
    }

    /**
     * 몽 쓰다 듬기
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     */
    @Transactional
    public void strokeMong(Long accountId, Long mongId) {

        MongEntity mongEntity = mongRepository.findByAccountIdAndMongIdAndStateIsActiveIsTrue(accountId, mongId)
                .orElseThrow(() -> new NotExistsMongException(accountId, mongId));

        if (mongEntity.isEgg()) {
            throw new InvalidMongTypeLevelException(mongId, mongEntity.getType().getMongCode().getComnCode(), mongEntity.getType().getLevel());
        }

        mongEntity.stroke(DEFAULT_STROKE_EXP);
    }

    /**
     * 몽 수면
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     */
    @Transactional
    public void sleepMong(Long accountId, Long mongId) {

        MongEntity mongEntity = mongRepository.findByAccountIdAndMongIdAndStateIsActiveIsTrue(accountId, mongId)
                .orElseThrow(() -> new NotExistsMongException(accountId, mongId));

        if (mongEntity.isEgg()) {
            throw new InvalidMongTypeLevelException(mongId, mongEntity.getType().getMongCode().getComnCode(), mongEntity.getType().getLevel());
        }

        mongEntity.sleep();
    }

    /**
     * 몽 기상
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     */
    @Transactional
    public void wakeupMong(Long accountId, Long mongId) {

        MongEntity mongEntity = mongRepository.findByAccountIdAndMongIdAndStateIsActiveIsTrue(accountId, mongId)
                .orElseThrow(() -> new NotExistsMongException(accountId, mongId));

        if (mongEntity.isEgg()) {
            throw new InvalidMongTypeLevelException(mongId, mongEntity.getType().getMongCode().getComnCode(), mongEntity.getType().getLevel());
        }

        mongEntity.wakeup();
    }

    /**
     * 배변 처리
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     */
    @Transactional
    public void poopCleanMong(Long accountId, Long mongId) {

        MongEntity mongEntity = mongRepository.findByAccountIdAndMongIdAndStateIsActiveIsTrue(accountId, mongId)
                .orElseThrow(() -> new NotExistsMongException(accountId, mongId));

        if (mongEntity.isEgg()) {
            throw new InvalidMongTypeLevelException(mongId, mongEntity.getType().getMongCode().getComnCode(), mongEntity.getType().getLevel());
        }

        mongEntity.poopClean(DEFAULT_POOP_CLEAN_EXP);
    }

    /**
     * 몽 진화
     * 1. 진화 준비 상태인 경우에 가능
     * 2. 더이상 진화가 불가능 한 경우 졸업 준비로 상태 변경
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     */
    @Transactional
    public void evolutionMong(Long accountId, Long mongId) {

        MongEntity mongEntity = mongRepository.findByAccountIdAndMongIdAndStateIsActiveIsTrue(accountId, mongId)
                .orElseThrow(() -> new NotExistsMongException(accountId, mongId));

        if (!MongStateCode.EVOLUTION_READY.equals(mongEntity.getState().getCode())) {
            throw new InvalidEvolutionException(accountId, mongId);
        }

        String nextTypeGroupCode = mongEntity.getType().getNextTypeGroupCode();

        List<MongTypeEntity> mongTypeEntities = mongTypeRepository.findByMongCodeGroupCode(nextTypeGroupCode).stream()
                .sorted((o1, o2) -> o2.getEvolutionScore().compareTo(o1.getEvolutionScore()))
                .toList();

        if (mongTypeEntities.isEmpty()) {
            // 더이상 진화할 수 없는 경우 졸업 준비 처리
            mongEntity.graduateReady();

        } else {
            // 진화가 가능한 경우
            MongTypeEntity nextMongTypeEntity = mongTypeEntities.get(mongTypeEntities.size() - 1);

            // 진화 점수 계산
            double evolutionScore = DEFAULT_EVOLUTION_SCORE + mongEntity.getState().getReward() - mongEntity.getState().getPenalty();

            // 점수에 맞는 다음 몽 타입 코드 선정
            for (MongTypeEntity mongTypeEntity : mongTypeEntities) {
                if (mongTypeEntity.getEvolutionScore() <= evolutionScore) {
                    nextMongTypeEntity = mongTypeEntity;
                    break;
                }
            }

            // 진화 처리
            mongEntity.evolution(nextMongTypeEntity);
        }
    }

    /**
     * 몽 졸업
     * 1. 졸업 준비 상태인 경우에 가능
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     */
    @Transactional
    public void graduateMong(Long accountId, Long mongId) {

        MongEntity mongEntity = mongRepository.findByAccountIdAndMongIdAndStateIsActiveIsTrue(accountId, mongId)
                .orElseThrow(() -> new NotExistsMongException(accountId, mongId));

        if (mongEntity.isEgg()) {
            throw new InvalidMongTypeLevelException(mongId, mongEntity.getType().getMongCode().getComnCode(), mongEntity.getType().getLevel());
        }

        if (!MongStateCode.GRADUATE_READY.equals(mongEntity.getState().getCode())) {
            throw new InvalidGraduateException(accountId, mongId);
        }

        mongEntity.graduate();
    }
}
