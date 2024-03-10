package com.mongs.lifecycle.task;

import com.mongs.lifecycle.code.TaskStatusCode;
import com.mongs.lifecycle.code.TaskCode;
import com.mongs.lifecycle.entity.TaskEvent;
import com.mongs.lifecycle.service.TaskActiveService;
import com.mongs.lifecycle.service.TaskService;
import com.mongs.lifecycle.vo.TaskEventVo;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Builder
public class HealthyDownTask extends TimerTask implements BasicTask {

    private final TaskService taskService;
    private final TaskActiveService taskActiveService;

    private final TaskEventVo taskEventVo;
    private Timer timer;

    public static HealthyDownTask of(
            TaskService taskService,
            TaskActiveService taskActiveService,
            TaskEventVo taskEventVo
    ) {
        return HealthyDownTask.builder()
                .taskService(taskService)
                .taskActiveService(taskActiveService)
                .taskEventVo(taskEventVo)
                .timer(new Timer())
                .build();
    }

    @Override
    public void start() {
        timer.schedule(this, 1000 * taskEventVo.expiration());
    }

    @Override
    public void stop() {
        taskService.processTask(taskEventVo.taskId());
        double healthy = taskActiveService.decreaseHealthy(taskEventVo.mongId(), taskEventVo.taskCode(), taskEventVo.createdAt());

        if (healthy == 0D && !taskService.checkTaskActive(taskEventVo.mongId(), TaskCode.DEAD_HEALTHY)) {
            taskService.startTask(taskEventVo.mongId(), TaskCode.DEAD_HEALTHY);
            log.info("[{}] 체력 {} 도달 : 죽음 Task 실행", taskEventVo.mongId(), healthy);
        }

        timer.cancel();
        timer.purge();
        taskService.doneTask(taskEventVo.taskId());
    }

    @Override
    public void run() {
        taskService.processTask(taskEventVo.taskId());
        double healthy = taskActiveService.decreaseHealthy(taskEventVo.mongId(), taskEventVo.taskCode(), taskEventVo.createdAt());

        if (healthy == 0D && !taskService.checkTaskActive(taskEventVo.mongId(), TaskCode.DEAD_HEALTHY)) {
            taskService.startTask(taskEventVo.mongId(), TaskCode.DEAD_HEALTHY);
            log.info("[{}] 체력 {} 도달 : 죽음 Task 실행", taskEventVo.mongId(), healthy);
        }

        taskService.doneTask(taskEventVo.taskId());
        taskService.startTask(taskEventVo.mongId(), taskEventVo.taskCode());
    }
}
