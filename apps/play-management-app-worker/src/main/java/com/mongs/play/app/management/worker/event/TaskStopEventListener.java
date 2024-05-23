package com.mongs.play.app.management.worker.event;

import com.mongs.play.core.error.app.ManagementWorkerErrorCode;
import com.mongs.play.core.exception.app.ManagementWorkerException;
import com.mongs.play.module.kafka.service.ManagementInternalKafkaService;
import com.mongs.play.module.task.enums.TaskCode;
import com.mongs.play.module.task.event.TaskStopEvent;
import com.mongs.play.module.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskStopEventListener {

    private final TaskService taskService;
    private final ManagementInternalKafkaService managementInternalKafkaService;

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

        String taskId = event.getTaskId();
        TaskCode taskCode = event.getTaskCode();
        Long mongId = event.getMongId();
        Long duringSeconds = event.getDuringSeconds();

        log.info("[Stop] mongId: {}, taskCode: {}, duringSeconds: {}", mongId, taskCode, duringSeconds);

        taskService.doneTask(taskId);

        switch (taskCode) {
            case ZERO_EVOLUTION -> {}
            case DECREASE_STATUS -> {
                double subWeight = this.subWeight / taskCode.getExpiration() * duringSeconds;
                double subStrength = this.subStrength / taskCode.getExpiration() * duringSeconds;
                double subSatiety = this.subSatiety / taskCode.getExpiration() * duringSeconds;
                double subHealthy = this.subHealthy / taskCode.getExpiration() * duringSeconds;
                double subSleep = this.subSleep / taskCode.getExpiration() * duringSeconds;

                managementInternalKafkaService.decreaseStatus(mongId, subWeight, subStrength, subSatiety, subHealthy, subSleep);
            }
            case INCREASE_STATUS -> {
                double addWeight = this.addWeight / taskCode.getExpiration() * duringSeconds;
                double addStrength = this.addStrength / taskCode.getExpiration() * duringSeconds;
                double addSatiety = this.addSatiety / taskCode.getExpiration() * duringSeconds;
                double addHealthy = this.addHealthy / taskCode.getExpiration() * duringSeconds;
                double addSleep = this.addSleep / taskCode.getExpiration() * duringSeconds;

                managementInternalKafkaService.increaseStatus(mongId, addWeight, addStrength, addSatiety, addHealthy, addSleep);
            }
            case INCREASE_POOP_COUNT -> {
                if ((double) (duringSeconds / taskCode.getExpiration()) > 0.5) {
                    int addPoopCount = 1;
                    managementInternalKafkaService.increasePoopCount(mongId, addPoopCount);
                }
            }
            case DEAD_HEALTHY, DEAD_SATIETY -> {}
            default -> throw new ManagementWorkerException(ManagementWorkerErrorCode.INVALID_PAUSE_EVENT);
        }
    }
}
