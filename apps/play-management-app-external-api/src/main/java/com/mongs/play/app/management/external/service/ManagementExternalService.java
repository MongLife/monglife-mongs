package com.mongs.play.app.management.external.service;

import com.mongs.play.app.management.external.annotation.ValidationDead;
import com.mongs.play.app.management.external.annotation.ValidationEvolution;
import com.mongs.play.app.management.external.vo.*;
import com.mongs.play.client.publisher.event.annotation.RealTimeMong;
import com.mongs.play.client.publisher.event.code.PublishEventCode;
import com.mongs.play.core.error.app.ManagementExternalErrorCode;
import com.mongs.play.core.exception.app.ManagementExternalException;
import com.mongs.play.core.exception.common.InvalidException;
import com.mongs.play.core.exception.module.ModuleErrorException;
import com.mongs.play.domain.code.entity.FoodCode;
import com.mongs.play.domain.code.entity.MongCode;
import com.mongs.play.domain.code.service.CodeService;
import com.mongs.play.domain.mong.enums.MongGrade;
import com.mongs.play.domain.mong.enums.MongShift;
import com.mongs.play.domain.mong.enums.MongTrainingCode;
import com.mongs.play.domain.mong.service.MongService;
import com.mongs.play.domain.mong.utils.MongUtil;
import com.mongs.play.domain.mong.vo.MongFeedLogVo;
import com.mongs.play.domain.mong.vo.MongStatusPercentVo;
import com.mongs.play.domain.mong.vo.MongVo;
import com.mongs.play.module.feign.service.ManagementWorkerFeignService;
import com.mongs.play.module.feign.service.PlayerInternalCollectionFeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementExternalService {
    private final Random random = new Random();

    private final CodeService codeService;
    private final MongService mongService;
    private final ManagementWorkerFeignService managementWorkerFeignService;
    private final PlayerInternalCollectionFeignService playerInternalCollectionFeignService;

    @RealTimeMong(codes = { PublishEventCode.MONG_SHIFT })
    @Transactional
    public EvolutionReadyVo evolutionReady(Long mongId) {
        MongVo newMongVo = mongService.toggleEvolutionReady(mongId);

        return EvolutionReadyVo.builder()
                .mongId(newMongVo.mongId())
                .shiftCode(newMongVo.shift().code)
                .build();
    }

    @Transactional(readOnly = true)
    public List<FindMongVo> findMong(Long accountId) {

        List<MongVo> mongVoList = mongService.findMongByAccountId(accountId);

        return mongVoList.stream()
                .map(mongVo -> {
                    MongStatusPercentVo mongStatusPercentVo = MongUtil.statusToPercent(mongVo.grade(), mongVo);

                    return FindMongVo.builder()
                            .mongId(mongVo.mongId())
                            .name(mongVo.name())
                            .mongCode(mongVo.mongCode())
                            .weight(mongVo.weight())
                            .exp(mongVo.exp())
                            .strength(mongVo.strength())
                            .satiety(mongVo.satiety())
                            .healthy(mongVo.healthy())
                            .sleep(mongVo.sleep())
                            .expPercent(mongStatusPercentVo.exp())
                            .strengthPercent(mongStatusPercentVo.strength())
                            .satietyPercent(mongStatusPercentVo.satiety())
                            .healthyPercent(mongStatusPercentVo.healthy())
                            .sleepPercent(mongStatusPercentVo.sleep())
                            .stateCode(mongVo.state().code)
                            .shiftCode(mongVo.shift().code)
                            .poopCount(mongVo.poopCount())
                            .isSleeping(mongVo.isSleeping())
                            .payPoint(mongVo.payPoint())
                            .born(mongVo.born())
                            .build();
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public FindMongVo findMong(Long accountId, Long mongId) {
        MongVo mongVo = mongService.findActiveMongById(mongId);

        if (!accountId.equals(mongVo.accountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.NOT_MATCH_MONG);
        }

        MongStatusPercentVo mongStatusPercentVo = MongUtil.statusToPercent(mongVo.grade(), mongVo);

        return FindMongVo.builder()
                .mongId(mongVo.mongId())
                .name(mongVo.name())
                .mongCode(mongVo.mongCode())
                .weight(mongVo.weight())
                .exp(mongVo.exp())
                .strength(mongVo.strength())
                .satiety(mongVo.satiety())
                .healthy(mongVo.healthy())
                .sleep(mongVo.sleep())
                .expPercent(mongStatusPercentVo.exp())
                .strengthPercent(mongStatusPercentVo.strength())
                .satietyPercent(mongStatusPercentVo.satiety())
                .healthyPercent(mongStatusPercentVo.healthy())
                .sleepPercent(mongStatusPercentVo.sleep())
                .stateCode(mongVo.state().code)
                .shiftCode(mongVo.shift().code)
                .poopCount(mongVo.poopCount())
                .isSleeping(mongVo.isSleeping())
                .payPoint(mongVo.payPoint())
                .born(mongVo.born())
                .build();
    }

    @Transactional
    public RegisterMongVo registerMong(Long accountId, String name, String sleepStart, String sleepEnd) {

        List<MongCode> mongCodeList = codeService.getMongCodeByLevel(MongGrade.ZERO.level);
        int randIdx = random.nextInt(mongCodeList.size());

        String eggMongCode = mongCodeList.get(randIdx).getCode();

        MongVo newMongVo = mongService.addMong(accountId, eggMongCode, name, sleepStart, sleepEnd);
        MongStatusPercentVo mongStatusPercentVo = MongUtil.statusToPercent(newMongVo.grade(), newMongVo);

        playerInternalCollectionFeignService.registerMongCollection(accountId, newMongVo.mongCode());

        try {
            managementWorkerFeignService.zeroEvolutionSchedule(newMongVo.mongId());
        } catch (ModuleErrorException e) {
            playerInternalCollectionFeignService.removeMongCollection(accountId, newMongVo.mongCode());
        }

        return RegisterMongVo.builder()
                .mongId(newMongVo.mongId())
                .name(newMongVo.name())
                .mongCode(newMongVo.mongCode())
                .weight(newMongVo.weight())
                .exp(newMongVo.exp())
                .strength(newMongVo.strength())
                .satiety(newMongVo.satiety())
                .healthy(newMongVo.healthy())
                .sleep(newMongVo.sleep())
                .expPercent(mongStatusPercentVo.exp())
                .strengthPercent(mongStatusPercentVo.strength())
                .satietyPercent(mongStatusPercentVo.satiety())
                .healthyPercent(mongStatusPercentVo.healthy())
                .sleepPercent(mongStatusPercentVo.sleep())
                .stateCode(newMongVo.state().code)
                .shiftCode(newMongVo.shift().code)
                .poopCount(newMongVo.poopCount())
                .isSleeping(newMongVo.isSleeping())
                .payPoint(newMongVo.payPoint())
                .born(newMongVo.born())
                .build();
    }

    @RealTimeMong(codes = { PublishEventCode.MONG_SHIFT })
    @Transactional
    public DeleteMongVo deleteMong(Long accountId, Long mongId) {

        MongVo mongVo = mongService.findActiveMongById(mongId);

        if (!accountId.equals(mongVo.accountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.NOT_MATCH_MONG);
        }

        MongVo newMongVo = mongService.removeMong(mongVo.mongId());

        managementWorkerFeignService.deleteSchedule(newMongVo.mongId());

        return DeleteMongVo.builder()
                .mongId(newMongVo.mongId())
                .shiftCode(newMongVo.shift().code)
                .build();
    }

    @ValidationEvolution
    @RealTimeMong(codes = { PublishEventCode.MONG_EXP })
    @Transactional
    public StrokeMongVo strokeMong(Long accountId, Long mongId) {

        MongVo mongVo = mongService.findActiveMongById(mongId);

        if (!accountId.equals(mongVo.accountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.NOT_MATCH_MONG);
        }
        if (MongGrade.EMPTY.equals(mongVo.grade())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_STROKE);
        }
        if (MongGrade.ZERO.equals(mongVo.grade())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_STROKE);
        }
        if (MongGrade.LAST.equals(mongVo.grade())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_STROKE);
        }

        MongVo newMongVo = mongService.increaseNumberOfStroke(mongVo.mongId(), 1);

        double expPercent = MongUtil.statusToPercent(newMongVo.grade().evolutionExp, newMongVo.exp());

        return StrokeMongVo.builder()
                .mongId(newMongVo.mongId())
                .shiftCode(newMongVo.shift().code)
                .exp(newMongVo.exp())
                .expPercent(expPercent)
                .build();
    }

    @RealTimeMong(codes = { PublishEventCode.MONG_IS_SLEEPING })
    @Transactional
    public SleepingMongVo sleepingMong(Long accountId, Long mongId) {

        MongVo mongVo = mongService.findActiveMongById(mongId);

        if (!accountId.equals(mongVo.accountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.NOT_MATCH_MONG);
        }
        if (MongGrade.EMPTY.equals(mongVo.grade())) {
            throw new ManagementExternalException(mongVo.isSleeping() ? ManagementExternalErrorCode.INVALID_AWAKE : ManagementExternalErrorCode.INVALID_SLEEPING);
        }
        if (MongGrade.ZERO.equals(mongVo.grade())) {
            throw new ManagementExternalException(mongVo.isSleeping() ? ManagementExternalErrorCode.INVALID_AWAKE : ManagementExternalErrorCode.INVALID_SLEEPING);
        }
        if (MongGrade.LAST.equals(mongVo.grade())) {
            throw new ManagementExternalException(mongVo.isSleeping() ? ManagementExternalErrorCode.INVALID_AWAKE : ManagementExternalErrorCode.INVALID_SLEEPING);
        }

        MongVo newMongVo = mongService.toggleIsSleeping(mongVo.mongId());
        managementWorkerFeignService.sleepingSchedule(newMongVo.mongId(), newMongVo.isSleeping());

        return SleepingMongVo.builder()
                .mongId(newMongVo.mongId())
                .isSleeping(newMongVo.isSleeping())
                .build();
    }

    @ValidationEvolution
    @RealTimeMong(codes = { PublishEventCode.MONG_POOP_COUNT, PublishEventCode.MONG_EXP })
    @Transactional
    public PoopCleanMongVo poopClean(Long accountId, Long mongId) {

        MongVo mongVo = mongService.findActiveMongById(mongId);

        if (!accountId.equals(mongVo.accountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.NOT_MATCH_MONG);
        }
        if (MongGrade.EMPTY.equals(mongVo.grade())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_POOP_CLEAN);
        }
        if (MongGrade.ZERO.equals(mongVo.grade())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_POOP_CLEAN);
        }
        if (MongGrade.LAST.equals(mongVo.grade())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_POOP_CLEAN);
        }

        MongVo newMongVo = mongService.clearPoopCount(mongVo.mongId());

        double expPercent = MongUtil.statusToPercent(newMongVo.grade().evolutionExp, newMongVo.exp());

        return PoopCleanMongVo.builder()
                .mongId(newMongVo.mongId())
                .poopCount(newMongVo.poopCount())
                .shiftCode(newMongVo.shift().code)
                .exp(newMongVo.exp())
                .expPercent(expPercent)
                .build();
    }

    @Transactional
    public ValidationTrainingMongVo validationTrainingMong(Long mongId, String trainingCode) {

        MongVo mongVo = mongService.findActiveMongById(mongId);

        if (MongGrade.EMPTY.equals(mongVo.grade())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_TRAINING);
        }
        if (MongGrade.ZERO.equals(mongVo.grade())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_TRAINING);
        }

        MongTrainingCode mongTrainingCode = MongTrainingCode.findMongTrainingCode(trainingCode);

        boolean isPossible = mongVo.payPoint() >= mongTrainingCode.point;

        return ValidationTrainingMongVo.builder()
                .mongId(mongVo.mongId())
                .isPossible(isPossible)
                .build();
    }

    @ValidationEvolution
    @ValidationDead
    @RealTimeMong(codes = { PublishEventCode.MONG_EXP, PublishEventCode.MONG_STATUS, PublishEventCode.MONG_STATE, PublishEventCode.MONG_PAY_POINT })
    @Transactional
    public TrainingMongVo trainingMong(Long accountId, Long mongId, String trainingCode) {

        MongVo mongVo = mongService.findActiveMongById(mongId);

        MongTrainingCode mongTrainingCode = MongTrainingCode.findMongTrainingCode(trainingCode);

        if (!accountId.equals(mongVo.accountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.NOT_MATCH_MONG);
        }
        if (mongVo.payPoint() < mongTrainingCode.point) {
            throw new ManagementExternalException(ManagementExternalErrorCode.NOT_ENOUGH_PAY_POINT);
        }
        if (MongGrade.EMPTY.equals(mongVo.grade())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_TRAINING);
        }
        if (MongGrade.ZERO.equals(mongVo.grade())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_TRAINING);
        }

        MongVo newMongVo = mongService.increaseStatusTraining(mongVo.mongId(), 1, mongTrainingCode);
        MongStatusPercentVo mongStatusPercentVo = MongUtil.statusToPercent(mongVo.grade(), newMongVo);

        return TrainingMongVo.builder()
                .mongId(newMongVo.mongId())
                .weight(newMongVo.weight())
                .shiftCode(newMongVo.state().code)
                .shiftCode(newMongVo.shift().code)
                .exp(newMongVo.exp())
                .strength(newMongVo.strength())
                .satiety(newMongVo.satiety())
                .healthy(newMongVo.healthy())
                .sleep(newMongVo.sleep())
                .expPercent(mongStatusPercentVo.exp())
                .strengthPercent(mongStatusPercentVo.strength())
                .satietyPercent(mongStatusPercentVo.satiety())
                .healthyPercent(mongStatusPercentVo.healthy())
                .sleepPercent(mongStatusPercentVo.sleep())
                .payPoint(newMongVo.payPoint())
                .isDeadSchedule(newMongVo.isDeadSchedule())
                .build();
    }

    @RealTimeMong(codes = { PublishEventCode.MONG_SHIFT })
    @Transactional
    public GraduateMongVo graduateMong(Long accountId, Long mongId) {

        MongVo mongVo = mongService.findActiveMongById(mongId);

        if (!accountId.equals(mongVo.accountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.NOT_MATCH_MONG);
        }
        if (!MongGrade.LAST.equals(mongVo.grade())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_GRADUATION);
        }
        if (!MongShift.GRADUATE_READY.equals(mongVo.shift())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_GRADUATION);
        }

        MongVo newMongVo = mongService.toggleGraduate(mongVo.mongId());

        return GraduateMongVo.builder()
                .mongId(newMongVo.mongId())
                .shiftCode(newMongVo.shift().code)
                .build();
    }

    @RealTimeMong(codes = { PublishEventCode.MONG_CODE, PublishEventCode.MONG_EXP, PublishEventCode.MONG_STATUS, PublishEventCode.MONG_STATE, PublishEventCode.MONG_SHIFT })
    @Transactional
    public EvolutionMongVo evolutionMong(Long accountId, Long mongId) {

        MongVo mongVo = mongService.findActiveMongById(mongId);

        if (!accountId.equals(mongVo.accountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.NOT_MATCH_MONG);
        }
        if (MongGrade.LAST.equals(mongVo.grade())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_EVOLUTION);
        }
        if (!MongShift.EVOLUTION_READY.equals(mongVo.shift())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_EVOLUTION);
        }
        if (mongVo.isSleeping()) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_EVOLUTION);
        }
        if (mongVo.exp() < mongVo.grade().evolutionExp) {
            throw new ManagementExternalException(ManagementExternalErrorCode.NOT_ENOUGH_EXP);
        }

        MongVo newMongVo;
        if (MongGrade.ZERO.equals(mongVo.grade())) {
            List<MongCode> mongCodeList = codeService.getMongCodeByLevel(MongGrade.ZERO.nextGrade.level);
            int randIdx = random.nextInt(mongCodeList.size());
            String mongCode = mongCodeList.get(randIdx).getCode();
            newMongVo = mongService.toggleFirstEvolution(mongVo.mongId(), mongCode);
            playerInternalCollectionFeignService.registerMongCollection(accountId, newMongVo.mongCode());
            try {
                managementWorkerFeignService.firstEvolutionSchedule(mongVo.mongId());
            } catch (ModuleErrorException e) {
                playerInternalCollectionFeignService.removeMongCollection(accountId, newMongVo.mongCode());
            }
        } else if (MongGrade.LAST.equals(mongVo.grade().nextGrade)) {
            newMongVo = mongService.toggleLastEvolution(mongVo.mongId());
            managementWorkerFeignService.lastEvolutionSchedule(mongVo.mongId());
        } else {
            int strokePoint = mongVo.numberOfStroke() * 2;
            int trainingPoint = mongVo.numberOfTraining() * 3;
            int penaltyPoint = mongVo.penalty();

            int evolutionPoint = Math.max(mongVo.evolutionPoint() + strokePoint + trainingPoint - penaltyPoint, 0);
            List<MongCode> mongCodeList = codeService.getMongCodeByLevelAndEvolutionPoint(mongVo.grade().nextGrade.level, evolutionPoint);

            MongCode mongCode = codeService.getMongCode(mongVo.mongCode());
            String newMongCode = mongCodeList.stream()
                    .filter(code -> code.getBaseCode().equals(mongCode.getBaseCode()))
                    .min((o1, o2) -> o2.getEvolutionPoint() - o1.getEvolutionPoint())
                    .orElseGet(() -> mongCodeList.get(0))
                    .getCode();
            int newEvolutionPoint = evolutionPoint - 300 + 150;

            newMongVo = mongService.toggleEvolution(mongVo.mongId(), newMongCode, newEvolutionPoint);
            playerInternalCollectionFeignService.registerMongCollection(accountId, newMongVo.mongCode());
            try {
                managementWorkerFeignService.evolutionSchedule(mongVo.mongId());
            } catch (ModuleErrorException e) {
                playerInternalCollectionFeignService.removeMongCollection(accountId, newMongVo.mongCode());
            }
        }

        MongStatusPercentVo mongStatusPercentVo = MongUtil.statusToPercent(newMongVo.grade(), newMongVo);

        return EvolutionMongVo.builder()
                .mongId(newMongVo.mongId())
                .mongCode(newMongVo.mongCode())
                .weight(newMongVo.weight())
                .exp(newMongVo.exp())
                .strength(newMongVo.strength())
                .satiety(newMongVo.satiety())
                .healthy(newMongVo.healthy())
                .sleep(newMongVo.sleep())
                .expPercent(mongStatusPercentVo.exp())
                .strengthPercent(mongStatusPercentVo.strength())
                .satietyPercent(mongStatusPercentVo.satiety())
                .healthyPercent(mongStatusPercentVo.healthy())
                .sleepPercent(mongStatusPercentVo.sleep())
                .stateCode(newMongVo.state().code)
                .shiftCode(newMongVo.shift().code)
                .build();
    }

    @ValidationEvolution
    @ValidationDead
    @RealTimeMong(codes = { PublishEventCode.MONG_EXP, PublishEventCode.MONG_STATUS, PublishEventCode.MONG_STATE, PublishEventCode.MONG_PAY_POINT })
    @Transactional
    public FeedMongVo feedMong(Long accountId, Long mongId, String foodCode) {

        MongVo mongVo = mongService.findActiveMongById(mongId);

        FoodCode food = codeService.getFoodCode(foodCode);

        if (!accountId.equals(mongVo.accountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.NOT_MATCH_MONG);
        }
        if (mongVo.payPoint() < food.getPrice()) {
            throw new InvalidException(ManagementExternalErrorCode.NOT_ENOUGH_PAY_POINT);
        }
        if (MongGrade.EMPTY.equals(mongVo.grade())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_FEED);
        }
        if (MongGrade.ZERO.equals(mongVo.grade())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.INVALID_FEED);
        }

        MongVo newMongVo = mongService.feedMong(mongVo.mongId(), food.getCode(), food.getAddWeightValue(), food.getAddStrengthValue(), food.getAddSatietyValue(), food.getAddHealthyValue(), food.getAddSleepValue(), food.getPrice());

        MongStatusPercentVo mongStatusPercentVo = MongUtil.statusToPercent(newMongVo.grade(), newMongVo);

        return FeedMongVo.builder()
                .mongId(newMongVo.mongId())
                .weight(newMongVo.weight())
                .stateCode(newMongVo.state().code)
                .shiftCode(newMongVo.shift().code)
                .exp(newMongVo.exp())
                .strength(newMongVo.strength())
                .satiety(newMongVo.satiety())
                .healthy(newMongVo.healthy())
                .sleep(newMongVo.sleep())
                .expPercent(mongStatusPercentVo.exp())
                .strengthPercent(mongStatusPercentVo.strength())
                .satietyPercent(mongStatusPercentVo.satiety())
                .healthyPercent(mongStatusPercentVo.healthy())
                .sleepPercent(mongStatusPercentVo.sleep())
                .payPoint(newMongVo.payPoint())
                .isDeadSchedule(newMongVo.isDeadSchedule())
                .build();
    }

    @Transactional
    public List<FindFeedLogVo> findFeedLog(Long accountId, Long mongId) {

        MongVo mongVo = mongService.findActiveMongById(mongId);

        if (!accountId.equals(mongVo.accountId())) {
            throw new ManagementExternalException(ManagementExternalErrorCode.NOT_MATCH_MONG);
        }

        List<MongFeedLogVo> mongFeedLogVoList = mongService.findMongFeedLogByMongId(mongVo.mongId());

        Map<String, MongFeedLogVo> mongFeedLogVoMap = mongFeedLogVoList.stream()
                .collect(Collectors.toMap(MongFeedLogVo::code, mongFeedLogVo -> mongFeedLogVo));

        List<FoodCode> foodCodeList = codeService.getFoodCode();

        return foodCodeList.stream()
                .map(foodCode -> {
                    String code = foodCode.getCode();
                    long id = mongVo.mongId();
                    boolean isCanBuy = true;

                    // 음식 섭취 기록이 있으면
                    if (mongFeedLogVoMap.containsKey(foodCode.getCode())) {
                        MongFeedLogVo mongFeedLogVo = mongFeedLogVoMap.get(foodCode.getCode());
                        isCanBuy = mongFeedLogVo.lastBuyAt()
                                .plusSeconds(foodCode.getDelaySeconds())
                                .isBefore(LocalDateTime.now());
                    }

                    return FindFeedLogVo.builder()
                            .code(code)
                            .mongId(id)
                            .isCanBuy(isCanBuy)
                            .build();
                })
                .toList();
    }
}
