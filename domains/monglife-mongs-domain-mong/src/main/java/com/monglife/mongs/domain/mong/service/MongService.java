package com.monglife.mongs.domain.mong.service;

import com.monglife.core.utils.CommonUtil;
import com.monglife.mongs.domain.mong.dto.etc.DecreaseMongStatusRatioDto;
import com.monglife.mongs.domain.mong.dto.etc.IncreaseMongStatusDto;
import com.monglife.mongs.domain.mong.dto.etc.IncreaseMongStatusRatioDto;
import com.monglife.mongs.domain.mong.dto.etc.PatchMongDto;
import com.monglife.mongs.domain.mong.entity.*;
import com.monglife.mongs.domain.mong.exception.*;
import com.monglife.mongs.domain.mong.repository.*;
import com.monglife.mongs.domain.mong.vo.FeedItemVo;
import com.monglife.mongs.domain.mong.vo.MongVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MongService {

    private static final Random random = new Random();

    @Value("${application.service.mong.egg-mong-type-group-type}")
    private String EGG_MONG_TYPE_GROUP_TYPE;

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

    @Value("${application.service.mong.default-stroke-expiration}")
    private Long DEFAULT_STROKE_EXPIRATION;

    private final MongRepository mongRepository;

    private final LockMongRepository lockMongRepository;

    private final MongFeedHistoryRepository mongFeedHistoryRepository;

    private final MongStrokeHistoryRepository mongStrokeHistoryRepository;

    private final MongTypeRepository mongTypeRepository;

    private final FoodTypeRepository foodTypeRepository;

    /**
     * 몽 목록 조회
     * @param accountId 계정 ID
     * @return 몽 목록
     */
    @Transactional(readOnly = true)
    public List<MongVo> getMongs(Long accountId) {

        List<MongEntity> mongEntities = mongRepository.findByAccountIdAndMetaIsActiveIsTrue(accountId);

        return mongEntities.stream()
                .map(MongVo::of)
                .toList();
    }

    /**
     * 몽 단건 조회
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @return 계정이 몽 소유 여부
     */
    @Transactional(readOnly = true)
    public Optional<MongVo> getMong(Long accountId, Long mongId) {

        Optional<MongEntity> optionalMongEntity = mongRepository.findByMongIdAndAccountIdAndMetaIsActiveIsTrue(mongId, accountId);

        Optional<MongVo> optionalMongVo = Optional.empty();

        if (optionalMongEntity.isPresent()) {
            optionalMongVo = Optional.of(MongVo.of(optionalMongEntity.get()));
        }

        return optionalMongVo;
    }

    /**
     * 몽 단건 조회
     * @param mongId 몽 ID
     * @return 몽 정보
     */
    @Transactional(readOnly = true)
    public MongVo getMong(Long mongId) {

        MongEntity mongEntity = mongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        return MongVo.of(mongEntity);
    }

    /**
     * 먹이 목록 조회
     * @param mongId 몽 ID
     * @param foodTypeGroupCode 음식 or 간식 그룹 코드
     * @return 구매 가능 여부를 포함한 음식/간식 목록
     */
    @Transactional(readOnly = true)
    public List<FeedItemVo> getFeedItems(Long mongId, String foodTypeGroupCode) {

        return foodTypeRepository.findByComnGroupCode(foodTypeGroupCode).stream()
                .map(foodTypeEntity -> {

                    String foodTypeCode = foodTypeEntity.getComn().getCode();

                    Boolean isCanBuy = mongFeedHistoryRepository.findByMongIdAndFoodTypeCode(mongId, foodTypeCode).isEmpty();

                    return FeedItemVo.builder()
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
    public MongVo createMong(Long accountId, String name, LocalTime sleepAt, LocalTime wakeupAt) {

        List<MongTypeEntity> mongTypeEntities = mongTypeRepository.findByGroupType(EGG_MONG_TYPE_GROUP_TYPE);

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

        return MongVo.of(mongEntity);
    }

    /**
     * 몽 삭제
     * @param mongId 몽 ID
     */
    @Transactional
    public void deleteMong(Long mongId) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
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

        mongFeedHistoryRepository.findByMongIdAndFoodTypeCode(mongId, foodTypeCode)
                .ifPresent(mongFeedHistory -> {
                    Long expirationSeconds = mongFeedHistory.getExpiration() - Duration.between(mongFeedHistory.getBuyAt(), LocalDateTime.now()).getSeconds();
                    throw new InvalidFeedException(mongId, foodTypeCode, expirationSeconds);
                });

        FoodTypeEntity foodTypeEntity = foodTypeRepository.findByComnCode(foodTypeCode)
                .orElseThrow(() -> new NotExistsFoodTypeCodeException(foodTypeCode));

        if (mongEntity.getPayPoint() < foodTypeEntity.getPrice()) {
            throw new NotEnoughPayPointException(mongId, foodTypeCode, foodTypeEntity.getPrice(), mongEntity.getPayPoint());
        }

        IncreaseMongStatusDto increaseMongStatusDto = IncreaseMongStatusDto.builder()
                .weight(foodTypeEntity.getAddWeightValue())
                .strength(foodTypeEntity.getAddStrengthValue())
                .satiety(foodTypeEntity.getAddSatietyValue())
                .healthy(foodTypeEntity.getAddHealthyValue())
                .fatigue(foodTypeEntity.getAddFatigueValue())
                .build();

        mongEntity.feed(foodTypeEntity.getPrice(), increaseMongStatusDto);

        MongFeedHistoryEntity mongFeedHistoryEntity = MongFeedHistoryEntity.builder()
                .mongFeedHistoryId(CommonUtil.randomId())
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

        mongStrokeHistoryRepository.findByMongId(mongId)
                .ifPresent(mongStrokeHistory -> {
                    Long expirationSeconds = mongStrokeHistory.getExpiration() - Duration.between(mongStrokeHistory.getStrokeAt(), LocalDateTime.now()).getSeconds();
                    throw new InvalidStrokeException(mongId, expirationSeconds);
                });

        mongEntity.stroke(DEFAULT_STROKE_EXP);

        MongStrokeHistoryEntity mongStrokeHistoryEntity = MongStrokeHistoryEntity.builder()
                .mongStrokeHistoryId(CommonUtil.randomId())
                .mongId(mongId)
                .strokeAt(LocalDateTime.now())
                .expiration(DEFAULT_STROKE_EXPIRATION)
                .build();

        mongStrokeHistoryRepository.save(mongStrokeHistoryEntity);
    }

    /**
     * 몽 수면
     * @param mongId 몽 ID
     */
    @Transactional
    public void sleepMong(Long mongId) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

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

        mongEntity.poopClean(DEFAULT_POOP_CLEAN_EXP);
    }

    /**
     * 몽 훈련
     * @param mongId 몽 ID
     * @param patchMongDto 갱신할 지수 Dto
     */
    @Transactional
    public void trainingMong(Long mongId, PatchMongDto patchMongDto) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        mongEntity.training(patchMongDto);
    }

    /**
     * 몽 배틀
     * @param mongId 몽 ID
     * @param exp 증가 경험치
     * @param payPoint 증가 페이 포인트
     */
    @Transactional
    public void battleMong(Long mongId, Double exp, Integer payPoint) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        mongEntity.battle(exp, payPoint);
    }

    /**
     * 몽 진화 준비
     * @param mongId 몽 ID
     */
    @Transactional
    public void evolutionReadyMong(Long mongId) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        if (mongEntity.isGraduateReady()) throw new InvalidMongStateException();

        if (mongEntity.isDead()) throw new InvalidMongStateException();

        mongEntity.evolutionReady();
    }

    /**
     * 몽 진화
     * 1. 진화 준비 상태인 경우에 가능
     * 2. 더이상 진화가 불가능 한 경우 졸업 준비로 상태 변경
     * @param mongId 몽 ID
     */
    @Transactional
    public MongVo evolutionMong(Long mongId, List<String> excludeMongTypeCodes) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        String nextGroupType = mongEntity.getType().getNextGroupType();
        List<MongTypeEntity> mongTypeEntities = mongTypeRepository.findByGroupType(nextGroupType).stream()
                .sorted((o1, o2) -> o2.getEvolutionScore().compareTo(o1.getEvolutionScore()))
                .toList();

        if (mongTypeEntities.isEmpty()) {
            // 더이상 진화할 수 없는 경우 졸업 준비 처리
            mongEntity.graduateReady();

        } else if (mongEntity.getType().getLevel() == 0) {

            // 중복된 몽 필터링
            List<MongTypeEntity> excludeDuplicateMongTypeEntities = mongTypeEntities.stream()
                    .filter(mongTypeEntity -> !excludeMongTypeCodes.contains(mongTypeEntity.getComn().getCode()))
                    .sorted((o1, o2) -> o2.getEvolutionScore().compareTo(o1.getEvolutionScore()))
                    .toList();

            // 1단계 몽을 모두 소유한 경우 목록 채움
            if (excludeDuplicateMongTypeEntities.isEmpty()) {
                excludeDuplicateMongTypeEntities = mongTypeEntities.stream().toList();
            }

            // 랜덤 선택
            int mongTypeCodeIndex = random.nextInt(0, excludeDuplicateMongTypeEntities.size());

            MongTypeEntity nextMongTypeEntity = excludeDuplicateMongTypeEntities.get(mongTypeCodeIndex);

            // 진화 처리
            mongEntity.evolution(nextMongTypeEntity, 0D);

        } else {
            // 진화가 가능한 경우
            MongTypeEntity nextMongTypeEntity = mongTypeEntities.get(mongTypeEntities.size() - 1);      // 시작은 패널티 몽 (까몽 까까몽)

            // 진화 점수 계산
            double evolutionScore = DEFAULT_EVOLUTION_SCORE;
            evolutionScore += mongEntity.getMeta().getReward();
            evolutionScore -= mongEntity.getMeta().getPenalty();
            evolutionScore += mongEntity.getMeta().getStrokeCount() * DEFAULT_STROKE_EVOLUTION_SCORE;
            evolutionScore += mongEntity.getMeta().getTrainingCount() * DEFAULT_TRAINING_EVOLUTION_SCORE;

            // 패널티 몽 필터링
            List<MongTypeEntity> excludePenaltyMongTypeEntities = mongTypeEntities.stream()
                    .filter(mongTypeEntity -> mongTypeEntity.getEvolutionScore() > 0)
                    .sorted(Comparator.comparing(MongTypeEntity::getEvolutionScore))
                    .toList();

            // 패널티 몽, 중복된 몽 필터링
            List<MongTypeEntity> excludePenaltyAndDuplicateMongTypeEntities = excludePenaltyMongTypeEntities.stream()
                    .filter(mongTypeEntity -> !excludeMongTypeCodes.contains(mongTypeEntity.getComn().getCode()))
                    .sorted((o1, o2) -> o2.getEvolutionScore().compareTo(o1.getEvolutionScore()))
                    .toList();

            if (excludePenaltyAndDuplicateMongTypeEntities.isEmpty()) {
                // 이미 다 보유한 상태
                int mongTypeCodeIndex = random.nextInt(0, excludePenaltyMongTypeEntities.size());
                // 선택 가능한 몽 중에서 랜덤 선택
                nextMongTypeEntity =  excludePenaltyMongTypeEntities.get(mongTypeCodeIndex);

            } else {
                // 가능한 몽 찾아서 선택
                for (MongTypeEntity mongTypeEntity : excludePenaltyAndDuplicateMongTypeEntities) {
                    if (mongTypeEntity.getEvolutionScore() <= evolutionScore) {
                        nextMongTypeEntity = mongTypeEntity;
                        break;
                    }
                }
            }

            // 리워드 (다음 진화 때 가산점)
            double reward = Math.max(0, Math.min(evolutionScore - 100D, 25D));

            // 진화 처리
            mongEntity.evolution(nextMongTypeEntity, reward);
        }

        return MongVo.of(mongEntity);
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

    /**
     * 몽 지수 증가
     * @param mongId 몽 ID
     * @param increaseMongStatusRatioDto 지수 증가 Dto
     */
    @Transactional
    public void increaseMongStatus(Long mongId, IncreaseMongStatusRatioDto increaseMongStatusRatioDto) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        mongEntity.increaseStatus(increaseMongStatusRatioDto);
    }

    /**
     * 몽 지수 감소
     * @param mongId 몽 ID
     * @param decreaseMongStatusRatioDto 지수 감소 Dto
     */
    @Transactional
    public void decreaseMongStatus(Long mongId, DecreaseMongStatusRatioDto decreaseMongStatusRatioDto) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        mongEntity.decreaseStatus(decreaseMongStatusRatioDto);
    }

    /**
     * 몽 배변 증가
     * @param mongId 몽 ID
     * @param poopCount 배변 증가 수
     */
    @Transactional
    public void increasePoop(Long mongId, Integer poopCount) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        mongEntity.increasePoop(poopCount);
    }

    /**
     * 페이 포인트 증가
     * @param mongId 몽 ID
     * @param payPoint 증가할 페이 포인트
     */
    @Transactional
    public void increasePayPoint(Long mongId, Integer payPoint) {

        MongEntity mongEntity = lockMongRepository.findByMongIdAndMetaIsActiveIsTrue(mongId)
                .orElseThrow(() -> new NotExistsMongException(mongId));

        mongEntity.increasePayPoint(payPoint);
    }
}
