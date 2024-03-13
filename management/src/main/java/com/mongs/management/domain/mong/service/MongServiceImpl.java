package com.mongs.management.domain.mong.service;

import com.mongs.core.entity.FoodCode;
import com.mongs.core.enums.management.MongEXP;
import com.mongs.core.enums.management.MongGrade;
import com.mongs.core.enums.management.MongShift;
import com.mongs.core.enums.management.MongState;
import com.mongs.core.mqtt.*;
import com.mongs.management.domain.ateFood.entity.AteFoodHistory;
import com.mongs.management.domain.ateFood.repository.AteFoodHistoryRepository;
import com.mongs.management.domain.mong.client.LifecycleClient;
import com.mongs.management.domain.mong.client.NotificationClient;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class MongServiceImpl implements MongService {

    private final MongUtil mongUtil;
    private final MongRepository mongRepository;
    private final AteFoodHistoryRepository ateFoodHistoryRepository;
    private final FoodCodeRepository foodCodeRepository;

    private final LifecycleClient lifecycleClient;
    private final NotificationClient notificationClient;

//    @Value("${value.traing_paied}")
    private Integer TRAINING_PAIED_POINT = 5;
    private Double TRAINING_STRENGTH = 5D;
    private Mong getMong(Long mongId, Long accountId) {
        return mongRepository.findByIdAndAccountId(mongId, accountId)
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.NOT_FOUND));
    }

    @Transactional
    public void eggMong(Long mongId) {
        lifecycleClient.eggMongEvent(mongId);
    }

    // 몽생성
    @Transactional
    @Override
    public CreateMong createMong(InitMong initMong, Long accountId, String email) {
        String sleepTimeStart = mongUtil.timeConverter(initMong.sleepStart());
        String sleepTimeEnd = mongUtil.timeConverter(initMong.sleepEnd());

        // 몽 이름, 잠 (기상, 취침) -> 이거에 따라서 자는지 안자는지 체크
        Mong mong = Mong.builder()
                .accountId(accountId)
                .name(initMong.name())
                .sleepTime(sleepTimeStart)
                .wakeUpTime(sleepTimeEnd)
                .build();

        String newMongCode = mongUtil.getNextMongCode(mong, MongGrade.ZERO);

        Mong saveMong = mongRepository.save(mong.toBuilder().mongCode(newMongCode).build());

        notificationClient.publishCreate(MqttReqDto.builder()
                .accountId(saveMong.getAccountId())
                .data(PublishCreate.builder()
                        .mongId(saveMong.getId())
                        .name(saveMong.getName())
                        .code(saveMong.getMongCode())
                        .weight(saveMong.getWeight())
                        .strength(saveMong.getStrength())
                        .satiety(saveMong.getSatiety())
                        .health(saveMong.getHealthy())
                        .sleep(saveMong.getSleep())
                        .poopCount(saveMong.getNumberOfPoop())
                        .stateCode(saveMong.getState().getCode())
                        .shiftCode(saveMong.getShift().getCode())
                        .payPoint(saveMong.getPayPoint())
                        .born(saveMong.getCreatedAt())
                        .build())
                .build());

        return CreateMong.of(saveMong);
    }

    // 몽 쓰다듬기 -> 경험치 상승, 몽 쓰다듬 횟수 + 1
    @Transactional
    @Override
    public Stroke toMongStroke(Long mongId, Long accountId) {
        Mong mong = getMong(mongId, accountId);

        int newNumberOfStroke = mong.getNumberOfStroke() + 1;
        int newExp = mong.getExp() + MongEXP.STROKE.getExp();

        Mong saveMong = mongRepository.save(mong.toBuilder()
                .numberOfStroke(newNumberOfStroke)
                .exp(newExp)
                .build());
        return Stroke.of(saveMong);
    }

    // 몽 낮잠 3시간 뒤에는 일어나야한다. -> 스케줄러
    @Transactional
    @Override
    public Sleep toMongSleeping(Long mongId, Long accountId, String email) {
        Mong mong = getMong(mongId, accountId);

        boolean newIsSleeping = !mong.getIsSleeping();

        // TODO("스케줄러 쪽에 수면 중인 경우 exp 증가 로직 추가 필요")

        Mong saveMong = mongRepository.save(mong.toBuilder()
                .isSleeping(newIsSleeping)
                .build());

        // 스케줄러 호출 [수면]
        if (saveMong.getIsSleeping()) {
            lifecycleClient.sleepMongEvent(mongId);
        } else {
            lifecycleClient.wakeupMongEvent(mongId);
        }

        return Sleep.of(saveMong);
    }
    @Transactional
    @Override
    public Poop toCleanMongsPoop(Long mongId, Long accountId, String email) {
        Mong mong = getMong(mongId, accountId);

        int newExp = mong.getExp() + MongEXP.CLEANING_POOP.getExp();

        Mong saveMong = mongRepository.save(mong.toBuilder()
                .numberOfPoop(0)
                .exp(newExp)
                .build());

        notificationClient.publishStatus(MqttReqDto.builder()
                .accountId(saveMong.getAccountId())
                .data(PublishStatus.builder()
                        .mongId(mongId)
                        .health(saveMong.getHealthy())
                        .satiety(saveMong.getSatiety())
                        .strength(saveMong.getStrength())
                        .sleep(saveMong.getSleep())
                        .poopCount(saveMong.getNumberOfPoop())
                        .isSleeping(saveMong.getIsSleeping())
                        .build())
                .build());

        return Poop.of(saveMong);
    }

    // 몽 먹이주기, 단백질 음식 먹으면 근력 향상 ( 코드에 있는 FD 뒤에 있는 숫자 / 5 해서 진행 )
    @Transactional
    @Override
    public EatTheFeed feedToMong(FeedCode code, Long mongId, Long accountId, String email) {
        Mong mong = getMong(mongId, accountId);

        FoodCode foodCode = foodCodeRepository.findByCode(code.feedCode())
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


        notificationClient.publishStatus(MqttReqDto.builder()
                .accountId(saveMong.getAccountId())
                .data(PublishStatus.builder()
                        .mongId(mongId)
                        .health(saveMong.getHealthy())
                        .satiety(saveMong.getSatiety())
                        .strength(saveMong.getStrength())
                        .sleep(saveMong.getSleep())
                        .poopCount(saveMong.getNumberOfPoop())
                        .isSleeping(saveMong.getIsSleeping())
                        .build())
                .build());

        return EatTheFeed.of(saveMong, foodCode);
    }

    // 몽 훈련 ( paypoint가 traingPaiedPoint (50) 보다 낮으면 훈련 불가 )
    // 포인트 감소, 훈련 횟수, 근력, 경험치 증가
    @Transactional
    @Override
    public Training mongTraining(Long mongId, Long accountId) {
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

    // 몽 진화 실행
    @Transactional
    @Override
    public Evolution mongEvolution(Long mongId, Long accountId, String email) {
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

        notificationClient.publishEvolution(MqttReqDto.builder()
                .accountId(saveMong.getAccountId())
                .data(PublishShift.builder()
                        .mongId(mongId)
                        .shiftCode(saveMong.getShift().getCode())
                        .build())
                .build());

        return Evolution.of(saveMong);
    }

    // 몽 졸업 ( 4단계 달성하면 졸업 ) 실행
    @Transactional
    @Override
    public Graduation mongsGraduate(Long mongId, Long accountId, String email) {
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

        notificationClient.publishStatus(MqttReqDto.builder()
                .accountId(saveMong.getAccountId())
                .data(PublishStatus.builder()
                        .mongId(mongId)
                        .health(saveMong.getHealthy())
                        .satiety(saveMong.getSatiety())
                        .strength(saveMong.getStrength())
                        .sleep(saveMong.getSleep())
                        .poopCount(saveMong.getNumberOfPoop())
                        .isSleeping(saveMong.getIsSleeping())
                        .build())
                .build());
        notificationClient.publishState(MqttReqDto.builder()
                .accountId(saveMong.getAccountId())
                .data(PublishState.builder()
                        .mongId(mongId)
                        .stateCode(saveMong.getState().getCode())
                        .build())
                .build());
        notificationClient.publishEvolution(MqttReqDto.builder()
                .accountId(saveMong.getAccountId())
                .data(PublishShift.builder()
                        .mongId(mongId)
                        .shiftCode(saveMong.getShift().getCode())
                        .build())
                .build());

        return Graduation.of(saveMong);
    }
}
