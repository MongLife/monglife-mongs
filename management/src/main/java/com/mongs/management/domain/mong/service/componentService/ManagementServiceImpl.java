package com.mongs.management.domain.mong.service.componentService;

import com.mongs.core.entity.FoodCode;
import com.mongs.core.enums.management.MongEXP;
import com.mongs.core.enums.management.MongGrade;
import com.mongs.core.enums.management.MongShift;
import com.mongs.core.enums.management.MongState;
import com.mongs.core.vo.mqtt.*;
import com.mongs.management.domain.ateFood.entity.AteFoodHistory;
import com.mongs.management.domain.ateFood.repository.AteFoodHistoryRepository;
import com.mongs.management.domain.mong.controller.dto.response.*;
import com.mongs.management.domain.mong.entity.Mong;
import com.mongs.management.domain.mong.repository.FoodCodeRepository;
import com.mongs.management.domain.mong.service.moduleService.LifecycleService;
import com.mongs.management.domain.mong.service.moduleService.MongService;
import com.mongs.management.domain.mong.service.moduleService.NotificationService;
import com.mongs.management.domain.mong.utils.MongUtil;
import com.mongs.management.exception.ManagementErrorCode;
import com.mongs.management.exception.ManagementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementServiceImpl implements ManagementService {

    private final MongUtil mongUtil;
    private final FoodCodeRepository foodCodeRepository;
    private final AteFoodHistoryRepository ateFoodHistoryRepository;

    private final MongService mongService;
    private final LifecycleService lifecycleService;
    private final NotificationService notificationService;

    @Value("${application.management.training-point}")
    private Integer TRAINING_POINT;
    @Value("${application.management.training-strength}")
    private Double TRAINING_STRENGTH;

    private void checkEvolution(Mong mong) {
        if (mong.getExp() >= mong.getGrade().getNextGrade().getEvolutionExp() && !mong.getGrade().equals(MongGrade.ZERO)) {
            Mong saveMong = mongService.saveMong(mong.toBuilder()
                    .shift(MongShift.EVOLUTION_READY)
                    .build());

            notificationService.publishEvolutionReady(saveMong.getAccountId(), PublishEvolutionReadyVo.builder()
                    .mongId(mong.getId())
                    .shiftCode(mong.getShift().getCode())
                    .build());
        }
    }

//    @Override
//    public void checkAttendance(Long accountId) {
//        mongService.getAllMong(accountId).forEach(mong -> {
//
//        });
//    }

    @Override
    @Transactional
    public List<FindMongResDto> findAllMong(Long accountId) {
        List<Mong> mongList = mongService.getAllMong(accountId);
        return FindMongResDto.toList(mongList);
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
                .mongId(mong.getId())
                .name(mong.getName())
                .mongCode(mong.getMongCode())
                .weight(mong.getWeight())
                .strength(mong.getStrength())
                .satiety(mong.getSatiety())
                .health(mong.getHealthy())
                .sleep(mong.getSleep())
                .poopCount(mong.getNumberOfPoop())
                .stateCode(mong.getState().getCode())
                .shiftCode(mong.getShift().getCode())
                .payPoint(mong.getPayPoint())
                .born(mong.getCreatedAt())
                .build());

        return RegisterMongResDto.of(saveMong);
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
                .shift(MongShift.DIE)
                .state(MongState.EMPTY)
                .build());

        lifecycleService.deleteMongEvent(saveMong.getId())
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.FEIGN_CLIENT_FAIL));

        notificationService.publishDelete(saveMong.getAccountId(), PublishDeleteVo.builder()
                .mongId(mong.getId())
                .build());

        return DeleteMongResDto.builder()
                .mongId(saveMong.getId())
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
        int newExp = Math.min(mong.getExp() + MongEXP.STROKE.getExp(), mong.getGrade().getNextGrade().getEvolutionExp());

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .numberOfStroke(newNumberOfStroke)
                .exp(newExp)
                .build());

        notificationService.publishStroke(saveMong.getAccountId(), PublishStrokeVo.builder()
                .mongId(mong.getId())
                .exp(mong.getExp())
                .build());

        this.checkEvolution(saveMong);

        return StrokeMongResDto.builder()
                .mongId(saveMong.getId())
                .exp(saveMong.getExp())
                .build();
    }

    @Override
    @Transactional
    public FeedMongResDto feedMong(Long accountId, Long mongId, String feedCode) {
        Mong mong = mongService.getMong(mongId, accountId);

        FoodCode foodCode = foodCodeRepository.findByCode(feedCode)
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.NOT_FOUND_FOOD_CODE));

        double newWeight = mong.getWeight() + foodCode.addWeightValue();
        double newStrength = mong.getStrength() + foodCode.addStrengthValue();
        double newSatiety = mong.getSatiety() + foodCode.addSatietyValue();
        double newHealthy = mong.getHealthy() + foodCode.addHealthyValue();
        double newSleep = mong.getSleep() + foodCode.addSleepValue();

        int newExp = Math.min(mong.getExp() + MongEXP.EAT_THE_FOOD.getExp(), mong.getGrade().getNextGrade().getEvolutionExp());

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .weight(newWeight)
                .strength(newStrength)
                .satiety(newSatiety)
                .healthy(newHealthy)
                .sleep(newSleep)
                .exp(newExp)
                .build());

        ateFoodHistoryRepository.save(AteFoodHistory.builder()
                .mongId(saveMong.getId())
                .code(foodCode.code())
                .price(foodCode.price())
                .build());

        notificationService.publishFeed(saveMong.getAccountId(), PublishFeedVo.builder()
                .mongId(mong.getId())
                .weight(mong.getWeight())
                .strength(mong.getStrength())
                .satiety(mong.getSatiety())
                .health(mong.getHealthy())
                .sleep(mong.getSleep())
                .exp(mong.getExp())
                .build());

        this.checkEvolution(saveMong);

        return FeedMongResDto.builder()
                .mongId(saveMong.getId())
                .weight(saveMong.getWeight())
                .health(saveMong.getHealthy())
                .satiety(saveMong.getSatiety())
                .strength(saveMong.getStrength())
                .sleep(saveMong.getSleep())
                .exp(saveMong.getExp())
                .build();
    }

    @Override
    @Transactional
    public SleepMongResDto sleepingMong(Long accountId, Long mongId) {
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
                .mongId(mong.getId())
                .isSleeping(mong.getIsSleeping())
                .build());

        return SleepMongResDto.builder()
                .mongId(saveMong.getId())
                .isSleeping(saveMong.getIsSleeping())
                .build();
    }

    @Override
    @Transactional
    public PoopCleanResDto poopClean(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);

        int newExp = Math.min(mong.getExp() + MongEXP.CLEANING_POOP.getExp(), mong.getGrade().getNextGrade().getEvolutionExp());

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .numberOfPoop(0)
                .exp(newExp)
                .build());

        notificationService.publishPoop(saveMong.getAccountId(), PublishPoopVo.builder()
                .mongId(mong.getId())
                .poopCount(mong.getNumberOfPoop())
                .exp(mong.getExp())
                .build());

        this.checkEvolution(saveMong);

        return PoopCleanResDto.builder()
                .mongId(saveMong.getId())
                .poopCount(saveMong.getNumberOfPoop())
                .exp(saveMong.getExp())
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

        int newExp = Math.min(mong.getExp() + MongEXP.TRAINING.getExp(), mong.getGrade().getNextGrade().getEvolutionExp());

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .payPoint(newPayPoint)
                .numberOfTraining(newNumberOfTraining)
                .strength(newStrength)
                .exp(newExp)
                .build());

        notificationService.publishTraining(saveMong.getAccountId(), PublishTrainingVo.builder()
                .mongId(mong.getId())
                .payPoint(mong.getPayPoint())
                .strength(mong.getStrength())
                .exp(mong.getExp())
                .build());

        this.checkEvolution(saveMong);

        return TrainingMongResDto.of(saveMong);
    }

    @Override
    @Transactional
    public GraduateMongResDto graduateMong(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);

        MongGrade grade = mong.getGrade();
        if (!grade.equals(MongGrade.LAST)) {
            throw new ManagementException(ManagementErrorCode.INVALID_EVOLUTION);
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
                .mongId(mong.getId())
                .isActive(mong.getIsActive())
                .poopCount(mong.getNumberOfPoop())
                .health(mong.getHealthy())
                .satiety(mong.getSatiety())
                .sleep(mong.getSleep())
                .strength(mong.getStrength())
                .weight(mong.getWeight())
                .shiftCode(mong.getShift().getCode())
                .stateCode(mong.getState().getCode())
                .build());

        return GraduateMongResDto.builder()
                .mongId(saveMong.getId())
                .shiftCode(saveMong.getShift().getCode())
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

        int exp = mong.getExp();
        if (exp < grade.getNextGrade().getEvolutionExp()) {
            throw new ManagementException(ManagementErrorCode.NOT_ENOUGH_EXP);
        }

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .mongCode(mongUtil.getNextMongCode(mong))
                .shift(mongUtil.getNextShiftCode(mong))
                .state(mongUtil.getNextStateCode(mong))
                .grade(grade.getNextGrade())
                .exp(0)
                .build());

        if (saveMong.getGrade().equals(MongGrade.LAST)) {
            lifecycleService.graduateMongEvent(saveMong.getId())
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.FEIGN_CLIENT_FAIL));
        } else {
            lifecycleService.evolutionMongEvent(saveMong.getId())
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.FEIGN_CLIENT_FAIL));
        }

        notificationService.publishEvolution(saveMong.getAccountId(), PublishEvolutionVo.builder()
                .mongId(mong.getId())
                .mongCode(mong.getMongCode())
                .shiftCode(mong.getShift().getCode())
                .stateCode(mong.getState().getCode())
                .exp(mong.getExp())
                .build());

        return EvolutionMongResDto.builder()
                .mongId(saveMong.getId())
                .mongCode(saveMong.getMongCode())
                .shiftCode(saveMong.getShift().getCode())
                .stateCode(saveMong.getState().getCode())
                .exp(saveMong.getExp())
                .build();
    }
}
