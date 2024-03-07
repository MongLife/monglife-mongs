package com.mongs.lifecycle.thread;

import com.mongs.lifecycle.code.EventStatusCode;
import com.mongs.lifecycle.code.MongEventCode;
import com.mongs.lifecycle.entity.MongEvent;
import com.mongs.lifecycle.service_.TaskActiveService;
import com.mongs.lifecycle.service_.TaskService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Builder
public class HealthyDownTask extends TimerTask implements BasicTask {

    private final TaskService taskService;
    private final TaskActiveService taskActiveService;

    private final MongEvent event;
    private Timer timer;

    public static HealthyDownTask of(TaskService taskService, TaskActiveService taskActiveService, MongEvent event) {
        return HealthyDownTask.builder()
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
        log.info("[HealthyDownTask] start, getEventId: {}", event.getEventId());
        this.timer = new Timer();
        timer.schedule(this, 1000 * event.getExpiration());
    }

    @Override
    public void stop() {
        log.info("[HealthyDownTask] stop, getEventId: {}", event.getEventId());
        taskService.modifyTaskStatus(event.getEventId(), EventStatusCode.PROCESS);
        taskActiveService.decreaseHealthy(event);
        timer.cancel();
        timer.purge();
        taskService.modifyTaskStatus(event.getEventId(), EventStatusCode.DONE);
    }

    @Override
    public void run() {
        log.info("[HealthyDownTask] exec, getEventId: {}", event.getEventId());
        taskService.modifyTaskStatus(event.getEventId(), EventStatusCode.PROCESS);
        taskActiveService.decreaseHealthy(event);
        taskService.modifyTaskStatus(event.getEventId(), EventStatusCode.DONE);
        taskService.registerTask(event.getMongId(), MongEventCode.HEALTHY_DOWN);
    }
}
