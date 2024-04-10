package com.mongs.lifecycle.service.task;

import com.mongs.core.enums.lifecycle.TaskCode;
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
public class SatietyDownTask implements BasicTask {

    private final TaskService taskService;
    private final TaskActiveService taskActiveService;
    private final ScheduledExecutorService executor;

    private final TaskEventVo taskEventVo;
    private ScheduledFuture<?> scheduler;

    public static SatietyDownTask of(
            TaskService taskService,
            TaskActiveService taskActiveService,
            ScheduledExecutorService executor,
            TaskEventVo taskEventVo
    ) {
        return SatietyDownTask.builder()
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
            double satiety = taskActiveService.decreaseSatiety(taskEventVo.mongId(), taskEventVo.taskCode(), taskEventVo.createdAt());

            /*
            * 포만감이 0이고
            * 포만감 죽음 테스크가 실행중이지 않고
            * 몽이 생존해 있는 경우 포만감 죽음 테스크가 실행된다.
             */
            if (satiety == 0D &&
                    !taskService.isTaskActive(taskEventVo.mongId(), TaskCode.DEAD_SATIETY) &&
                    taskActiveService.isMongActive(taskEventVo.mongId())
            ) {
                taskService.startTask(taskEventVo.mongId(), TaskCode.DEAD_SATIETY);
            }
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
            double satiety = taskActiveService.decreaseSatiety(taskEventVo.mongId(), taskEventVo.taskCode(), taskEventVo.createdAt());

            /*
             * 포만감이 0이고
             * 포만감 죽음 테스크가 실행중이지 않고
             * 몽이 생존해 있는 경우 포만감 죽음 테스크가 실행된다.
             */
            if (satiety == 0D &&
                    !taskService.isTaskActive(taskEventVo.mongId(), TaskCode.DEAD_SATIETY) &&
                    taskActiveService.isMongActive(taskEventVo.mongId())
            ) {
                taskService.startTask(taskEventVo.mongId(), TaskCode.DEAD_SATIETY);
            }

            taskService.doneTask(taskEventVo.taskId());
            taskService.startTask(taskEventVo.mongId(), taskEventVo.taskCode());
        } catch (EventTaskException e) {
            taskService.doneTask(taskEventVo.taskId());
        }
    }
}
