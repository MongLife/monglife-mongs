package com.mongs.lifecycle.task;

import com.mongs.lifecycle.service.TaskActiveService;
import com.mongs.lifecycle.service.TaskService;
import com.mongs.lifecycle.vo.TaskEventVo;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Builder
public class GeneratePoopTask extends TimerTask implements BasicTask {

    private final TaskService taskService;
    private final TaskActiveService taskActiveService;

    private final TaskEventVo taskEventVo;
    private Timer timer;

    public static GeneratePoopTask of(
            TaskService taskService,
            TaskActiveService taskActiveService,
            TaskEventVo taskEventVo
    ) {
        return GeneratePoopTask.builder()
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
        taskActiveService.increasePoop(taskEventVo.mongId(), taskEventVo.taskCode());
        timer.cancel();
        timer.purge();
        taskService.doneTask(taskEventVo.taskId());
    }

    @Override
    public void run() {
        taskService.processTask(taskEventVo.taskId());
        taskActiveService.increasePoop(taskEventVo.mongId(), taskEventVo.taskCode());
        taskService.doneTask(taskEventVo.taskId());
        taskService.startTask(taskEventVo.mongId(), taskEventVo.taskCode());
    }
}
