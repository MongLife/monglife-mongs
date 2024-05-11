package com.mongs.play.app.management.worker.service;

import com.mongs.play.module.kafka.event.managementWorker.*;
import com.mongs.play.module.kafka.service.KafkaService.KafkaTopic;
import com.mongs.play.module.kafka.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementWorkerService {

    private final KafkaService kafkaService;

    public void evolutionReadyMong(Long mongId) {
        kafkaService.sendCommit(KafkaTopic.EVOLUTION_READY_MONG_SCHEDULE, EvolutionReadyMongScheduleEvent.builder()
                .mongId(mongId)
                .build());
    }

    public void decreaseWeight(Long mongId, Double subWeight) {
        kafkaService.sendCommit(KafkaTopic.DECREASE_WEIGHT_SCHEDULE, DecreaseWeightScheduleEvent.builder()
                .mongId(mongId)
                .subWeight(subWeight)
                .build());
    }

    public void decreaseStrength(Long mongId, Double subStrength) {
        kafkaService.sendCommit(KafkaTopic.DECREASE_STRENGTH_SCHEDULE, DecreaseStrengthScheduleEvent.builder()
                .mongId(mongId)
                .subStrength(subStrength)
                .build());
    }

    public void decreaseSatiety(Long mongId, Double subSatiety) {
        kafkaService.sendCommit(KafkaTopic.DECREASE_SATIETY_SCHEDULE, DecreaseSatietyScheduleEvent.builder()
                .mongId(mongId)
                .subSatiety(subSatiety)
                .build());
    }

    public void decreaseHealthy(Long mongId, Double subHealthy) {
        kafkaService.sendCommit(KafkaTopic.DECREASE_HEALTHY_SCHEDULE, DecreaseHealthyScheduleEvent.builder()
                .mongId(mongId)
                .subHealthy(subHealthy)
                .build());
    }

    public void decreaseSleep(Long mongId, Double subSleep) {
        kafkaService.sendCommit(KafkaTopic.DECREASE_SLEEP_SCHEDULE, DecreaseSleepScheduleEvent.builder()
                .mongId(mongId)
                .subSleep(subSleep)
                .build());
    }

    public void increasePoopCount(Long mongId, Integer addPoopCount) {
        kafkaService.sendCommit(KafkaTopic.INCREASE_POOP_COUNT_SCHEDULE, IncreasePoopCountScheduleEvent.builder()
                .mongId(mongId)
                .addPoopCount(addPoopCount)
                .build());
    }

    public void increaseSleep(Long mongId, Double addSleep) {
        kafkaService.sendCommit(KafkaTopic.INCREASE_POOP_COUNT_SCHEDULE, IncreaseSleepScheduleEvent.builder()
                .mongId(mongId)
                .addSleep(addSleep)
                .build());
    }

    public void deadMong(Long mongId) {
        kafkaService.sendCommit(KafkaTopic.DEAD_MONG_SCHEDULE, DeadMongScheduleEvent.builder()
                .mongId(mongId)
                .build());
    }
}
