package com.mongs.lifecycle.service.task;

import com.mongs.core.enums.lifecycle.TaskStatusCode;
import com.mongs.lifecycle.exception.EventTaskException;
import com.mongs.lifecycle.service.componentService.TaskActiveService;
import com.mongs.lifecycle.service.componentService.TaskService;
import com.mongs.lifecycle.service.vo.TaskEventVo;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Builder
public class EggTask implements BasicTask {

    private final TaskService taskService;
    private final TaskActiveService taskActiveService;
    private final ScheduledExecutorService executor;

    private final TaskEventVo taskEventVo;
    private ScheduledFuture<?> scheduler;

    public static EggTask of(
            TaskService taskService,
            TaskActiveService taskActiveService,
            ScheduledExecutorService executor,
            TaskEventVo taskEventVo
    ) {
        return EggTask.builder()
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
        try {
            taskService.processTask(taskEventVo.taskId());
            taskActiveService.eggEvolution(taskEventVo.mongId());
            taskService.doneTask(taskEventVo.taskId());
        } catch (EventTaskException e) {
            taskService.doneTask(taskEventVo.taskId());
        } finally {
            scheduler.cancel(false);
        }
    }

    private void run() {
        try {
            taskService.processTask(taskEventVo.taskId());
            taskActiveService.eggEvolution(taskEventVo.mongId());
            taskService.doneTask(taskEventVo.taskId());
        } catch (EventTaskException e) {
            e.printStackTrace();
            taskService.doneTask(taskEventVo.taskId());
        }
    }
}
