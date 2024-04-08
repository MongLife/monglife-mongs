package com.mongs.management.domain.mong.service.event;

import com.mongs.core.enums.management.MongHistoryCode;
import com.mongs.core.enums.management.MongShift;
import com.mongs.core.enums.management.MongState;
import com.mongs.management.domain.mong.entity.Mong;
import com.mongs.management.domain.mong.service.componentService.ManagementService;
import com.mongs.management.domain.mong.service.componentService.vo.MongVo;
import com.mongs.management.domain.mong.service.event.vo.*;
import com.mongs.management.domain.mong.service.moduleService.MongHistoryService;
import com.mongs.management.domain.mong.service.moduleService.MongService;
import com.mongs.management.domain.mong.service.moduleService.NotificationService;
import com.mongs.management.domain.mong.service.moduleService.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.mongs.core.utils.MongUtil.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagementServiceEventListener {
    private final MongService mongService;
    private final MongHistoryService mongHistoryService;
    private final NotificationService notificationService;

    /**
     * 진화 가능 여부 체크
     * 진화가 가능한지 여부를 확인하여 진화 준비 상태로 변경한다.
     *
     * @param event 진화 체크 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void evolutionCheckEventListener(EvolutionCheckEvent event) {
        Mong mong = event.mong();

        log.info("[{}] evolutionCheckEventListener", mong.getId());

        if (isEvolutionReady(mong.getExp(), mong.getGrade())) {
            Mong saveMong = mongService.saveMong(mong.toBuilder()
                    .shift(MongShift.EVOLUTION_READY)
                    .build());

            MongVo mongVo = MongVo.of(saveMong);

            notificationService.publishEvolutionReady(saveMong.getAccountId(), PublishEvolutionReadyVo.of(mongVo));
        }
    }

    /**
     * 상태 변경 여부 체크
     * 몽 지수를 확인하여 상태를 변경시킨다.
     *
     * @param event 상태 체크 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void stateCheckEventListener(StateCheckEvent event) {
        Mong mong = event.mong();

        log.info("[{}] stateCheckEventListener", mong.getId());

        MongState nextState = getNextState(
                mong.getState(),
                mong.getGrade(),
                mong.getWeight(),
                mong.getStrength(),
                mong.getSatiety(),
                mong.getHealthy(),
                mong.getSleep()
        );

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .state(nextState)
                .build());

        MongVo mongVo = MongVo.of(saveMong);

        notificationService.publishState(saveMong.getAccountId(), PublishStateVo.of(mongVo));
    }

    /**
     * 
     * @param event 몽 생성 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registerMongEventListener(RegisterMongEvent event) {
        MongVo mongVo = event.mongVo();

        log.info("[{}] registerMongEventListener", mongVo.mongId());

        /* 몽 생성 알림 전송 */
        notificationService.publishCreate(mongVo.accountId(), PublishCreateVo.of(mongVo));

        mongHistoryService.saveMongHistory(mongVo.mongId(), MongHistoryCode.CREATE);
    }

    /**
     * 
     * @param event 몽 삭제 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteMongEventListener(DeleteMongEvent event) {
        MongVo mongVo = event.mongVo();

        log.info("[{}] deleteMongEventListener", mongVo.mongId());

        notificationService.publishDelete(mongVo.accountId(), PublishDeleteVo.of(mongVo));

        mongHistoryService.saveMongHistory(mongVo.mongId(), MongHistoryCode.DELETE);

    }

    /**
     * 
     * @param event 몽 쓰다듬기 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void strokeMongEventListener(StrokeMongEvent event) {
        MongVo mongVo = event.mongVo();

        log.info("[{}] strokeMongEventListener", mongVo.mongId());

        notificationService.publishStroke(mongVo.accountId(), PublishStrokeVo.of(mongVo));

        mongHistoryService.saveMongHistory(mongVo.mongId(), MongHistoryCode.STROKE);
    }

    /**
     *
     * @param event 몽 식사 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void feedMongEventListener(FeedMongEvent event) {
        MongVo mongVo = event.mongVo();

        log.info("[{}] feedMongEventListener", mongVo.mongId());

        notificationService.publishFeed(mongVo.accountId(), PublishFeedVo.of(mongVo));

        mongHistoryService.saveMongHistory(mongVo.mongId(), MongHistoryCode.FEED);
    }

    /**
     *
     * @param event 몽 수면 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sleepingMongEventListener(SleepingMongEvent event) {
        MongVo mongVo = event.mongVo();

        log.info("[{}] sleepingMongEventListener", mongVo.mongId());

        notificationService.publishSleeping(mongVo.accountId(), PublishSleepingVo.of(mongVo));

        mongHistoryService.saveMongHistory(mongVo.mongId(), MongHistoryCode.SLEEP);
    }

    /**
     *
     * @param event 몽 배변 처리 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void poopCleanEventListener(PoopCleanEvent event) {
        MongVo mongVo = event.mongVo();

        log.info("[{}] poopCleanEventListener", mongVo.mongId());

        notificationService.publishPoop(mongVo.accountId(), PublishPoopVo.of(mongVo));

        mongHistoryService.saveMongHistory(mongVo.mongId(), MongHistoryCode.POOP_CLEAN);
    }

    /**
     *
     * @param event 몽 훈련 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void trainingMongEventListener(TrainingMongEvent event) {
        MongVo mongVo = event.mongVo();

        log.info("[{}] trainingMongEventListener", mongVo.mongId());

        notificationService.publishTraining(mongVo.accountId(), PublishTrainingVo.of(mongVo));

        mongHistoryService.saveMongHistory(mongVo.mongId(), MongHistoryCode.TRAINING);

    }

    /**
     *
     * @param event 몽 졸업 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void graduateMongEventListener(GraduateMongEvent event) {
        MongVo mongVo = event.mongVo();

        log.info("[{}] graduateMongEventListener", mongVo.mongId());

        notificationService.publishGraduation(mongVo.accountId(), PublishGraduationVo.of(mongVo));

        mongHistoryService.saveMongHistory(mongVo.mongId(), MongHistoryCode.GRADUATION);

    }

    /**
     *
     * @param event 몽 진화 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void evolutionMongEventListener(EvolutionMongEvent event) {
        MongVo mongVo = event.mongVo();

        log.info("[{}] evolutionMongEventListener", mongVo.mongId());

        /* 알림 전송 부분 */
        if (isLastGrade(mongVo.grade())) {
            // 마지막 진화인 경우 졸업 준비 알림 전송
            notificationService.publishGraduationReady(mongVo.accountId(), PublishGraduationReadyVo.of(mongVo));
        } else {
            // 마지막 진화가 아닌 경우 변경 사항 알림 전송
            notificationService.publishEvolution(mongVo.accountId(), PublishEvolutionVo.of(mongVo));
        }

        /* 몽 변경 로그 저장 부분 */
        mongHistoryService.saveMongHistory(mongVo.mongId(), MongHistoryCode.EVOLUTION);
    }
}
