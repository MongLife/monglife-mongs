package com.mongs.management.domain.management.event;

import com.mongs.core.enums.management.MongHistoryCode;
import com.mongs.management.domain.management.event.vo.*;
import com.mongs.management.domain.management.service.vo.MongVo;
import com.mongs.management.global.moduleService.MongHistoryService;
import com.mongs.management.global.moduleService.NotificationService;
import com.mongs.management.global.moduleService.vo.*;
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
    private final MongHistoryService mongHistoryService;
    private final NotificationService notificationService;

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
