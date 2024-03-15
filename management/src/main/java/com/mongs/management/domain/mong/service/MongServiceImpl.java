package com.mongs.management.domain.mong.service;

import com.mongs.core.entity.FoodCode;
import com.mongs.core.enums.management.MongEXP;
import com.mongs.core.enums.management.MongGrade;
import com.mongs.core.enums.management.MongShift;
import com.mongs.core.enums.management.MongState;
import com.mongs.core.vo.mqtt.*;
import com.mongs.core.vo.mqtt.PublishCreateVo;
import com.mongs.management.domain.ateFood.entity.AteFoodHistory;
import com.mongs.management.domain.ateFood.repository.AteFoodHistoryRepository;
import com.mongs.management.domain.mong.client.NotificationClient;
import com.mongs.management.domain.mong.controller.dto.response.*;
import com.mongs.management.domain.mong.entity.Mong;
import com.mongs.management.domain.mong.repository.FoodCodeRepository;
import com.mongs.management.domain.mong.repository.MongRepository;
import com.mongs.management.domain.mong.service.dto.*;
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
public class MongServiceImpl implements MongService {

    private final MongUtil mongUtil;
    private final MongRepository mongRepository;
    private final AteFoodHistoryRepository ateFoodHistoryRepository;
    private final FoodCodeRepository foodCodeRepository;

    private Integer TRAINING_PAIED_POINT = 5;
    private Double TRAINING_STRENGTH = 5D;

