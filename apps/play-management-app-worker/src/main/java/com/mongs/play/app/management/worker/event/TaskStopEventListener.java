package com.mongs.play.app.management.worker.event;

import com.mongs.play.core.error.app.ManagementWorkerErrorCode;
import com.mongs.play.core.exception.app.ManagementWorkerException;
import com.mongs.play.module.kafka.service.ManagementInternalKafkaService;
import com.mongs.play.module.task.enums.TaskCode;
import com.mongs.play.module.task.enums.TaskUtil;
import com.mongs.play.module.task.event.TaskStopEvent;
import com.mongs.play.module.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskStopEventListener {

    private final TaskService taskService;
    private final ManagementInternalKafkaService managementInternalKafkaService;

    @EventListener
    public void taskStopEventListener(TaskStopEvent event) {

        String taskId = event.getTaskId();
        TaskCode taskCode = event.getTaskCode();
        Long mongId = event.getMongId();
        Long expiration = event.getExpiration();
        Long duringSeconds = event.getDuringSeconds();

        log.info("[Stop] mongId: {}, taskCode: {}, duringSeconds: {}", mongId, taskCode, duringSeconds);

        taskService.doneTask(taskId);

        switch (taskCode) {
            case ZERO_EVOLUTION -> {}
            case DECREASE_STATUS -> {
                double subWeight = TaskUtil.subWeight / TaskUtil.decreaseStatusExpiration * duringSeconds;
                double subStrength = TaskUtil.subStrength / TaskUtil.decreaseStatusExpiration * duringSeconds;
                double subSatiety = TaskUtil.subSatiety / TaskUtil.decreaseStatusExpiration * duringSeconds;
                double subHealthy = TaskUtil.subHealthy / TaskUtil.decreaseStatusExpiration * duringSeconds;
                double subSleep = TaskUtil.subSleep / TaskUtil.decreaseStatusExpiration * duringSeconds;

                managementInternalKafkaService.decreaseStatus(mongId, subWeight, subStrength, subSatiety, subHealthy, subSleep);
            }
            case INCREASE_STATUS -> {
                double addWeight = TaskUtil.addWeight / TaskUtil.increaseStatusExpiration * duringSeconds;
                double addStrength = TaskUtil.addStrength / TaskUtil.increaseStatusExpiration * duringSeconds;
                double addSatiety = TaskUtil.addSatiety / TaskUtil.increaseStatusExpiration * duringSeconds;
                double addHealthy = TaskUtil.addHealthy / TaskUtil.increaseStatusExpiration * duringSeconds;
                double addSleep = TaskUtil.addSleep / TaskUtil.increaseStatusExpiration * duringSeconds;

                managementInternalKafkaService.increaseStatus(mongId, addWeight, addStrength, addSatiety, addHealthy, addSleep);
            }
            case INCREASE_POOP_COUNT -> {
                if ((double) (duringSeconds / expiration) > 0.5) {
                    int addPoopCount = 1;
                    managementInternalKafkaService.increasePoopCount(mongId, addPoopCount);
                }
            }
            case DEAD_HEALTHY, DEAD_SATIETY -> {}
            default -> throw new ManagementWorkerException(ManagementWorkerErrorCode.INVALID_PAUSE_EVENT);
        }
    }
}
