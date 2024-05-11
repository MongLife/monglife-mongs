package com.mongs.play.app.management.internal.consumer;

import com.mongs.play.app.management.internal.service.ManagementInternalService;
import com.mongs.play.module.kafka.event.managementInternal.*;
import com.mongs.play.module.kafka.event.managementWorker.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagementInternalRollbackConsumer {

    private final ManagementInternalService managementInternalService;

    @KafkaListener(topics = { "rollback.zeroEvolutionMong" })
    public void zeroEvolutionMong(EvolutionReadyMongScheduleEvent payload) {}

    @KafkaListener(topics = { "rollback.decreaseWeight" })
    public void decreaseWeight(DecreaseWeightEvent payload) {
        Long mongId = payload.getMongId();
        Double subWeight = payload.getSubWeight();
        managementInternalService.increaseWeight(mongId, subWeight);
    }

    @KafkaListener(topics = { "rollback.decreaseStrength" })
    public void decreaseStrength(DecreaseStrengthEvent payload) {
        Long mongId = payload.getMongId();
        Double subStrength = payload.getSubStrength();
        managementInternalService.increaseStrength(mongId, subStrength);
    }

    @KafkaListener(topics = { "rollback.decreaseSatiety" })
    public void decreaseSatiety(DecreaseSatietyEvent payload) {
        Long mongId = payload.getMongId();
        Double subSatiety = payload.getSubSatiety();
        managementInternalService.increaseSatiety(mongId, subSatiety);
    }

    @KafkaListener(topics = { "rollback.decreaseHealthy" })
    public void decreaseHealthy(DecreaseHealthyEvent payload) {
        Long mongId = payload.getMongId();
        Double subHealthy = payload.getSubHealthy();
        managementInternalService.increaseHealthy(mongId, subHealthy);
    }

    @KafkaListener(topics = { "rollback.decreaseSleep" })
    public void decreaseSleep(DecreaseSleepEvent payload) {
        Long mongId = payload.getMongId();
        Double subSleep = payload.getSubSleep();
        managementInternalService.increaseSleep(mongId, subSleep);
    }

    @KafkaListener(topics = { "rollback.decreasePoopCount" })
    public void decreasePoopCount(DecreasePoopCountEvent payload) {
        Long mongId = payload.getMongId();
        Integer subPoopCount = payload.getSubPoopCount();
        managementInternalService.increasePoopCount(mongId, subPoopCount);
    }

    @KafkaListener(topics = { "rollback.increaseWeight" })
    public void decreaseWeight(IncreaseWeightEvent payload) {
        Long mongId = payload.getMongId();
        Double addWeight = payload.getAddWeight();
        managementInternalService.decreaseWeight(mongId, addWeight);
    }

    @KafkaListener(topics = { "rollback.increaseStrength" })
    public void decreaseStrength(IncreaseStrengthEvent payload) {
        Long mongId = payload.getMongId();
        Double addStrength = payload.getAddStrength();
        managementInternalService.decreaseStrength(mongId, addStrength);
    }

    @KafkaListener(topics = { "rollback.increaseSatiety" })
    public void decreaseSatiety(IncreaseSatietyEvent payload) {
        Long mongId = payload.getMongId();
        Double addSatiety = payload.getAddSatiety();
        managementInternalService.decreaseSatiety(mongId, addSatiety);
    }

    @KafkaListener(topics = { "rollback.increaseHealthy" })
    public void decreaseHealthy(IncreaseHealthyEvent payload) {
        Long mongId = payload.getMongId();
        Double addHealthy = payload.getAddHealthy();
        managementInternalService.decreaseHealthy(mongId, addHealthy);
    }

    @KafkaListener(topics = { "rollback.increaseSleep" })
    public void increaseSleep(IncreaseSleepEvent payload) {
        Long mongId = payload.getMongId();
        Double addSleep = payload.getAddSleep();
        managementInternalService.decreaseSleep(mongId, addSleep);
    }

    @KafkaListener(topics = { "rollback.increasePoopCount" })
    public void increasePoopCount(IncreasePoopCountEvent payload) {
        Long mongId = payload.getMongId();
        Integer addPoopCount = payload.getAddPoopCount();
        managementInternalService.decreasePoopCount(mongId, addPoopCount);
    }

    @KafkaListener(topics = { "rollback.deadMong" })
    public void deadMong(DeadMongScheduleEvent payload) {}
}
