package com.monglife.mongs.app.manager.management.listener;

import com.monglife.mongs.app.manager.management.service.ManagementService;
import com.monglife.mongs.domain.mong.dto.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class MongEventListener {

    private final ManagementService managementService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void mongCreateEventListener(MongCreateEvent event) {
        managementService.eggEvolutionScheduler(event.getMongId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void mongEggEvolutionEventListener(MongEggEvolutionEvent event) {
        managementService.cycleSleepScheduler(event.getMongId(), event.getSleepAt(), event.getWakeupAt());
        managementService.cycleDecreaseStatusScheduler(event.getMongId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void mongDeleteEventListener(MongDeleteEvent event) {
        managementService.stopAllScheduler(event.getMongId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void mongSleepEventListener(MongSleepEvent event) {
        managementService.cycleIncreaseStatusScheduler(event.getMongId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void mongWakeupEventListener(MongWakeupEvent event) {
        managementService.cycleDecreaseStatusScheduler(event.getMongId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void mongGraduateEventListener(MongGraduateEvent event) {
        managementService.stopAllScheduler(event.getMongId());
    }
}
