package com.monglife.mongs.app.manager.management.service;

import com.monglife.mongs.app.manager.management.domain.FoodTypeEntity;
import com.monglife.mongs.app.manager.management.domain.MongEntity;
import com.monglife.mongs.app.manager.management.domain.MongFeedHistoryEntity;
import com.monglife.mongs.app.manager.management.domain.MongTypeEntity;
import com.monglife.mongs.app.manager.management.dto.etc.GetFeedItemDto;
import com.monglife.mongs.app.manager.management.dto.etc.GetMongDto;
import com.monglife.mongs.app.manager.management.dto.etc.UpdateMongStatusDto;
import com.monglife.mongs.app.manager.management.enums.MongShiftCode;
import com.monglife.mongs.app.manager.management.exception.*;
import com.monglife.mongs.app.manager.management.repository.FoodTypeRepository;
import com.monglife.mongs.app.manager.management.repository.MongFeedHistoryRepository;
import com.monglife.mongs.app.manager.management.repository.MongRepository;
import com.monglife.mongs.app.manager.management.repository.MongTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementService {

    private static final Random random = new Random();

    private static final String  MONG_TYPE_EGG_GROUP_CODE = "GCH000";

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

        List<MongEntity> mongEntities = mongRepository.findByAccountIdAndIsActiveIsTrue(accountId);

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

        MongEntity mongEntity = mongRepository.findByAccountIdAndMongIdAndIsActiveIsTrue(accountId, mongId)
                .orElseThrow(() -> new NotExistsMongException(accountId, mongId));

        return GetMongDto.of(mongEntity);
    }

    /**
     * 음식/간식 목록 조회
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @return 구매 가능 여부를 포함한 음식/간식 목록
     */
    @Transactional(readOnly = true)
    public List<GetFeedItemDto> getFeedItems(Long accountId, Long mongId, String foodTypeGroupCode) {

        mongRepository.findByAccountIdAndMongIdAndIsActiveIsTrue(accountId, mongId)
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
    public void createMong(Long accountId, String name, LocalTime sleepAt, LocalTime wakeupAt) {

        List<MongTypeEntity> mongTypeEntities = mongTypeRepository.findByMongCodeGroupCode(MONG_TYPE_EGG_GROUP_CODE);

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

        // TODO: 알 깨기 스케줄러 시작
    }

    /**
     * 몽 삭제
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     */
    @Transactional
    public void deleteMong(Long accountId, Long mongId) {

        MongEntity mongEntity = mongRepository.findByAccountIdAndMongIdAndIsActiveIsTrue(accountId, mongId)
                .orElseThrow(() -> new NotExistsMongException(accountId, mongId));

        // TODO: 모든 스케줄러 중단

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
                    LocalDateTime expirationAt = LocalDateTime.now().plusNanos(mongFeedHistory.getExpiration());
                    throw new InvalidFeedException(mongId, foodTypeCode, expirationAt); });

        FoodTypeEntity foodTypeEntity = foodTypeRepository.findByFoodCodeComnCode(foodTypeCode)
                .orElseThrow(() -> new NotExistsFoodTypeCodeException(foodTypeCode));

        MongEntity mongEntity = mongRepository.findByAccountIdAndMongIdAndIsActiveIsTrue(accountId, mongId)
                .orElseThrow(() -> new NotExistsMongException(accountId, mongId));

        UpdateMongStatusDto updateMongStatusDto = UpdateMongStatusDto.builder()
                .addWeightValue(foodTypeEntity.getAddWeightValue())
                .addStrengthValue(foodTypeEntity.getAddStrengthValue())
                .addSatietyValue(foodTypeEntity.getAddSatietyValue())
                .addHealthyValue(foodTypeEntity.getAddHealthyValue())
                .addFatigueValue(foodTypeEntity.getAddFatigueValue())
                .build();

        mongEntity.feed(updateMongStatusDto);

        MongFeedHistoryEntity mongFeedHistoryEntity = MongFeedHistoryEntity.builder()
                .mongFeedHistoryId(UUID.randomUUID().toString())
                .mongId(mongId)
                .foodTypeCode(foodTypeCode)
                .expiration(foodTypeEntity.getDelaySeconds() * 1000L)
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

        MongEntity mongEntity = mongRepository.findByAccountIdAndMongIdAndIsActiveIsTrue(accountId, mongId)
                .orElseThrow(() -> new NotExistsMongException(accountId, mongId));

        mongEntity.stroke(DEFAULT_STROKE_EXP);
    }

    /**
     * 몽 수면/기상
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     */
    @Transactional
    public void sleepMong(Long accountId, Long mongId) {

        MongEntity mongEntity = mongRepository.findByAccountIdAndMongIdAndIsActiveIsTrue(accountId, mongId)
                .orElseThrow(() -> new NotExistsMongException(accountId, mongId));

        if (mongEntity.getIsSleep()) {
            mongEntity.wakeup();
        } else {
            mongEntity.sleep();
        }

        // TODO: 수면 스케줄러 변경
    }

    /**
     * 배변 처리
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     */
    @Transactional
    public void poopCleanMong(Long accountId, Long mongId) {

        MongEntity mongEntity = mongRepository.findByAccountIdAndMongIdAndIsActiveIsTrue(accountId, mongId)
                .orElseThrow(() -> new NotExistsMongException(accountId, mongId));

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

        MongEntity mongEntity = mongRepository.findByAccountIdAndMongIdAndIsActiveIsTrue(accountId, mongId)
                .orElseThrow(() -> new NotExistsMongException(accountId, mongId));

        if (!MongShiftCode.EVOLUTION_READY.equals(mongEntity.getMongShiftCode())) {
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
            double evolutionScore = DEFAULT_EVOLUTION_SCORE + mongEntity.getReward() - mongEntity.getPenalty();

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

        MongEntity mongEntity = mongRepository.findByAccountIdAndMongIdAndIsActiveIsTrue(accountId, mongId)
                .orElseThrow(() -> new NotExistsMongException(accountId, mongId));

        if (!MongShiftCode.GRADUATE_READY.equals(mongEntity.getMongShiftCode())) {
            throw new InvalidGraduateException(accountId, mongId);
        }

        mongEntity.graduate();
    }
}
