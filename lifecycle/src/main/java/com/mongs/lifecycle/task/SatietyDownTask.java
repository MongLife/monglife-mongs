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
public class SatietyDownTask extends TimerTask implements BasicTask {

    private final TaskService taskService;
    private final TaskActiveService taskActiveService;

    private final MongEvent event;
    private Timer timer;

    public static SatietyDownTask of(TaskService taskService, TaskActiveService taskActiveService, MongEvent event) {
        return SatietyDownTask.builder()
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
        taskService.modifyTaskStatus(event.getId(), EventStatusCode.PROCESS, event.getEventCode());
        double satiety = taskActiveService.decreaseSatiety(event);

        if (satiety == 0D && !taskService.isActiveTask(event.getMongId(), MongEventCode.DEAD_SATIETY)) {
            taskService.startTask(event.getMongId(), MongEventCode.DEAD_SATIETY);
            log.info("[{}] 포만감 {} 도달 : 죽음 Task 실행", event.getMongId(), satiety);
        }

        timer.cancel();
        timer.purge();
        taskService.modifyTaskStatus(event.getId(), EventStatusCode.DONE, event.getEventCode());
        taskService.deleteTask(event.getEventId());
    }

    @Override
    public void run() {
        taskService.modifyTaskStatus(event.getId(), EventStatusCode.PROCESS, event.getEventCode());
        double satiety = taskActiveService.decreaseSatiety(event);

        if (satiety == 0D && !taskService.isActiveTask(event.getMongId(), MongEventCode.DEAD_SATIETY)) {
            taskService.startTask(event.getMongId(), MongEventCode.DEAD_SATIETY);
            log.info("[{}] 포만감 {} 도달 : 죽음 Task 실행", event.getMongId(), satiety);
        }

        taskService.modifyTaskStatus(event.getId(), EventStatusCode.DONE, event.getEventCode());
        taskService.deleteTask(event.getEventId());
        taskService.startTask(event.getMongId(), event.getEventCode());
    }
}
