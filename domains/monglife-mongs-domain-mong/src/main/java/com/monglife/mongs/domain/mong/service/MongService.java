package com.monglife.mongs.domain.mong.service;

import com.monglife.mongs.domain.mong.dto.etc.*;
import com.monglife.mongs.domain.mong.entity.type.FoodTypeEntity;
import com.monglife.mongs.domain.mong.entity.data.MongEntity;
import com.monglife.mongs.domain.mong.entity.history.MongFeedHistoryEntity;
import com.monglife.mongs.domain.mong.entity.type.MongTypeEntity;
import com.monglife.mongs.domain.mong.exception.*;
import com.monglife.mongs.domain.mong.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MongService {

    private static final Random random = new Random();

    @Value("${application.service.mong.egg-mong-type-group-code}")
    private String  EGG_MONG_TYPE_GROUP_CODE;

    @Value("${application.service.mong.default-evolution-score}")
    private Integer DEFAULT_EVOLUTION_SCORE;

    @Value("${application.service.mong.default-pay-point}")
    private Integer DEFAULT_PAY_POINT;

    @Value("${application.service.mong.default-stroke-exp}")
    private Double  DEFAULT_STROKE_EXP;

    @Value("${application.service.mong.default-poop-clean-exp}")
    private Double  DEFAULT_POOP_CLEAN_EXP;

    @Value("${application.service.mong.default-stroke-evolution-score}")
    private Integer DEFAULT_STROKE_EVOLUTION_SCORE;

    @Value("${application.service.mong.default-training-evolution-score}")
    private Integer DEFAULT_TRAINING_EVOLUTION_SCORE;

    private final MongRepository mongRepository;

    private final LockMongRepository lockMongRepository;

    private final MongFeedHistoryRepository mongFeedHistoryRepository;

    private final MongTypeRepository mongTypeRepository;

    private final FoodTypeRepository foodTypeRepository;


    /**
     * 몽 소유자 여부 확인
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @return 계정이 몽 소유 여부
     */
    @Transactional(readOnly = true)
    public Boolean validateMongByAccountId(Long accountId, Long mongId) {

        MongEntity mongEntity =  mongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        return mongEntity.getAccountId().equals(accountId);
    }

    /**
     * 몽 목록 조회
     * @param accountId 계정 ID
     * @return 몽 목록
     */
    @Transactional(readOnly = true)
    public List<GetMongDto> getMongs(Long accountId) {

        List<MongEntity> mongEntities = mongRepository.findByAccountIdAndMetaIsActiveIsTrue(accountId);

        return mongEntities.stream()
                .map(GetMongDto::of)
                .toList();
    }

    /**
     * 몽 단건 조회
     * @param mongId 몽 ID
     * @return 몽 정보
     */
    @Transactional(readOnly = true)
    public GetMongDto getMong(Long mongId) {

        MongEntity mongEntity = mongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        return GetMongDto.of(mongEntity);
    }

    /**
     * 먹이 목록 조회
     * @param mongId 몽 ID
     * @param foodTypeGroupCode 음식 or 간식 그룹 코드
     * @return 구매 가능 여부를 포함한 음식/간식 목록
     */
    @Transactional(readOnly = true)
    public List<GetFeedItemDto> getFeedItems(Long mongId, String foodTypeGroupCode) {

        return foodTypeRepository.findByComnGroupCode(foodTypeGroupCode).stream()
                .map(foodTypeEntity -> {

                    String foodTypeCode = foodTypeEntity.getComn().getCode();

                    Boolean isCanBuy = mongFeedHistoryRepository.findByMongIdAndFoodTypeCode(mongId, foodTypeCode).isEmpty();

                    return GetFeedItemDto.builder()
                            .isCanBuy(isCanBuy)
                            .foodTypeCode(foodTypeCode)
                            .price(foodTypeEntity.getPrice())
                            .foodTypeName(foodTypeEntity.getComn().getName())
                            .foodTypeGroupCode(foodTypeEntity.getComn().getGroup().getCode())
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

        List<MongTypeEntity> mongTypeEntities = mongTypeRepository.findByComnGroupCode(EGG_MONG_TYPE_GROUP_CODE);

        if (mongTypeEntities.isEmpty()) throw new NotExistsMongTypeCodeException();

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
     * @param mongId 몽 ID
     */
    @Transactional
    public void deleteMong(Long mongId) {

        MongEntity mongEntity = mongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        mongEntity.delete();
    }

    /**
     * 몽 먹이 주기
     * @param mongId 몽 ID
     * @param foodTypeCode 음식/간식 타입 코드
     */
    @Transactional
    public void feedMong(Long mongId, String foodTypeCode) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        if (mongEntity.isDead()) throw new InvalidMongStateException();

        if (mongEntity.getState().getIsSleep()) throw new InvalidMongStateException();

        if (mongEntity.isEgg()) throw new InvalidMongTypeLevelException(mongId, mongEntity.getType().getComn().getCode(), mongEntity.getType().getLevel());

        mongFeedHistoryRepository.findByMongIdAndFoodTypeCode(mongId, foodTypeCode)
                .ifPresent(mongFeedHistory -> {
                    Long expirationSeconds = mongFeedHistory.getExpiration() - Duration.between(mongFeedHistory.getBuyAt(), LocalDateTime.now()).getSeconds();
                    throw new InvalidFeedException(mongId, foodTypeCode, expirationSeconds);
                });

        FoodTypeEntity foodTypeEntity = foodTypeRepository.findByComnCode(foodTypeCode)
                .orElseThrow(() -> new NotExistsFoodTypeCodeException(foodTypeCode));

        if (mongEntity.getPayPoint() < foodTypeEntity.getPrice()) throw new NotEnoughPayPointException(mongId, foodTypeCode, foodTypeEntity.getPrice(), mongEntity.getPayPoint());

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
     * @param mongId 몽 ID
     */
    @Transactional
    public void strokeMong(Long mongId) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        if (mongEntity.isDead()) throw new InvalidMongStateException();

        if (mongEntity.getState().getIsSleep()) throw new InvalidMongStateException();

        if (mongEntity.isEgg()) throw new InvalidMongTypeLevelException(mongId, mongEntity.getType().getComn().getCode(), mongEntity.getType().getLevel());

        mongEntity.stroke(DEFAULT_STROKE_EXP);
    }

    /**
     * 몽 수면
     * @param mongId 몽 ID
     */
    @Transactional
    public void sleepMong(Long mongId) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        if (mongEntity.isDead()) throw new InvalidMongStateException();

        if (mongEntity.isEgg()) throw new InvalidMongTypeLevelException(mongId, mongEntity.getType().getComn().getCode(), mongEntity.getType().getLevel());

        mongEntity.sleep();
    }

    /**
     * 몽 기상
     * @param mongId 몽 ID
     */
    @Transactional
    public void wakeupMong(Long mongId) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        if (mongEntity.isDead()) throw new InvalidMongStateException();

        if (mongEntity.isEgg()) throw new InvalidMongTypeLevelException(mongId, mongEntity.getType().getComn().getCode(), mongEntity.getType().getLevel());

        mongEntity.wakeup();
    }

    /**
     * 배변 처리
     * @param mongId 몽 ID
     */
    @Transactional
    public void poopCleanMong(Long mongId) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        if (mongEntity.isDead()) throw new InvalidMongStateException();

        if (mongEntity.getState().getIsSleep()) throw new InvalidMongStateException();

        if (mongEntity.isEgg()) throw new InvalidMongTypeLevelException(mongId, mongEntity.getType().getComn().getCode(), mongEntity.getType().getLevel());

        mongEntity.poopClean(DEFAULT_POOP_CLEAN_EXP);
    }

    /**
     * 몽 진화 준비
     * @param mongId 몽 ID
     */
    @Transactional
    public void evolutionReadyMong(Long mongId) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        mongEntity.evolutionReady();
    }

    /**
     * 몽 진화
     * 1. 진화 준비 상태인 경우에 가능
     * 2. 더이상 진화가 불가능 한 경우 졸업 준비로 상태 변경
     * @param mongId 몽 ID
     */
    @Transactional
    public void evolutionMong(Long mongId) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        if (mongEntity.isDead()) throw new InvalidMongStateException();

        if (mongEntity.getState().getIsSleep()) throw new InvalidMongStateException();

        if (!mongEntity.isEvolutionReady()) throw new InvalidEvolutionException(mongId);

        String nextTypeGroupCode = mongEntity.getType().getNextTypeGroupCode();
        List<MongTypeEntity> mongTypeEntities = mongTypeRepository.findByComnGroupCode(nextTypeGroupCode).stream()
                .sorted((o1, o2) -> o2.getEvolutionScore().compareTo(o1.getEvolutionScore()))
                .toList();

        if (mongTypeEntities.isEmpty()) {
            // 더이상 진화할 수 없는 경우 졸업 준비 처리
            mongEntity.graduateReady();

        } else {
            // 진화가 가능한 경우
            MongTypeEntity nextMongTypeEntity = mongTypeEntities.get(mongTypeEntities.size() - 1);

            // 진화 점수 계산
            double evolutionScore = DEFAULT_EVOLUTION_SCORE;
            evolutionScore += mongEntity.getMeta().getReward();
            evolutionScore -= mongEntity.getMeta().getPenalty();
            evolutionScore += mongEntity.getMeta().getStrokeCount() * DEFAULT_STROKE_EVOLUTION_SCORE;
            evolutionScore += mongEntity.getMeta().getTrainingCount() * DEFAULT_TRAINING_EVOLUTION_SCORE;

            // 점수에 맞는 다음 몽 타입 코드 선정
            for (MongTypeEntity mongTypeEntity : mongTypeEntities) {
                if (mongTypeEntity.getEvolutionScore() <= evolutionScore) {
                    nextMongTypeEntity = mongTypeEntity;
                    break;
                }
            }

            // 진화 처리
            double reward = Math.max(0, evolutionScore - DEFAULT_EVOLUTION_SCORE);
            mongEntity.evolution(nextMongTypeEntity, reward);
        }
    }

    /**
     * 몽 졸업
     * 1. 졸업 준비 상태인 경우에 가능
     * @param mongId 몽 ID
     */
    @Transactional
    public void graduateMong(Long mongId) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        if (mongEntity.isDead()) throw new InvalidMongStateException();

        if (mongEntity.getState().getIsSleep()) throw new InvalidMongStateException();

        if (mongEntity.isEgg()) throw new InvalidMongTypeLevelException(mongId, mongEntity.getType().getComn().getCode(), mongEntity.getType().getLevel());

        if (!mongEntity.isGraduateReady()) throw new InvalidGraduateException(mongId);

        mongEntity.graduate();
    }

    /**
     * 몽 사망
     * @param mongId 몽 ID
     */
    @Transactional
    public void deadMong(Long mongId) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        mongEntity.dead();
    }
    
    @Transactional
    public void increaseMongStatus(Long mongId, IncreaseMongStatusDto increaseMongStatusDto) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        mongEntity.increaseStatus(increaseMongStatusDto);
    }
    
    @Transactional
    public void decreaseMongStatus(Long mongId, DecreaseMongStatusDto decreaseMongStatusDto) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        mongEntity.decreaseStatus(decreaseMongStatusDto);
    }
    
    @Transactional
    public void increasePoop(Long mongId, Integer addPoopCount) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        mongEntity.increasePoop(addPoopCount);
    }

    @Transactional
    public void increasePayPoint(Long mongId, Integer payPoint) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        mongEntity.increasePayPoint(payPoint);
    }
}
