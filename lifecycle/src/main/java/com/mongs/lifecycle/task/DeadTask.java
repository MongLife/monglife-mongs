package com.mongs.lifecycle.task;

import com.mongs.lifecycle.code.EventStatusCode;
import com.mongs.lifecycle.code.MongEventCode;
import com.mongs.lifecycle.entity.MongEvent;
import com.mongs.lifecycle.service.TaskActiveService;
import com.mongs.lifecycle.service.TaskService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Builder
public class DeadTask extends TimerTask implements BasicTask {

    private final TaskService taskService;
    private final TaskActiveService taskActiveService;

    private final MongEvent event;
    private Timer timer;

    public static DeadTask of(TaskService taskService, TaskActiveService taskActiveService, MongEvent event) {
        return DeadTask.builder()
                .taskService(taskService)
                .taskActiveService(taskActiveService)
                .event(event)
                .build();
    }

    @Override
    public MongEvent getEvent() {
        return this.event;
    }

    @Override
    public void start() {
        this.timer = new Timer();
        timer.schedule(this, 1000 * event.getExpiration());
    }

    @Override
    public void stop() {
        taskService.modifyTaskStatus(event.getEventId(), EventStatusCode.PROCESS, event.getEventCode());
        timer.cancel();
        timer.purge();
        taskService.modifyTaskStatus(event.getId(), EventStatusCode.DONE, event.getEventCode());
        taskService.deleteTask(event.getEventId());
    }

    @Override
    public void run() {
        taskService.modifyTaskStatus(event.getId(), EventStatusCode.PROCESS, event.getEventCode());
        taskActiveService.dead(event);
        taskService.stopAllTask(event.getMongId());
        taskService.modifyTaskStatus(event.getId(), EventStatusCode.DONE, event.getEventCode());
        taskService.deleteTask(event.getEventId());
    }
}
