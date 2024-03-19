package com.mongs.management.domain.mong.service.componentService;

import com.mongs.core.entity.FoodCode;
import com.mongs.core.enums.management.MongEXP;
import com.mongs.core.enums.management.MongGrade;
import com.mongs.core.enums.management.MongShift;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private Integer TRAINING_PAIED_POINT = 5;
    private Double TRAINING_STRENGTH = 5D;

    private void checkEvolution(Mong mong) {
        if (mong.getExp() >= mong.getGrade().getNextGrade().getEvolutionExp() && !mong.getGrade().equals(MongGrade.ZERO)
        ) {
            Mong saveMong = mongService.saveMong(mong.toBuilder()
                    .shift(MongShift.EVOLUTION_READY)
                    .build());

            lifecycleService.evolutionReadyMongEvent(saveMong.getId());

            notificationService.publishEvolutionReady(saveMong);
        }
    }

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

        lifecycleService.eggMongEvent(saveMong.getId());

        notificationService.publishCreate(saveMong);

        return RegisterMongResDto.of(saveMong);
    }

    @Override
    @Transactional
    public DeleteMongResDto deleteMong(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .isActive(false)
                .build());

        lifecycleService.deadMongEvent(saveMong.getId());

        notificationService.publishDelete(saveMong);

        return DeleteMongResDto.builder()
                .mongId(saveMong.getId())
                .build();
    }

    @Override
    @Transactional
    public StrokeMongResDto strokeMong(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);

        int newNumberOfStroke = mong.getNumberOfStroke() + 1;
        int newExp = mong.getExp() + MongEXP.STROKE.getExp();

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .numberOfStroke(newNumberOfStroke)
                .exp(newExp)
                .build());

        notificationService.publishStroke(saveMong);

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

        int newExp = mong.getExp() + MongEXP.EAT_THE_FOOD.getExp();

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

        notificationService.publishFeed(saveMong);

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
    public SleepMongResDto sleepMong(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);

        boolean newIsSleeping = !mong.getIsSleeping();

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .isSleeping(newIsSleeping)
                .build());

        if (saveMong.getIsSleeping()) {
            lifecycleService.sleepMongEvent(saveMong.getId());
        } else {
            lifecycleService.wakeUpMongEvent(saveMong.getId());
        }

        notificationService.publishSleeping(saveMong);

        return SleepMongResDto.builder()
                .mongId(saveMong.getId())
                .isSleeping(saveMong.getIsSleeping())
                .build();
    }

    @Override
    @Transactional
    public PoopCleanResDto poopClean(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);

        int newExp = mong.getExp() + MongEXP.CLEANING_POOP.getExp();

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .numberOfPoop(0)
                .exp(newExp)
                .build());

        notificationService.publishPoop(saveMong);

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

        if(mong.getPayPoint() < TRAINING_PAIED_POINT) {
            throw new ManagementException(ManagementErrorCode.NOT_ENOUGH_PAYPOINT);
        }

        int newPayPoint = mong.getPayPoint() - TRAINING_PAIED_POINT;
        int newNumberOfTraining = mong.getNumberOfTraining() + 1;
        double newStrength = mong.getStrength() + TRAINING_STRENGTH;

        int newExp = mong.getExp() + MongEXP.TRAINING.getExp();

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .payPoint(newPayPoint)
                .numberOfTraining(newNumberOfTraining)
                .strength(newStrength)
                .exp(newExp)
                .build());

        notificationService.publishTraining(saveMong);

        this.checkEvolution(saveMong);

        return TrainingMongResDto.of(saveMong);
    }

    @Override
    @Transactional
    public GraduateMongResDto graduateMong(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);

        MongGrade grade = mong.getGrade();
        if (!grade.equals(MongGrade.GRADUATE)) {
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

        notificationService.publishGraduation(saveMong);

        this.checkEvolution(saveMong);

        return GraduateMongResDto.builder()
                .mongId(saveMong.getId())
                .shiftCode(saveMong.getShift().getCode())
                .build();
    }

    @Override
    @Transactional
    public EvolutionMongResDto evolutionMong(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);

        MongShift shift = mong.getShift();
        if (!shift.equals(MongShift.EVOLUTION_READY)) {
            throw new ManagementException(ManagementErrorCode.INVALID_EVOLUTION);
        }

        MongGrade grade = mong.getGrade();
        if (grade.equals(MongGrade.GRADUATE)) {
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

        if (saveMong.getGrade().equals(MongGrade.GRADUATE)) {
            lifecycleService.graduateMongEvent(saveMong.getId());
        } else {
            lifecycleService.evolutionMongEvent(saveMong.getId());
        }

        notificationService.publishEvolution(saveMong);

        return EvolutionMongResDto.builder()
                .mongId(saveMong.getId())
                .mongCode(saveMong.getMongCode())
                .shiftCode(saveMong.getShift().getCode())
                .stateCode(saveMong.getState().getCode())
                .exp(saveMong.getExp())
                .build();
    }
}
