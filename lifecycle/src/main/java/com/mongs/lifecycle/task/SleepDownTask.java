package com.mongs.lifecycle.task;

import com.mongs.lifecycle.code.TaskStatusCode;
import com.mongs.lifecycle.service.TaskActiveService;
import com.mongs.lifecycle.service.TaskService;
import com.mongs.lifecycle.vo.TaskEventVo;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Builder
public class SleepDownTask implements BasicTask {

    private final TaskService taskService;
    private final TaskActiveService taskActiveService;
    private final ScheduledExecutorService executor;

    private final TaskEventVo taskEventVo;
    private ScheduledFuture<?> scheduler;

    public static SleepDownTask of(
            TaskService taskService,
            TaskActiveService taskActiveService,
            ScheduledExecutorService executor,
            TaskEventVo taskEventVo
    ) {
        return SleepDownTask.builder()
                .taskService(taskService)
                .taskActiveService(taskActiveService)
                .executor(executor)
                .taskEventVo(taskEventVo)
                .build();
    }

    @Override
    public void start() {
        scheduler = this.executor.schedule(this::run, 1000 * taskEventVo.expiration(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void pause(TaskStatusCode taskStatusCode) {
        taskService.pauseTask(taskEventVo.taskId(), taskStatusCode);
        scheduler.cancel(false);
    }

    @Override
    public void stop() {
        taskService.processTask(taskEventVo.taskId());
        taskActiveService.decreaseSleep(taskEventVo.mongId(), taskEventVo.taskCode(), taskEventVo.createdAt());
        scheduler.cancel(false);
        taskService.doneTask(taskEventVo.taskId());
    }

    private void run() {
        taskService.processTask(taskEventVo.taskId());
        taskActiveService.decreaseSleep(taskEventVo.mongId(), taskEventVo.taskCode(), taskEventVo.createdAt());
        taskService.doneTask(taskEventVo.taskId());
        taskService.startTask(taskEventVo.mongId(), taskEventVo.taskCode());
    }
}
