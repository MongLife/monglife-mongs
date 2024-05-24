package com.mongs.play.app.management.worker.event;

import com.mongs.play.core.error.app.ManagementWorkerErrorCode;
import com.mongs.play.core.exception.app.ManagementWorkerException;
import com.mongs.play.module.kafka.service.ManagementInternalKafkaService;
import com.mongs.play.module.task.enums.TaskCode;
import com.mongs.play.module.task.enums.TaskUtil;
import com.mongs.play.module.task.event.TaskRunEvent;
import com.mongs.play.module.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskRunEventListener {

    private final TaskService taskService;
    private final ManagementInternalKafkaService managementInternalKafkaService;

    @EventListener
    public void taskRunEventListener(TaskRunEvent event) {

        String taskId = event.getTaskId();
        TaskCode taskCode = event.getTaskCode();
        Long mongId = event.getMongId();

        log.info("[Run] mongId: {}, taskCode: {}", mongId, taskCode);

        taskService.doneTask(taskId);

        switch (taskCode) {
            case ZERO_EVOLUTION -> {
                managementInternalKafkaService.evolutionReady(mongId);
            }
            case DECREASE_STATUS -> {
                managementInternalKafkaService.decreaseStatus(mongId, TaskUtil.subWeight, TaskUtil.subStrength, TaskUtil.subSatiety, TaskUtil.subHealthy, TaskUtil.subSleep);
                taskService.startTask(mongId, taskCode);
            }
            case INCREASE_STATUS -> {
                managementInternalKafkaService.increaseStatus(mongId, TaskUtil.addWeight, TaskUtil.addStrength, TaskUtil.addSatiety, TaskUtil.addHealthy, TaskUtil.addSleep);
                taskService.startTask(mongId, taskCode);
            }
            case INCREASE_POOP_COUNT -> {
                int addPoopCount = 1;
                managementInternalKafkaService.increasePoopCount(mongId, addPoopCount);
                taskService.startTask(mongId, taskCode);
            }
            case DEAD_HEALTHY, DEAD_SATIETY -> {
                managementInternalKafkaService.dead(mongId);
                taskService.forceStopAllTask(mongId);
            }
            default -> throw new ManagementWorkerException(ManagementWorkerErrorCode.INVALID_STOP_EVENT);
        }
    }
}
