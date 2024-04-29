package com.mongs.management.domain.management.service;

import com.mongs.core.enums.management.*;
import com.mongs.core.enums.management.MongExp;
import com.mongs.core.enums.management.MongGrade;
import com.mongs.core.enums.management.MongState;
import com.mongs.management.domain.management.event.vo.*;
import com.mongs.management.global.event.vo.EvolutionCheckEvent;
import com.mongs.management.global.event.vo.StateCheckEvent;
import com.mongs.management.global.moduleService.CollectionService;
import com.mongs.management.global.moduleService.LifecycleService;
import com.mongs.management.global.moduleService.MongService;
import com.mongs.management.global.entity.Mong;
import com.mongs.management.domain.management.service.vo.MongVo;
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

    /* Module Service */
    private final MongService mongService;
    private final CollectionService collectionService;
    private final LifecycleService lifecycleService;

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
                .grade(MongGrade.LAST)
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
     * 계정 Id 와 몽 Id 로 수면/비수면 상태로의 변경을 진행한다.
     * 수면 상태인 경우 비수면 상태로 변경 한다.
     * 비수면 상태인 경우 수면 상태로 변경 한다.
     * 수면/비수면 상태로 변경할 때 스케줄러를 호출한다.
     *
     * @param accountId 계정 Id
     * @param mongId 몽 Id
     * @return {@link MongVo}
     */
    @Override
    @Transactional
    public MongVo sleepingMong(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);

        boolean newIsSleeping = !mong.getIsSleeping();

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .isSleeping(newIsSleeping)
                .build());

        /* 수면 여부에 따른 분기 */
        if (saveMong.getIsSleeping()) {
            /* 비수면 상태로 변환에 실패하면 롤백 */
            lifecycleService.sleepMongEvent(saveMong.getId())
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.FEIGN_CLIENT_FAIL));
        } else {
            /* 수면 상태로 변환에 실패하면 롤백 */
            lifecycleService.wakeUpMongEvent(saveMong.getId())
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.FEIGN_CLIENT_FAIL));
        }

        MongVo mongVo = MongVo.of(saveMong);
        publisher.publishEvent(new SleepingMongEvent(mongVo));
        return mongVo;
    }

    /**
     * 계정 Id 와 몽 Id 로 배변 처리를 진행한다.
     * 배변 갯수에 비례하여 exp 를 증가 시킨다.
     * 알 상태인 경우 배변 처리가 불가능하다.
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
    public MongVo poopClean(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);

        /* 알 상태인 경우 배변 처리 불가능 */
        if (mong.getGrade().equals(MongGrade.ZERO)) {
            throw new ManagementException(ManagementErrorCode.INVALID_POOP_CLEAN);
        }

        int newExp = Math.min(
                mong.getExp() + MongExp.CLEANING_POOP.getExp() * mong.getNumberOfPoop(),
                mong.getGrade().getNextGrade().getEvolutionExp()
        );

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .numberOfPoop(0)
                .exp(newExp)
                .build());

        /* 진화 가능 여부 확인 */
        publisher.publishEvent(new EvolutionCheckEvent(saveMong));

        MongVo mongVo = MongVo.of(saveMong);
        publisher.publishEvent(new PoopCleanEvent(mongVo));
        return mongVo;
    }

    /**
     * 계정 Id 와 몽 Id 로 훈련을 진행한다.
     * 훈련 종류에 따른 필요 포인트가 존재하는데, 포인트가 없으면 훈련을 할 수 없다.
     * 훈련을 하면, 훈련 횟수가 증가한다.
     * 훈련 종류에 따른 몽의 지수가 증가한다.
     * <p>
     * 진화 가능 여부를 체크한다.
     * 상태 변화 여부를 체크한다.
     * <p>
     * 증가 요소 : strength, exp
     *
     * @param accountId 계정 Id
     * @param mongId 몽 Id
     * @param mongTraining 훈련 종류
     * @return {@link MongVo}
     */
    @Override
    @Transactional
    public MongVo trainingMong(Long accountId, Long mongId, MongTraining mongTraining) {
        Mong mong = mongService.getMong(mongId, accountId);

        /* 훈련에 따른 소모 포인트 여부 확인 */
        if(mong.getPayPoint() < mongTraining.getPoint()) {
            throw new ManagementException(ManagementErrorCode.NOT_ENOUGH_PAYPOINT);
        }

        /* 훈련 종류에 따른 포인트 소모 */
        int newPayPoint = mong.getPayPoint() - mongTraining.getPoint();
        /* 훈련 횟수 증가 */
        int newNumberOfTraining = mong.getNumberOfTraining() + 1;

        /* 훈련 종류에 따른 지수 증가 */
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

        /* 진화 가능 여부 확인 */
        publisher.publishEvent(new EvolutionCheckEvent(saveMong));
        /* 상태 변화 여부 확인 */
        publisher.publishEvent(new StateCheckEvent(saveMong));

        MongVo mongVo = MongVo.of(saveMong);
        publisher.publishEvent(new TrainingMongEvent(mongVo));
        return mongVo;
    }

    /**
     * 계정 Id 와 몽 Id 로 졸업을 진행한다.
     * 마지막 레벨인 경우에만 졸업이 가능하다.
     * 졸업 준비 상태가 아니면 졸업이 불가능하다.
     *
     * @param accountId 계정 Id
     * @param mongId 몽 Id
     * @return {@link MongVo}
     */
    @Override
    @Transactional
    public MongVo graduateMong(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);

        /* 마지막 레벨이 아니면 졸업 불가능 */
        if (!mong.getGrade().equals(MongGrade.LAST)) {
            throw new ManagementException(ManagementErrorCode.INVALID_GRADUATION);
        }
        /* 졸업 준비 상태가 아니면 졸업 불가능 */
        if (!mong.getShift().equals(MongShift.GRADUATE_READY)) {
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

        MongVo mongVo = MongVo.of(saveMong);
        publisher.publishEvent(new GraduateMongEvent(mongVo));
        return mongVo;
    }
    /**
     * 계정 Id 와 몽 Id 로 진화를 진행한다.
     * 마지막 레벨인 경우 진화가 불가능하다.
     * 진화 준비 상태가 아닌 경우 진화가 불가능하다.
     * 자는 상태인 경우 진화가 불가능하다.
     * 해당 레벨에 대한 필요 Exp 를 채우지 못하면 진화가 불가능하다.
     * 진화 전 지수 퍼센트 값을 다음 레벨의 지수 값으로 환산하여 저장한다.
     * 진화하는 캐릭터에 대해 몽 컬렉션을 등록한다.
     * 알에서 1단계로 진화하는 경우 지속적으로 지수 감소하는 스케줄러를 실행 시킨다.
     * 졸업 준비 단계로 진화하는 경우 해당 몽에 대한 모든 스케줄러를 중지 시킨다.
     *
     * @param accountId 계정 Id
     * @param mongId 몽 Id
     * @return {@link MongVo}
     */
    @Override
    @Transactional
    public MongVo evolutionMong(Long accountId, Long mongId) {
        Mong mong = mongService.getMong(mongId, accountId);

        /* 더이상 진화가 불가능한 레벨인 경우 진화 불가 */
        if (mong.getGrade().equals(MongGrade.LAST)) {
            throw new ManagementException(ManagementErrorCode.INVALID_EVOLUTION);
        }
        /* 진화 준비 상태가 아닌 경우 진화 불가 */
        if (!mong.getShift().equals(MongShift.EVOLUTION_READY)) {
            throw new ManagementException(ManagementErrorCode.INVALID_EVOLUTION);
        }
        /* 자는 중인 경우 진화 불가 */
        if (mong.getIsSleeping()) {
            throw new ManagementException(ManagementErrorCode.INVALID_EVOLUTION);
        }
        /* 해당 레벨에 대한 Exp 를 채우지 못하면 진화 불가 */
        if (mong.getExp() < mong.getGrade().getNextGrade().getEvolutionExp()) {
            throw new ManagementException(ManagementErrorCode.NOT_ENOUGH_EXP);
        }

        /* 비율 만큼 증가 */
        double weightPercent = statusToPercent(mong.getWeight(), mong.getGrade());
        double strengthPercent = statusToPercent(mong.getStrength(), mong.getGrade());
        double satietyPercent = statusToPercent(mong.getSatiety(), mong.getGrade());
        double healthyPercent = statusToPercent(mong.getHealthy(), mong.getGrade());
        double sleepPercent = statusToPercent(mong.getSleep(), mong.getGrade());

        double newWeight = percentToStatus(weightPercent, mong.getGrade());
        double newStrength = percentToStatus(strengthPercent, mong.getGrade());
        double newSatiety = percentToStatus(satietyPercent, mong.getGrade());
        double newHealthy = percentToStatus(healthyPercent, mong.getGrade());
        double newSleep = percentToStatus(sleepPercent, mong.getGrade());

        String nextMongCode = getNextMongCode(mong.getGrade(), mong.getMongCode());
        MongShift nextShift = getNextShiftCode(mong.getGrade(), mong.getShift());
        MongState nextState = getNextStateCode(mong.getGrade(), mong.getState());

        /* 몽 변경 사항 반영 부분 */
        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .mongCode(nextMongCode)
                .shift(nextShift)
                .state(nextState)
                .grade(mong.getGrade().getNextGrade())
                .weight(newWeight)
                .strength(newStrength)
                .satiety(newSatiety)
                .healthy(newHealthy)
                .sleep(newSleep)
                .exp(0)
                .build());

        /* 컬렉션 추가 부분 */
        collectionService.registerMongCollection(saveMong.getMongCode())
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.FEIGN_CLIENT_FAIL));

        /* 스케줄러 동작 부분 */
        if (isFirstGrade(saveMong.getGrade())) {
            /* 첫 번째 진화인 경우 스케줄러 시작 */
            lifecycleService.eggEvolutionMongEvent(saveMong.getId())
                    .orElseThrow(() -> new ManagementException(ManagementErrorCode.FEIGN_CLIENT_FAIL));
        } else if (isLastGrade(saveMong.getGrade())) {
            /* 마지막 진화인 경우 모든 스케줄러 중지 */
            lifecycleService.graduateReadyMongEvent(saveMong.getId())
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.FEIGN_CLIENT_FAIL));
        }

        MongVo mongVo = MongVo.of(saveMong);
        publisher.publishEvent(new EvolutionMongEvent(mongVo));
        return mongVo;
    }
}
