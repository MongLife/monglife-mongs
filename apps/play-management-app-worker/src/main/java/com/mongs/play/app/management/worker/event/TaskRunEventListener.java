package com.mongs.play.app.management.worker.event;

import com.mongs.play.core.error.app.ManagementWorkerErrorCode;
import com.mongs.play.core.exception.app.ManagementWorkerException;
import com.mongs.play.module.kafka.event.managementWorker.*;
import com.mongs.play.module.kafka.service.KafkaService;
import com.mongs.play.module.task.enums.TaskCode;
import com.mongs.play.module.task.event.TaskRunEvent;
import com.mongs.play.module.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskRunEventListener {

    private final KafkaService kafkaService;
    private final TaskService taskService;

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
    public void taskRunEventListener(TaskRunEvent event) {

        String taskId = event.getTaskId();
        TaskCode taskCode = event.getTaskCode();
        Long mongId = event.getMongId();

        log.info("[Run] mongId: {}, taskCode: {}", mongId, taskCode);

        switch (taskCode) {
            case ZERO_EVOLUTION -> {
                kafkaService.sendCommit(KafkaService.CommitTopic.ZERO_EVOLUTION_SCHEDULE, ZeroEvolutionScheduleEvent.builder()
                        .mongId(mongId)
                        .build());

                taskService.doneTask(taskId);
            }
            case DECREASE_STATUS -> {
                kafkaService.sendCommit(KafkaService.CommitTopic.DECREASE_STATUS_SCHEDULE, DecreaseStatusScheduleEvent.builder()
                        .mongId(mongId)
                        .subWeight(subWeight)
                        .subStrength(subStrength)
                        .subSatiety(subSatiety)
                        .subHealthy(subHealthy)
                        .subSleep(subSleep)
                        .build());

                taskService.doneTask(taskId);
                taskService.startTask(mongId, taskCode);
            }
            case INCREASE_STATUS -> {
                kafkaService.sendCommit(KafkaService.CommitTopic.INCREASE_STATUS_SCHEDULE, IncreaseStatusScheduleEvent.builder()
                        .mongId(mongId)
                        .addWeight(addWeight)
                        .addStrength(addStrength)
                        .addSatiety(addSatiety)
                        .addHealthy(addHealthy)
                        .addSleep(addSleep)
                        .build());

                taskService.doneTask(taskId);
                taskService.startTask(mongId, taskCode);
            }
            case INCREASE_POOP_COUNT -> {
                int addPoopCount = 1;
                kafkaService.sendCommit(KafkaService.CommitTopic.INCREASE_POOP_COUNT_SCHEDULE, IncreasePoopCountScheduleEvent.builder()
                        .mongId(mongId)
                        .addPoopCount(addPoopCount)
                        .build());

                taskService.doneTask(taskId);
                taskService.startTask(mongId, taskCode);
            }
            case DEAD_HEALTHY, DEAD_SATIETY -> {
                kafkaService.sendCommit(KafkaService.CommitTopic.DEAD_SCHEDULE, DeadScheduleEvent.builder()
                        .mongId(mongId)
                        .build());

                taskService.stopAllTask(mongId);
            }
            default -> throw new ManagementWorkerException(ManagementWorkerErrorCode.INVALID_STOP_EVENT);
        }
    }
}
