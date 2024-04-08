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
import com.mongs.management.domain.mong.service.componentService.vo.MongVo;
import com.mongs.management.domain.mong.service.event.vo.*;
import com.mongs.management.domain.mong.service.moduleService.*;
import com.mongs.management.exception.ManagementErrorCode;
import com.mongs.management.exception.ManagementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mongs.core.utils.MongStatusUtil.percentToStatus;
import static com.mongs.core.utils.MongStatusUtil.statusToPercent;
import static com.mongs.core.utils.MongUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementServiceImpl implements ManagementService {
    /* Repository */
    private final FoodCodeRepository foodCodeRepository;
    private final FeedHistoryRepository feedHistoryRepository;

    /* Module Service */
    private final MongService mongService;
    private final CollectionService collectionService;
    private final LifecycleService lifecycleService;
    private final NotificationService notificationService;
    private final MongHistoryService mongHistoryService;

    /* Spring Event Publisher */
    private final ApplicationEventPublisher publisher;

    /**
     * 계정 Id 에 해당하는 몽 리스트를 반환한다.
     *
     * @param accountId 계정 Id
     * @return {@link List<MongVo>}
     */
    @Override
    @Transactional
    public List<MongVo> findAllMong(Long accountId) {
        List<Mong> mongList = mongService.getAllMong(accountId);
        return MongVo.toList(mongList);
    }

    /**
     * 기초 정보를 바탕으로 몽을 생성한다.
     * 알 코드 값은 랜덤으로 부여한다.
     *
     * @param accountId 계정 Id
     * @param name 생성할 몽 이름
     * @param sleepStart 생성할 몽 장기 수면 시작 시각
     * @param sleepEnd 생성할 몽 장기 수면 종료 시각
     * @return {@link MongVo}
     */
    @Override
    @Transactional
    public MongVo registerMong(Long accountId, String name, String sleepStart, String sleepEnd) {
        /* 알 코드 부여 */
        String newMongCode = getNextMongCode();

        Mong saveMong = mongService.saveMong(Mong.builder()
                .accountId(accountId)
                .name(name)
                .sleepTime(sleepStart)
                .wakeUpTime(sleepEnd)
                .mongCode(newMongCode)
                .grade(MongGrade.ZERO)
                .build());

        /* 몽 컬렉션 등록 실패하는 경우 롤백 */
        collectionService.registerMongCollection(saveMong.getMongCode())
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.FEIGN_CLIENT_FAIL));

        /* 알 스케줄러 실행 실패하는 경우 롤백 */
        lifecycleService.eggMongEvent(saveMong.getId())
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.FEIGN_CLIENT_FAIL));

        /* 후처리 이벤트 호출 및 MongVo 반환 */
        MongVo mongVo = MongVo.of(saveMong);
        publisher.publishEvent(new RegisterMongEvent(mongVo));
        return mongVo;
    }

    /**
     * 계정 Id 와 몽 Id 로 몽을 삭제 한다.
     * 계정에 귀속된 몽인지 확인 후 삭제를 진행한다.
     * 삭제를 하게 되면 모든 디바이스로 알람을 보내어 모든 기기에서 삭제할 수 있도록 한다.
     *
     * @param accountId 계정 Id
     * @param mongId 몽 Id
     * @return {@link MongVo}
     */
    @Override
    @Transactional
    public MongVo deleteMong(Long accountId, Long mongId) {
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

        /* 스케줄러 중지에 실패하는 경우 롤백 */
        lifecycleService.deleteMongEvent(saveMong.getId())
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.FEIGN_CLIENT_FAIL));

        MongVo mongVo = MongVo.of(saveMong);
        publisher.publishEvent(new DeleteMongEvent(mongVo));
        return mongVo;
    }

    /**
     * 계정 Id 와 몽 Id 로 몽 쓰다듬기를 진행한다.
     * 레벨 0 (알) 인 경우에는 쓰다듬기가 불가능하다.
     * 쓰다듬기 횟수가 1 증가하고, {@link MongExp#STROKE} 의 exp 만큼 몽의 Exp 가 증가한다.
     * <p>
     * 진화 가능 여부를 체크한다.
     * <p>
     * 증가 요소 : exp
     *
     * @param accountId 계정 Id
     * @param mongId 몽 Id
     * @return {@link MongVo}
     */
    @Override
    @Transactional
    public MongVo strokeMong(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);
        
        /* 0 레벨 (알) 인 경우 쓰다듬기 불가능 */
        if (mong.getGrade().equals(MongGrade.ZERO)) {
            throw new ManagementException(ManagementErrorCode.INVALID_STROKE);
        }

        int newNumberOfStroke = mong.getNumberOfStroke() + 1;
        int newExp = Math.min(mong.getExp() + MongExp.STROKE.getExp(), mong.getGrade().getNextGrade().getEvolutionExp());

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .numberOfStroke(newNumberOfStroke)
                .exp(newExp)
                .build());

        /* 진화 가능 여부 체크 */
        publisher.publishEvent(new EvolutionCheckEvent(saveMong));

        MongVo mongVo = MongVo.of(saveMong);
        publisher.publishEvent(new StrokeMongEvent(mongVo));
        return mongVo;
    }

    /**
     * 계정 Id 와 몽 Id, 음식 코드로 몽 식사를 진행한다.
     * 음식에 해당하는 증가 값 {@link FoodCode} 에 따라 지수가 증가한다.
     * {@link MongExp#EAT_THE_FOOD} 의 exp 만큼 몽의 Exp 가 증가한다.
     * <p>
     * 진화 가능 여부를 체크한다.
     * 상태 변화 여부를 체크한다.
     * <p>
     * 증가 요소 : weight, strength, satiety, healthy, sleep, exp
     * @param accountId 계정 Id
     * @param mongId 몽 Id
     * @param foodCode 음식 코드
     * @return {@link MongVo}
     */
    @Override
    @Transactional
    public MongVo feedMong(Long accountId, Long mongId, String foodCode) {
        Mong mong = mongService.getMong(mongId, accountId);

        /* 음식 코드가 없는 경우 롤백 */
        FoodCode code = foodCodeRepository.findByCode(foodCode)
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.NOT_FOUND_FOOD_CODE));

        double newWeight = Math.min(mong.getWeight() + code.addWeightValue(), mong.getGrade().getMaxStatus());
        double newStrength = Math.min(mong.getStrength() + code.addStrengthValue(), mong.getGrade().getMaxStatus());
        double newSatiety = Math.min(mong.getSatiety() + code.addSatietyValue(), mong.getGrade().getMaxStatus());
        double newHealthy = Math.min(mong.getHealthy() + code.addHealthyValue(), mong.getGrade().getMaxStatus());
        double newSleep = Math.min(mong.getSleep() + code.addSleepValue(), mong.getGrade().getMaxStatus());

        int newExp = Math.min(mong.getExp() + MongExp.EAT_THE_FOOD.getExp(), mong.getGrade().getNextGrade().getEvolutionExp());

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .weight(newWeight)
                .strength(newStrength)
                .satiety(newSatiety)
                .healthy(newHealthy)
                .sleep(newSleep)
                .exp(newExp)
                .build());

        /* 식사 기록 저장 */
        feedHistoryRepository.save(FeedHistory.builder()
                .mongId(saveMong.getId())
                .code(code.code())
                .price(code.price())
                .build());

        /* 진화 가능 여부 확인 */
        publisher.publishEvent(new EvolutionCheckEvent(saveMong));
        /* 상태 변화 여부 확인 */
        publisher.publishEvent(new StateCheckEvent(saveMong));

        MongVo mongVo = MongVo.of(saveMong);
        publisher.publishEvent(new FeedMongEvent(mongVo));
        return mongVo;
    }

    @Override
    @Transactional
    public MongVo sleepingMong(Long accountId, Long mongId) {
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
    public MongVo poopClean(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);

        int newExp = Math.min(
                mong.getExp() + MongExp.CLEANING_POOP.getExp() * mong.getNumberOfPoop(),
                mong.getGrade().getNextGrade().getEvolutionExp()
        );

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .numberOfPoop(0)
                .exp(newExp)
                .build());

        notificationService.publishPoop(saveMong.getAccountId(), PublishPoopVo.builder()
                .mongId(saveMong.getId())
                .poopCount(saveMong.getNumberOfPoop())
                .exp(statusToPercent((double) saveMong.getExp(), saveMong.getGrade()))
                .build());

        mongHistoryService.saveMongHistory(saveMong.getId(), MongHistoryCode.POOP_CLEAN);

        publisher.publishEvent(new EvolutionCheckEvent(saveMong));

        return PoopCleanResDto.builder()
                .mongId(saveMong.getId())
                .poopCount(saveMong.getNumberOfPoop())
                .exp(statusToPercent((double) saveMong.getExp(), saveMong.getGrade()))
                .build();
    }

    @Override
    @Transactional
    public MongVo trainingMong(Long accountId, Long mongId, MongTraining mongTraining) {
        Mong mong = mongService.getMong(mongId, accountId);

        if(mong.getPayPoint() < mongTraining.getPoint()) {
            throw new ManagementException(ManagementErrorCode.NOT_ENOUGH_PAYPOINT);
        }

        int newPayPoint = mong.getPayPoint() - mongTraining.getPoint();
        int newNumberOfTraining = mong.getNumberOfTraining() + 1;

        double newWeight = Math.min(mong.getWeight() + mongTraining.getAddWeightValue(), mong.getGrade().getMaxStatus());
        double newStrength = Math.min(mong.getStrength() + mongTraining.getAddStrengthValue(), mong.getGrade().getMaxStatus());
        double newSatiety = Math.min(mong.getSatiety() + mongTraining.getAddSatietyValue(), mong.getGrade().getMaxStatus());
        double newHealthy = Math.min(mong.getHealthy() + mongTraining.getAddHealthyValue(), mong.getGrade().getMaxStatus());
        double newSleep = Math.min(mong.getSleep() + mongTraining.getAddSleepValue(), mong.getGrade().getMaxStatus());

        int newExp = Math.min(mong.getExp() + mongTraining.getExp(), mong.getGrade().getNextGrade().getEvolutionExp());

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .payPoint(newPayPoint)
                .numberOfTraining(newNumberOfTraining)
                .strength(newStrength)
                .exp(newExp)
                .build());

        notificationService.publishTraining(saveMong.getAccountId(), PublishTrainingVo.builder()
                .mongId(saveMong.getId())
                .payPoint(saveMong.getPayPoint())
                .strength(statusToPercent(saveMong.getStrength(), saveMong.getGrade()))
                .exp(statusToPercent((double) saveMong.getExp(), saveMong.getGrade()))
                .build());

        mongHistoryService.saveMongHistory(saveMong.getId(), MongHistoryCode.TRAINING);

        publisher.publishEvent(new EvolutionCheckEvent(saveMong));
        publisher.publishEvent(new StateCheckEvent(saveMong));

        return TrainingMongResDto.builder()
                .mongId(saveMong.getId())
                .payPoint(saveMong.getPayPoint())
                .strength(statusToPercent(saveMong.getStrength(), saveMong.getGrade()))
                .exp(statusToPercent((double) saveMong.getExp(), saveMong.getGrade()))
                .build();
    }

    @Override
    @Transactional
    public MongVo graduateMong(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);

        MongGrade grade = mong.getGrade();
        if (!grade.equals(MongGrade.LAST)) {
            throw new ManagementException(ManagementErrorCode.INVALID_GRADUATION);
        }

        MongShift nextShift = getNextShiftCode(mong.getGrade(), mong.getShift());
        MongState nextState = getNextStateCode(mong.getGrade(), mong.getState());

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .isActive(false)
                .numberOfPoop(0)
                .healthy(-1D)
                .satiety(-1D)
                .sleep(-1D)
                .strength(-1D)
                .weight(-1D)
                .shift(nextShift)
                .state(nextState)
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
    public MongVo evolutionMong(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);

        /* 자는 중인 경우 진화 불가 */
        Boolean isSleeping = mong.getIsSleeping();
        if (isSleeping) {
            throw new ManagementException(ManagementErrorCode.INVALID_EVOLUTION);
        }
        /* 진화 준비 상태가 아닌 경우 진화 불가 */
        MongShift shift = mong.getShift();
        if (!shift.equals(MongShift.EVOLUTION_READY)) {
            throw new ManagementException(ManagementErrorCode.INVALID_EVOLUTION);
        }
        /* 더이상 진화가 불가능한 레벨인 경우 진화 불가 */
        MongGrade grade = mong.getGrade();
        if (grade.equals(MongGrade.LAST)) {
            throw new ManagementException(ManagementErrorCode.INVALID_EVOLUTION);
        }
        /* 해당 레벨에 대한 Exp 를 채우지 못하면 진화 불가 */
        double exp = mong.getExp();
        if (exp < grade.getNextGrade().getEvolutionExp()) {
            throw new ManagementException(ManagementErrorCode.NOT_ENOUGH_EXP);
        }

        /* 비율 만큼 증가 */
        double weightPercent = statusToPercent(mong.getWeight(), mong.getGrade());
        double strengthPercent = statusToPercent(mong.getStrength(), mong.getGrade());
        double satietyPercent = statusToPercent(mong.getSatiety(), mong.getGrade());
        double healthyPercent = statusToPercent(mong.getHealthy(), mong.getGrade());
        double sleepPercent = statusToPercent(mong.getSleep(), mong.getGrade());

        double newWeight = percentToStatus(weightPercent, grade);
        double newStrength = percentToStatus(strengthPercent, grade);
        double newSatiety = percentToStatus(satietyPercent, grade);
        double newHealthy = percentToStatus(healthyPercent, grade);
        double newSleep = percentToStatus(sleepPercent, grade);

        String nextMongCode = getNextMongCode(mong.getGrade(), mong.getMongCode());
        MongShift nextShift = getNextShiftCode(mong.getGrade(), mong.getShift());
        MongState nextState = getNextStateCode(mong.getGrade(), mong.getState());

        Mong nextMong = mong.toBuilder()
                .mongCode(nextMongCode)
                .shift(nextShift)
                .state(nextState)
                .grade(grade.getNextGrade())
                .weight(newWeight)
                .strength(newStrength)
                .satiety(newSatiety)
                .healthy(newHealthy)
                .sleep(newSleep)
                .exp(0)
                .build();

        if (isLastGrade(nextMong)) {
            nextMong = nextMong.toBuilder().shift(MongShift.GRADUATE_READY).build();
        }

        /* 몽 변경 사항 반영 부분 */
        Mong saveMong = mongService.saveMong(nextMong);
        /* 몽 변경 로그 저장 부분 */
        mongHistoryService.saveMongHistory(saveMong.getId(), MongHistoryCode.EVOLUTION);
        /* 컬렉션 추가 부분 */
        collectionService.registerMongCollection(saveMong.getMongCode())
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.FEIGN_CLIENT_FAIL));

        /* 스케줄러 동작 부분 */
        if (isFirstGrade(saveMong)) {
            // 첫 번째 진화인 경우 스케줄러 시작
            lifecycleService.eggEvolutionMongEvent(saveMong.getId())
                    .orElseThrow(() -> new ManagementException(ManagementErrorCode.FEIGN_CLIENT_FAIL));
        } else if (isLastGrade(saveMong)) {
            // 마지막 진화인 경우 스케줄러 중지
            lifecycleService.graduateReadyMongEvent(saveMong.getId())
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.FEIGN_CLIENT_FAIL));
        }

        /* 알림 전송 부분 */
        if (isLastGrade(saveMong)) {
            // 마지막 진화인 경우 졸업 준비 알림 전송
            notificationService.publishGraduationReady(saveMong.getAccountId(), PublishGraduationReadyVo.builder()
                    .mongId(saveMong.getId())
                    .shiftCode(MongShift.GRADUATE_READY.getCode())
                    .build());
        } else {
            // 마지막 진화가 아닌 경우 변경 사항 알림 전송
            notificationService.publishEvolution(saveMong.getAccountId(), PublishEvolutionVo.builder()
                    .mongId(saveMong.getId())
                    .mongCode(saveMong.getMongCode())
                    .weight(statusToPercent(saveMong.getWeight(), saveMong.getGrade()))
                    .strength(statusToPercent(saveMong.getStrength(), saveMong.getGrade()))
                    .satiety(statusToPercent(saveMong.getSatiety(), saveMong.getGrade()))
                    .health(statusToPercent(saveMong.getHealthy(), saveMong.getGrade()))
                    .sleep(statusToPercent(saveMong.getSleep(), saveMong.getGrade()))
                    .exp(statusToPercent((double) saveMong.getExp(), saveMong.getGrade()))
                    .shiftCode(saveMong.getShift().getCode())
                    .stateCode(saveMong.getState().getCode())
                    .build());
        }

        return EvolutionMongResDto.builder()
                .mongId(saveMong.getId())
                .mongCode(saveMong.getMongCode())
                .shiftCode(saveMong.getShift().getCode())
                .stateCode(saveMong.getState().getCode())
                .weight(statusToPercent(saveMong.getWeight(), saveMong.getGrade()))
                .strength(statusToPercent(saveMong.getStrength(), saveMong.getGrade()))
                .satiety(statusToPercent(saveMong.getSatiety(), saveMong.getGrade()))
                .healthy(statusToPercent(saveMong.getHealthy(), saveMong.getGrade()))
                .sleep(statusToPercent(saveMong.getSleep(), saveMong.getGrade()))
                .exp(statusToPercent((double) saveMong.getExp(), saveMong.getGrade()))
                .build();
    }

    private boolean isFirstGrade(Mong mong) {
        return mong.getGrade().equals(MongGrade.FIRST);
    }

    private boolean isLastGrade(Mong mong) {
        return mong.getGrade().equals(MongGrade.LAST);
    }
}