    private Mong getMong(Long mongId, Long accountId) {
        return mongRepository.findByIdAndAccountIdAndIsActiveTrue(mongId, accountId)
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.NOT_FOUND));
    }

    @Override
    @Transactional
    public List<FindMongResDto> findAllMong(Long accountId) {

        return null;
    }

    @Override
    @Transactional
    public RegisterMongResDto registerMong(Long accountId, String name, String sleepStart, String sleepEnd) {
        Mong mong = Mong.builder()
                .accountId(accountId)
                .name(name)
                .sleepTime(sleepStart)
                .wakeUpTime(sleepEnd)
                .build();

        String newMongCode = mongUtil.getNextMongCode(mong, MongGrade.ZERO);

        Mong saveMong = mongRepository.save(mong.toBuilder().mongCode(newMongCode).build());

//        notificationClient.publishCreate(MqttReqDto.builder()
//                .accountId(saveMong.getAccountId())
//                .data(PublishCreateVo.builder()
//                        .mongId(saveMong.getId())
//                        .name(saveMong.getName())
//                        .code(saveMong.getMongCode())
//                        .weight(saveMong.getWeight())
//                        .strength(saveMong.getStrength())
//                        .satiety(saveMong.getSatiety())
//                        .health(saveMong.getHealthy())
//                        .sleep(saveMong.getSleep())
//                        .poopCount(saveMong.getNumberOfPoop())
//                        .stateCode(saveMong.getState().getCode())
//                        .shiftCode(saveMong.getShift().getCode())
//                        .payPoint(saveMong.getPayPoint())
//                        .born(saveMong.getCreatedAt())
//                        .build())
//                .build());

        return CreateMong.of(saveMong);
    }

    @Transactional
    @Override
    public DeleteMongResDto deleteMong(Long accountId, Long mongId) {

        return null;
    }

    @Transactional
    @Override
    public StrokeMongResDto strokeMong(Long accountId, Long mongId) {
        Mong mong = getMong(mongId, accountId);

        int newNumberOfStroke = mong.getNumberOfStroke() + 1;
        int newExp = mong.getExp() + MongEXP.STROKE.getExp();

        Mong saveMong = mongRepository.save(mong.toBuilder()
                .numberOfStroke(newNumberOfStroke)
                .exp(newExp)
                .build());
        return Stroke.of(saveMong);
    }

    @Transactional
    @Override
    public FeedMongResDto feedMong(Long accountId, Long mongId, String feedCode) {
        Mong mong = getMong(mongId, accountId);

        FoodCode foodCode = foodCodeRepository.findByCode(feedCode)
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.NOT_FOUND_FOOD_CODE));

        double newWeight = mong.getWeight() + foodCode.addWeightValue();
        double newStrength = mong.getStrength() + foodCode.addStrengthValue();
        double newSatiety = mong.getSatiety() + foodCode.addSatietyValue();
        double newHealthy = mong.getHealthy() + foodCode.addHealthyValue();
        double newSleep = mong.getSleep() + foodCode.addSleepValue();

        int newExp = mong.getExp() + MongEXP.EAT_THE_FOOD.getExp();

        Mong saveMong = mongRepository.save(mong.toBuilder()
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


//        notificationClient.publishStatus(MqttReqDto.builder()
//                .accountId(saveMong.getAccountId())
//                .data(PublishStatusVo.builder()
//                        .mongId(mongId)
//                        .health(saveMong.getHealthy())
//                        .satiety(saveMong.getSatiety())
//                        .strength(saveMong.getStrength())
//                        .sleep(saveMong.getSleep())
//                        .poopCount(saveMong.getNumberOfPoop())
//                        .isSleeping(saveMong.getIsSleeping())
//                        .build())
//                .build());

        return EatTheFeed.of(saveMong, foodCode);
    }

    @Transactional
    @Override
    public SleepMongResDto sleepMong(Long accountId, Long mongId) {
        Mong mong = getMong(mongId, accountId);

        boolean newIsSleeping = !mong.getIsSleeping();

        // TODO("스케줄러 쪽에 수면 중인 경우 exp 증가 로직 추가 필요")

        Mong saveMong = mongRepository.save(mong.toBuilder()
                .isSleeping(newIsSleeping)
                .build());

//        // 스케줄러 호출 [수면]
//        if (saveMong.getIsSleeping()) {
//            lifecycleClient.sleepMongEvent(mongId);
//        } else {
//            lifecycleClient.wakeupMongEvent(mongId);
//        }

        return Sleep.of(saveMong);
    }

    @Transactional
    @Override
    public PoopCleanResDto poopClean(Long accountId, Long mongId) {
        Mong mong = getMong(mongId, accountId);

        int newExp = mong.getExp() + MongEXP.CLEANING_POOP.getExp();

        Mong saveMong = mongRepository.save(mong.toBuilder()
                .numberOfPoop(0)
                .exp(newExp)
                .build());

//        notificationClient.publishStatus(MqttReqDto.builder()
//                .accountId(saveMong.getAccountId())
//                .data(PublishStatusVo.builder()
//                        .mongId(mongId)
//                        .health(saveMong.getHealthy())
//                        .satiety(saveMong.getSatiety())
//                        .strength(saveMong.getStrength())
//                        .sleep(saveMong.getSleep())
//                        .poopCount(saveMong.getNumberOfPoop())
//                        .isSleeping(saveMong.getIsSleeping())
//                        .build())
//                .build());

        return Poop.of(saveMong);
    }

    @Transactional
    @Override
    public TrainingMongResDto trainingMong(Long accountId, Long mongId) {
        Mong mong = getMong(mongId, accountId);

        if(mong.getPayPoint() <= TRAINING_PAIED_POINT) {
            throw new ManagementException(ManagementErrorCode.NOT_ENOUGH_PAYPOINT);
        }

        int newPayPoint = mong.getPayPoint() - TRAINING_PAIED_POINT;
        int newNumberOfTraining = mong.getNumberOfTraining() + 1;
        double newStrength = mong.getStrength() + TRAINING_STRENGTH;

        int newExp = mong.getExp() + MongEXP.TRAINING.getExp();

        Mong saveMong = mongRepository.save(mong.toBuilder()
                .payPoint(newPayPoint)
                .numberOfTraining(newNumberOfTraining)
                .strength(newStrength)
                .exp(newExp)
                .build());
        return Training.of(saveMong);
      
    }

    @Transactional
    @Override
    public GraduateMongResDto graduateMong(Long accountId, Long mongId) {
        Mong mong = getMong(mongId, accountId);

        MongGrade grade = mong.getGrade();
        int exp = mong.getExp();
        if (exp < grade.getEvolutionExp()) {
            throw new ManagementException(ManagementErrorCode.NOT_ENOUGH_EXP);
        }

        Mong saveMong = mongRepository.save(mong.toBuilder()
//                .isActive(false)
                .numberOfPoop(0)
                .healthy(-1D)
                .satiety(-1D)
                .sleep(-1D)
                .strength(-1D)
//                .weight(-1D)
                .state(MongState.NORMAL)
                .shift(MongShift.NORMAL)
                .build());

//        notificationClient.publishStatus(MqttReqDto.builder()
//                .accountId(saveMong.getAccountId())
//                .data(PublishStatusVo.builder()
//                        .mongId(mongId)
//                        .health(saveMong.getHealthy())
//                        .satiety(saveMong.getSatiety())
//                        .strength(saveMong.getStrength())
//                        .sleep(saveMong.getSleep())
//                        .poopCount(saveMong.getNumberOfPoop())
//                        .isSleeping(saveMong.getIsSleeping())
//                        .build())
//                .build());
//        notificationClient.publishState(MqttReqDto.builder()
//                .accountId(saveMong.getAccountId())
//                .data(PublishStateVo.builder()
//                        .mongId(mongId)
//                        .stateCode(saveMong.getState().getCode())
//                        .build())
//                .build());
//        notificationClient.publishEvolution(MqttReqDto.builder()
//                .accountId(saveMong.getAccountId())
//                .data(PublishShiftVo.builder()
//                        .mongId(mongId)
//                        .shiftCode(saveMong.getShift().getCode())
//                        .build())
//                .build());

        return Graduation.of(saveMong);
    }

    @Transactional
    @Override
    public EvolutionMongResDto evolutionMong(Long accountId, Long mongId) {
        Mong mong = getMong(mongId, accountId);

        MongGrade grade = mong.getGrade();
        if (grade.equals(MongGrade.FOURTH)) {
            throw new ManagementException(ManagementErrorCode.IMPOSSIBLE);
        }

        int exp = mong.getExp();
        if (exp < grade.getEvolutionExp()) {
            throw new ManagementException(ManagementErrorCode.NOT_ENOUGH_EXP);
        }

        Mong saveMong = mongRepository.save(mong.toBuilder()
                .mongCode(mongUtil.getNextMongCode(mong, grade))
                .build());

//        notificationClient.publishEvolution(MqttReqDto.builder()
//                .accountId(saveMong.getAccountId())
//                .data(PublishShiftVo.builder()
//                        .mongId(mongId)
//                        .shiftCode(saveMong.getShift().getCode())
//                        .build())
//                .build());

        return Evolution.of(saveMong);
    }
}
