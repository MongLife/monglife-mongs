package com.mongs.play.app.management.internal.consumer;

import com.mongs.play.app.management.internal.service.ManagementInternalService;
import com.mongs.play.module.kafka.event.managementWorker.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagementInternalCommitConsumer {

    private final ManagementInternalService managementInternalService;

    @KafkaListener(topics = { "commit.evolutionReadyMongSchedule" })
    public void evolutionReadyMong(EvolutionReadyMongScheduleEvent payload) {
        Long mongId = payload.getMongId();
        managementInternalService.evolutionReadyMong(mongId);
    }

    @KafkaListener(topics = { "commit.decreaseWeightSchedule" })
    public void decreaseWeight(DecreaseWeightScheduleEvent payload) {
        Long mongId = payload.getMongId();
        Double subWeight = payload.getSubWeight();
        managementInternalService.decreaseWeight(mongId, subWeight);
    }

    @KafkaListener(topics = { "commit.decreaseStrengthSchedule" })
    public void decreaseStrength(DecreaseStrengthScheduleEvent payload) {
        Long mongId = payload.getMongId();
        Double subStrength = payload.getSubStrength();
        managementInternalService.decreaseStrength(mongId, subStrength);
    }

    @KafkaListener(topics = { "commit.decreaseSatietySchedule" })
    public void decreaseSatiety(DecreaseSatietyScheduleEvent payload) {
        Long mongId = payload.getMongId();
        Double subSatiety = payload.getSubSatiety();
        managementInternalService.decreaseSatiety(mongId, subSatiety);
    }

    @KafkaListener(topics = { "commit.decreaseHealthySchedule" })
    public void decreaseHealthy(DecreaseHealthyScheduleEvent payload) {
        Long mongId = payload.getMongId();
        Double subHealthy = payload.getSubHealthy();
        managementInternalService.decreaseHealthy(mongId, subHealthy);
    }

    @KafkaListener(topics = { "commit.decreaseSleepSchedule" })
    public void decreaseSleep(DecreaseSleepScheduleEvent payload) {
        Long mongId = payload.getMongId();
        Double subSleep = payload.getSubSleep();
        managementInternalService.decreaseSleep(mongId, subSleep);
    }

    @KafkaListener(topics = { "commit.increasePoopCountSchedule" })
    public void increasePoopCount(IncreasePoopCountScheduleEvent payload) {
        Long mongId = payload.getMongId();
        Integer addPoopCount = payload.getAddPoopCount();
        managementInternalService.increasePoopCount(mongId, addPoopCount);
    }

    @KafkaListener(topics = { "commit.increaseSleepSchedule" })
    public void increaseSleep(IncreaseSleepScheduleEvent payload) {
        Long mongId = payload.getMongId();
        Double addWeight = payload.getAddSleep();
        managementInternalService.increaseSleep(mongId, addWeight);
    }

    @KafkaListener(topics = { "commit.deadMongSchedule" })
    public void deadMong(DeadMongScheduleEvent payload) {
        Long mongId = payload.getMongId();
        managementInternalService.deadMong(mongId);
    }
}
