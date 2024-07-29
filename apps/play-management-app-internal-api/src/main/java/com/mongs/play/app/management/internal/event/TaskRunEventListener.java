package com.mongs.play.app.management.internal.event;

import com.mongs.play.app.management.internal.service.ManagementInternalService;
import com.mongs.play.core.error.app.ManagementWorkerErrorCode;
import com.mongs.play.core.exception.app.ManagementWorkerException;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.task.enums.TaskCode;
import com.mongs.play.domain.task.enums.TaskUtil;
import com.mongs.play.domain.task.event.TaskRunEvent;
import com.mongs.play.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskRunEventListener {

    private final TaskService taskService;
    private final ManagementInternalService managementInternalService;

    @Async
    @EventListener
    public void taskRunEventListener(TaskRunEvent event) {

        String taskId = event.getTaskId();
        TaskCode taskCode = event.getTaskCode();
        Long mongId = event.getMongId();

        taskService.doneTask(taskId);

        switch (taskCode) {
            case ZERO_EVOLUTION -> {
                try {
                    managementInternalService.evolutionReady(mongId);
                } catch (NotFoundException e) {
                    taskService.forceStopAllTask(mongId);
                }
            }
            case DECREASE_STATUS -> {
                try {
                    taskService.startTask(mongId, taskCode);
                    managementInternalService.decreaseStatus(mongId, TaskUtil.subWeight, TaskUtil.subStrength, TaskUtil.subSatiety, TaskUtil.subHealthy, TaskUtil.subSleep);
                } catch (NotFoundException e) {
                    taskService.forceStopAllTask(mongId);
                }
            }
            case INCREASE_STATUS -> {
                try {
                    taskService.startTask(mongId, taskCode);
                    managementInternalService.increaseStatus(mongId, TaskUtil.addWeight, TaskUtil.addStrength, TaskUtil.addSatiety, TaskUtil.addHealthy, TaskUtil.addSleep);
                } catch (NotFoundException e) {
                    taskService.forceStopAllTask(mongId);
                }
            }
            case INCREASE_POOP_COUNT -> {
                try {
                    int addPoopCount = 1;
                    taskService.startTask(mongId, taskCode);
                    managementInternalService.increasePoopCount(mongId, addPoopCount);
                } catch (NotFoundException e) {
                    taskService.forceStopAllTask(mongId);
                }
            }
            case DEAD -> {
                try {
                    taskService.forceStopAllTask(mongId);
                    managementInternalService.dead(mongId);
                } catch (NotFoundException e) {
                    taskService.forceStopAllTask(mongId);
                }
            }
            default -> throw new ManagementWorkerException(ManagementWorkerErrorCode.INVALID_STOP_EVENT);
        }
    }
}
