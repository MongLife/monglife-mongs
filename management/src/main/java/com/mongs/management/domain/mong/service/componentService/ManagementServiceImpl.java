package com.mongs.management.domain.mong.service.componentService;

import com.mongs.core.entity.FoodCode;
import com.mongs.core.enums.management.*;
import com.mongs.core.enums.management.MongExp;
import com.mongs.core.enums.management.MongGrade;
import com.mongs.core.enums.management.MongState;
import com.mongs.core.vo.mqtt.*;
import com.mongs.management.domain.feedHistory.entity.FeedHistory;
import com.mongs.management.domain.feedHistory.repository.FeedHistoryRepository;
import com.mongs.management.domain.mong.controller.dto.response.*;
import com.mongs.management.domain.mong.entity.Mong;
import com.mongs.management.domain.mong.repository.FoodCodeRepository;
import com.mongs.management.domain.mong.service.moduleService.*;
import com.mongs.management.domain.mong.utils.MongUtil;
import com.mongs.management.exception.ManagementErrorCode;
import com.mongs.management.exception.ManagementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementServiceImpl implements ManagementService {

    private final MongUtil mongUtil;
    private final FoodCodeRepository foodCodeRepository;
    private final FeedHistoryRepository feedHistoryRepository;

    private final MongService mongService;
    private final CollectionService collectionService;
    private final LifecycleService lifecycleService;
    private final NotificationService notificationService;
    private final MongHistoryService mongHistoryService;

    @Value("${application.management.training-point}")
    private Integer TRAINING_POINT;
    @Value("${application.management.training-strength}")
    private Double TRAINING_STRENGTH;

    /*
    * 진화 체크 함수
    */
    private void evolutionValid(Mong mong) {
        if (mong.getExp() >= mong.getGrade().getNextGrade().getEvolutionExp() && !mong.getGrade().equals(MongGrade.ZERO)) {
            Mong saveMong = mongService.saveMong(mong.toBuilder()
                    .shift(MongShift.EVOLUTION_READY)
                    .build());

            notificationService.publishEvolutionReady(saveMong.getAccountId(), PublishEvolutionReadyVo.builder()
                    .mongId(saveMong.getId())
                    .shiftCode(saveMong.getShift().getCode())
                    .build());
        }
    }
    /*
     * 상태 체크 함수
     */
    private void stateChangeCheck(Mong mong) {
        AtomicReference<MongState> nextState = new AtomicReference<>(mong.getState());

        Arrays.stream(MongState.values())
                .forEach(mongState -> {
                    if (mongUtil.statusToPercent(mong.getWeight(), mong.getGrade()) < mongState.getWeightPercent() ||
                        mongUtil.statusToPercent(mong.getStrength(), mong.getGrade()) < mongState.getStrengthPercent() ||
                        mongUtil.statusToPercent(mong.getSatiety(), mong.getGrade()) < mongState.getSatietyPercent() ||
                        mongUtil.statusToPercent(mong.getHealthy(), mong.getGrade()) < mongState.getHealthyPercent() ||
                        mongUtil.statusToPercent(mong.getSleep(), mong.getGrade()) < mongState.getSleepPercent()
                    ) {
                        nextState.set(mongState);
                    }
                });

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .state(nextState.get())
                .build());

        notificationService.publishState(saveMong.getAccountId(), PublishStateVo.builder()
                .mongId(saveMong.getId())
                .stateCode(saveMong.getState().getCode())
                .build());
    }

    @Transactional
    public List<FindMongResDto> findAllMongAndCheckAttendance(Long accountId) {
        List<Mong> mongList = mongService.getAllMong(accountId);

        return mongList.stream()
                .map(mong -> FindMongResDto.builder()
                        .mongId(mong.getId())
                        .name(mong.getName())
                        .mongCode(mong.getMongCode())
                        .weight(mongUtil.statusToPercent(mong.getWeight(), mong.getGrade()))
                        .strength(mongUtil.statusToPercent(mong.getStrength(), mong.getGrade()))
                        .satiety(mongUtil.statusToPercent(mong.getSatiety(), mong.getGrade()))
                        .healthy(mongUtil.statusToPercent(mong.getHealthy(), mong.getGrade()))
                        .sleep(mongUtil.statusToPercent(mong.getSleep(), mong.getGrade()))
                        .exp(mongUtil.statusToPercent((double) mong.getExp(), mong.getGrade()))
                        .poopCount(mong.getNumberOfPoop())
                        .isSleeping(mong.getIsSleeping())
                        .stateCode(mong.getState().getCode())
                        .shiftCode(mong.getShift().getCode())
                        .payPoint(mong.getPayPoint())
                        .born(mong.getCreatedAt())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public RegisterMongResDto registerMong(Long accountId, String name, String sleepStart, String sleepEnd) {
        Mong mong = Mong.builder()
                .accountId(accountId)
                .name(name)
                .sleepTime(sleepStart)
                .wakeUpTime(sleepEnd)
                .grade(MongGrade.EMPTY)
                .build();

        String newMongCode = mongUtil.getNextMongCode(mong);

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .mongCode(newMongCode)
                .grade(MongGrade.ZERO)
                .build());

        lifecycleService.eggMongEvent(saveMong.getId())
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.FEIGN_CLIENT_FAIL));

        notificationService.publishCreate(saveMong.getAccountId(), PublishCreateVo.builder()
                .mongId(saveMong.getId())
                .name(saveMong.getName())
                .mongCode(saveMong.getMongCode())
                .weight(mongUtil.statusToPercent(saveMong.getWeight(), saveMong.getGrade()))
                .strength(mongUtil.statusToPercent(saveMong.getStrength(), saveMong.getGrade()))
                .satiety(mongUtil.statusToPercent(saveMong.getSatiety(), saveMong.getGrade()))
                .healthy(mongUtil.statusToPercent(saveMong.getHealthy(), saveMong.getGrade()))
                .sleep(mongUtil.statusToPercent(saveMong.getSleep(), saveMong.getGrade()))
                .exp(mongUtil.statusToPercent((double) saveMong.getExp(), saveMong.getGrade()))
                .poopCount(saveMong.getNumberOfPoop())
                .isSleeping(saveMong.getIsSleeping())
                .stateCode(saveMong.getState().getCode())
                .shiftCode(saveMong.getShift().getCode())
                .payPoint(saveMong.getPayPoint())
                .born(saveMong.getCreatedAt())
                .build());

        mongHistoryService.saveMongHistory(saveMong.getId(), MongHistoryCode.CREATE);

        return RegisterMongResDto.builder()
                .mongId(saveMong.getId())
                .name(saveMong.getName())
                .mongCode(saveMong.getMongCode())
                .weight(mongUtil.statusToPercent(saveMong.getWeight(), saveMong.getGrade()))
                .strength(mongUtil.statusToPercent(saveMong.getStrength(), saveMong.getGrade()))
                .satiety(mongUtil.statusToPercent(saveMong.getSatiety(), saveMong.getGrade()))
                .healthy(mongUtil.statusToPercent(saveMong.getHealthy(), saveMong.getGrade()))
                .sleep(mongUtil.statusToPercent(saveMong.getSleep(), saveMong.getGrade()))
                .exp(mongUtil.statusToPercent((double) saveMong.getExp(), saveMong.getGrade()))
                .poopCount(saveMong.getNumberOfPoop())
                .isSleeping(saveMong.getIsSleeping())
                .stateCode(saveMong.getState().getCode())
                .shiftCode(saveMong.getShift().getCode())
                .payPoint(saveMong.getPayPoint())
                .born(saveMong.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public DeleteMongResDto deleteMong(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .isActive(false)
                .numberOfPoop(0)
                .healthy(-1D)
                .satiety(-1D)
                .sleep(-1D)
                .strength(-1D)
                .weight(-1D)
                .shift(MongShift.DELETE)
                .state(MongState.EMPTY)
                .build());

        lifecycleService.deleteMongEvent(saveMong.getId())
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.FEIGN_CLIENT_FAIL));

        notificationService.publishDelete(saveMong.getAccountId(), PublishDeleteVo.builder()
                .mongId(saveMong.getId())
                .shiftCode(MongShift.DELETE.getCode())
                .build());

        mongHistoryService.saveMongHistory(saveMong.getId(), MongHistoryCode.DELETE);

        return DeleteMongResDto.builder()
                .mongId(saveMong.getId())
                .shiftCode(MongShift.DELETE.getCode())
                .build();
    }

    @Override
    @Transactional
    public StrokeMongResDto strokeMong(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);

        MongGrade grade = mong.getGrade();
        if (grade.equals(MongGrade.ZERO)) {
            throw new ManagementException(ManagementErrorCode.INVALID_STROKE);
        }

        int newNumberOfStroke = mong.getNumberOfStroke() + 1;
        int newExp = Math.min(mong.getExp() + MongExp.STROKE.getExp(), mong.getGrade().getNextGrade().getEvolutionExp());

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .numberOfStroke(newNumberOfStroke)
                .exp(newExp)
                .build());

        notificationService.publishStroke(saveMong.getAccountId(), PublishStrokeVo.builder()
                .mongId(saveMong.getId())
                .exp(mongUtil.statusToPercent((double) saveMong.getExp(), saveMong.getGrade()))
                .build());

        mongHistoryService.saveMongHistory(saveMong.getId(), MongHistoryCode.STROKE);

        this.evolutionValid(saveMong);

        return StrokeMongResDto.builder()
                .mongId(saveMong.getId())
                .exp(mongUtil.statusToPercent((double) saveMong.getExp(), saveMong.getGrade()))
                .build();
    }

    @Override
    @Transactional
    public FeedMongResDto feedMong(Long accountId, Long mongId, String foodCode) {
        Mong mong = mongService.getMong(mongId, accountId);

        FoodCode code = foodCodeRepository.findByCode(foodCode)
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.NOT_FOUND_FOOD_CODE));

        double newWeight = mong.getWeight() + code.addWeightValue();
        double newStrength = mong.getStrength() + code.addStrengthValue();
        double newSatiety = mong.getSatiety() + code.addSatietyValue();
        double newHealthy = mong.getHealthy() + code.addHealthyValue();
        double newSleep = mong.getSleep() + code.addSleepValue();

        int newExp = Math.min(mong.getExp() + MongExp.EAT_THE_FOOD.getExp(), mong.getGrade().getNextGrade().getEvolutionExp());

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .weight(newWeight)
                .strength(newStrength)
                .satiety(newSatiety)
                .healthy(newHealthy)
                .sleep(newSleep)
                .exp(newExp)
                .build());

        feedHistoryRepository.save(FeedHistory.builder()
                .mongId(saveMong.getId())
                .code(code.code())
                .price(code.price())
                .build());

        notificationService.publishFeed(saveMong.getAccountId(), PublishFeedVo.builder()
                .mongId(saveMong.getId())
                .weight(mongUtil.statusToPercent(saveMong.getWeight(), saveMong.getGrade()))
                .strength(mongUtil.statusToPercent(saveMong.getStrength(), saveMong.getGrade()))
                .satiety(mongUtil.statusToPercent(saveMong.getSatiety(), saveMong.getGrade()))
                .health(mongUtil.statusToPercent(saveMong.getHealthy(), saveMong.getGrade()))
                .sleep(mongUtil.statusToPercent(saveMong.getSleep(), saveMong.getGrade()))
                .exp(mongUtil.statusToPercent((double) saveMong.getExp(), saveMong.getGrade()))
                .payPoint(saveMong.getPayPoint())
                .build());

        mongHistoryService.saveMongHistory(saveMong.getId(), MongHistoryCode.FEED);

        this.evolutionValid(saveMong);
        this.stateChangeCheck(saveMong);

        return FeedMongResDto.builder()
                .mongId(saveMong.getId())
                .weight(mongUtil.statusToPercent(saveMong.getWeight(), saveMong.getGrade()))
                .strength(mongUtil.statusToPercent(saveMong.getStrength(), saveMong.getGrade()))
                .satiety(mongUtil.statusToPercent(saveMong.getSatiety(), saveMong.getGrade()))
                .healthy(mongUtil.statusToPercent(saveMong.getHealthy(), saveMong.getGrade()))
                .sleep(mongUtil.statusToPercent(saveMong.getSleep(), saveMong.getGrade()))
                .exp(mongUtil.statusToPercent((double) saveMong.getExp(), saveMong.getGrade()))
                .build();
    }

    @Override
    @Transactional
    public SleepingMongResDto sleepingMong(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);

        boolean newIsSleeping = !mong.getIsSleeping();

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .isSleeping(newIsSleeping)
                .build());

        if (saveMong.getIsSleeping()) {
            lifecycleService.sleepMongEvent(saveMong.getId())
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.FEIGN_CLIENT_FAIL));
        } else {
            lifecycleService.wakeUpMongEvent(saveMong.getId())
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.FEIGN_CLIENT_FAIL));
        }

        notificationService.publishSleeping(saveMong.getAccountId(), PublishSleepingVo.builder()
                .mongId(saveMong.getId())
                .isSleeping(saveMong.getIsSleeping())
                .build());

        mongHistoryService.saveMongHistory(saveMong.getId(), MongHistoryCode.SLEEP);

        return SleepingMongResDto.builder()
                .mongId(saveMong.getId())
                .isSleeping(saveMong.getIsSleeping())
                .build();
    }

    @Override
    @Transactional
    public PoopCleanResDto poopClean(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);

        int newExp = Math.min(mong.getExp() + MongExp.CLEANING_POOP.getExp(), mong.getGrade().getNextGrade().getEvolutionExp());

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .numberOfPoop(0)
                .exp(newExp)
                .build());

        notificationService.publishPoop(saveMong.getAccountId(), PublishPoopVo.builder()
                .mongId(saveMong.getId())
                .poopCount(saveMong.getNumberOfPoop())
                .exp(mongUtil.statusToPercent((double) saveMong.getExp(), saveMong.getGrade()))
                .build());

        mongHistoryService.saveMongHistory(saveMong.getId(), MongHistoryCode.POOP_CLEAN);

        this.evolutionValid(saveMong);

        return PoopCleanResDto.builder()
                .mongId(saveMong.getId())
                .poopCount(saveMong.getNumberOfPoop())
                .exp(mongUtil.statusToPercent((double) saveMong.getExp(), saveMong.getGrade()))
                .build();
    }

    @Override
    @Transactional
    public TrainingMongResDto trainingMong(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);

        if(mong.getPayPoint() < TRAINING_POINT) {
            throw new ManagementException(ManagementErrorCode.NOT_ENOUGH_PAYPOINT);
        }

        int newPayPoint = mong.getPayPoint() - TRAINING_POINT;
        int newNumberOfTraining = mong.getNumberOfTraining() + 1;
        double newStrength = mong.getStrength() + TRAINING_STRENGTH;

        int newExp = Math.min(mong.getExp() + MongExp.TRAINING.getExp(), mong.getGrade().getNextGrade().getEvolutionExp());

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .payPoint(newPayPoint)
                .numberOfTraining(newNumberOfTraining)
                .strength(newStrength)
                .exp(newExp)
                .build());

        notificationService.publishTraining(saveMong.getAccountId(), PublishTrainingVo.builder()
                .mongId(saveMong.getId())
                .payPoint(saveMong.getPayPoint())
                .strength(mongUtil.statusToPercent(saveMong.getStrength(), saveMong.getGrade()))
                .exp(mongUtil.statusToPercent((double) saveMong.getExp(), saveMong.getGrade()))
                .build());

        mongHistoryService.saveMongHistory(saveMong.getId(), MongHistoryCode.TRAINING);

        this.evolutionValid(saveMong);
        this.stateChangeCheck(saveMong);

        return TrainingMongResDto.builder()
                .mongId(saveMong.getId())
                .payPoint(saveMong.getPayPoint())
                .strength(mongUtil.statusToPercent(saveMong.getStrength(), saveMong.getGrade()))
                .exp(mongUtil.statusToPercent((double) saveMong.getExp(), saveMong.getGrade()))
                .build();
    }

    @Override
    @Transactional
    public GraduateMongResDto graduateMong(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);

        MongGrade grade = mong.getGrade();
        if (!grade.equals(MongGrade.LAST)) {
            throw new ManagementException(ManagementErrorCode.INVALID_GRADUATION);
        }

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .isActive(false)
                .numberOfPoop(0)
                .healthy(-1D)
                .satiety(-1D)
                .sleep(-1D)
                .strength(-1D)
                .weight(-1D)
                .shift(mongUtil.getNextShiftCode(mong))
                .state(mongUtil.getNextStateCode(mong))
                .build());

        notificationService.publishGraduation(saveMong.getAccountId(), PublishGraduationVo.builder()
                .mongId(saveMong.getId())
                .shiftCode(MongShift.DELETE.getCode())
                .build());

        mongHistoryService.saveMongHistory(saveMong.getId(), MongHistoryCode.GRADUATION);

        return GraduateMongResDto.builder()
                .mongId(saveMong.getId())
                .shiftCode(MongShift.DELETE.getCode())
                .build();
    }

    @Override
    @Transactional
    public EvolutionMongResDto evolutionMong(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);

        Boolean isSleeping = mong.getIsSleeping();
        if (isSleeping) {
            throw new ManagementException(ManagementErrorCode.INVALID_EVOLUTION);
        }

        MongShift shift = mong.getShift();
        if (!shift.equals(MongShift.EVOLUTION_READY)) {
            throw new ManagementException(ManagementErrorCode.INVALID_EVOLUTION);
        }

        MongGrade grade = mong.getGrade();
        if (grade.equals(MongGrade.LAST)) {
            throw new ManagementException(ManagementErrorCode.INVALID_EVOLUTION);
        }

        double exp = mong.getExp();
        if (exp < grade.getNextGrade().getEvolutionExp()) {
            throw new ManagementException(ManagementErrorCode.NOT_ENOUGH_EXP);
        }

        /* 비율 만큼 증가 */
        double weightPercent = mongUtil.statusToPercent(mong.getWeight(), mong.getGrade());
        double strengthPercent = mongUtil.statusToPercent(mong.getStrength(), mong.getGrade());
        double satietyPercent = mongUtil.statusToPercent(mong.getSatiety(), mong.getGrade());
        double healthyPercent = mongUtil.statusToPercent(mong.getHealthy(), mong.getGrade());
        double sleepPercent = mongUtil.statusToPercent(mong.getSleep(), mong.getGrade());

        double maxStatus = grade.getNextGrade().getMaxStatus();
        double newWeight = weightPercent / 100 * maxStatus;
        double newStrength = strengthPercent / 100 * maxStatus;
        double newSatiety = satietyPercent / 100 * maxStatus;
        double newHealthy = healthyPercent / 100 * maxStatus;
        double newSleep = sleepPercent / 100 * maxStatus;

        String preMongCode = mong.getMongCode();

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .mongCode(mongUtil.getNextMongCode(mong))
                .shift(mongUtil.getNextShiftCode(mong))
                .state(mongUtil.getNextStateCode(mong))
                .grade(grade.getNextGrade())
                .weight(newWeight)
                .strength(newStrength)
                .satiety(newSatiety)
                .healthy(newHealthy)
                .sleep(newSleep)
                .exp(0)
                .build());

        collectionService.registerMongCollection(saveMong.getAccountId(), preMongCode)
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.FEIGN_CLIENT_FAIL));

        if (saveMong.getGrade().equals(MongGrade.LAST)) {
            lifecycleService.graduateMongEvent(saveMong.getId())
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.FEIGN_CLIENT_FAIL));
        } else {
            lifecycleService.evolutionMongEvent(saveMong.getId())
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.FEIGN_CLIENT_FAIL));
        }

        mongHistoryService.saveMongHistory(saveMong.getId(), MongHistoryCode.EVOLUTION);

        notificationService.publishEvolution(saveMong.getAccountId(), PublishEvolutionVo.builder()
                .mongId(saveMong.getId())
                .mongCode(saveMong.getMongCode())
                .weight(mongUtil.statusToPercent(saveMong.getWeight(), saveMong.getGrade()))
                .strength(mongUtil.statusToPercent(saveMong.getStrength(), saveMong.getGrade()))
                .satiety(mongUtil.statusToPercent(saveMong.getSatiety(), saveMong.getGrade()))
                .health(mongUtil.statusToPercent(saveMong.getHealthy(), saveMong.getGrade()))
                .sleep(mongUtil.statusToPercent(saveMong.getSleep(), saveMong.getGrade()))
                .exp(mongUtil.statusToPercent((double) saveMong.getExp(), saveMong.getGrade()))
                .shiftCode(saveMong.getShift().getCode())
                .stateCode(saveMong.getState().getCode())
                .build());

        return EvolutionMongResDto.builder()
                .mongId(saveMong.getId())
                .mongCode(saveMong.getMongCode())
                .shiftCode(saveMong.getShift().getCode())
                .stateCode(saveMong.getState().getCode())
                .weight(mongUtil.statusToPercent(saveMong.getWeight(), saveMong.getGrade()))
                .strength(mongUtil.statusToPercent(saveMong.getStrength(), saveMong.getGrade()))
                .satiety(mongUtil.statusToPercent(saveMong.getSatiety(), saveMong.getGrade()))
                .healthy(mongUtil.statusToPercent(saveMong.getHealthy(), saveMong.getGrade()))
                .sleep(mongUtil.statusToPercent(saveMong.getSleep(), saveMong.getGrade()))
                .exp(mongUtil.statusToPercent((double) saveMong.getExp(), saveMong.getGrade()))
                .build();
    }
}
