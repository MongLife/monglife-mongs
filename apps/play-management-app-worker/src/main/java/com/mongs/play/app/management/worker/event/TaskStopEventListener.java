package com.mongs.play.app.management.worker.event;

import com.mongs.play.core.error.app.ManagementWorkerErrorCode;
import com.mongs.play.core.exception.app.ManagementWorkerException;
import com.mongs.play.module.kafka.event.managementWorker.*;
import com.mongs.play.module.kafka.service.KafkaService;
import com.mongs.play.module.task.enums.TaskCode;
import com.mongs.play.module.task.event.TaskStopEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskStopEventListener {

    private final KafkaService kafkaService;

    @Value("${application.status.sub.weight}")
    private Double subWeight;
    @Value("${application.status.sub.strength}")
    private Double subStrength;
    @Value("${application.status.sub.satiety}")
    private Double subSatiety;
    @Value("${application.status.sub.healthy}")
    private Double subHealthy;
    @Value("${application.status.sub.sleep}")
    private Double subSleep;

    @Value("${application.status.add.weight}")
    private Double addWeight;
    @Value("${application.status.add.strength}")
    private Double addStrength;
    @Value("${application.status.add.satiety}")
    private Double addSatiety;
    @Value("${application.status.add.healthy}")
    private Double addHealthy;
    @Value("${application.status.add.sleep}")
    private Double addSleep;

    @EventListener
    public void taskStopEventListener(TaskStopEvent event) {

        TaskCode taskCode = event.getTaskCode();
        Long mongId = event.getMongId();
        Long duringSeconds = event.getDuringSeconds();

        log.info("[Stop] mongId: {}, taskCode: {}, duringSeconds: {}", mongId, taskCode, duringSeconds);

        switch (taskCode) {
            case ZERO_EVOLUTION -> {}
            case DECREASE_STATUS -> {
                double weight = subWeight / taskCode.getExpiration() * duringSeconds;
                double strength = subStrength / taskCode.getExpiration() * duringSeconds;
                double satiety = subSatiety / taskCode.getExpiration() * duringSeconds;
                double healthy = subHealthy / taskCode.getExpiration() * duringSeconds;
                double sleep = subSleep / taskCode.getExpiration() * duringSeconds;
                kafkaService.sendCommit(KafkaService.KafkaTopic.DECREASE_STATUS_SCHEDULE, DecreaseStatusScheduleEvent.builder()
                        .mongId(mongId)
                        .subWeight(weight)
                        .subStrength(strength)
                        .subSatiety(satiety)
                        .subHealthy(healthy)
                        .subSleep(sleep)
                        .build());
            }
            case INCREASE_STATUS -> {
                double weight = addWeight / taskCode.getExpiration() * duringSeconds;
                double strength = addStrength / taskCode.getExpiration() * duringSeconds;
                double satiety = addSatiety / taskCode.getExpiration() * duringSeconds;
                double healthy = addHealthy / taskCode.getExpiration() * duringSeconds;
                double sleep = addSleep / taskCode.getExpiration() * duringSeconds;
                kafkaService.sendCommit(KafkaService.KafkaTopic.INCREASE_STATUS_SCHEDULE, IncreaseStatusScheduleEvent.builder()
                        .mongId(mongId)
                        .addWeight(weight)
                        .addStrength(strength)
                        .addSatiety(satiety)
                        .addHealthy(healthy)
                        .addSleep(sleep)
                        .build());
            }
            case INCREASE_POOP_COUNT -> {
                if ((double) (duringSeconds / taskCode.getExpiration()) > 0.5) {
                    int addValue = 1;
                    kafkaService.sendCommit(KafkaService.KafkaTopic.INCREASE_POOP_COUNT_SCHEDULE, IncreasePoopCountScheduleEvent.builder()
                            .mongId(mongId)
                            .addPoopCount(addValue)
                            .build());
                }
            }
            case DEAD_HEALTHY, DEAD_SATIETY -> {}
            default -> throw new ManagementWorkerException(ManagementWorkerErrorCode.INVALID_PAUSE_EVENT);
        }
    }
}
