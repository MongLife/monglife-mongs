package com.mongs.play.module.task.entity;

import com.mongs.play.module.task.enums.TaskCode;
import com.mongs.play.module.task.event.*;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
@Builder
public class Task {
    private String taskId;
    private TaskCode taskCode;
    private Long mongId;
    private Long expiration;
    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;

    private ApplicationEventPublisher publisher;
    private ScheduledExecutorService executor;
    private ScheduledFuture<?> scheduler;

    public void run() {
        publisher.publishEvent(TaskRunEvent.builder()
                .taskId(taskId)
                .taskCode(taskCode)
                .mongId(mongId)
                .build());
    }

    public void start() {
        scheduler = this.executor.schedule(this::run, 1000 * expiration, TimeUnit.MILLISECONDS);
    }

    public void pause() {
        publisher.publishEvent(TaskPauseEvent.builder()
                .taskCode(taskCode)
                .mongId(mongId)
                .build());

        scheduler.cancel(false);
    }

    public void forceStop() {
        scheduler.cancel(false);
    }

    public void stop() {
        Long duringSeconds = Math.max(0, Math.min(expiration, Duration.between(createdAt, LocalDateTime.now()).getSeconds()));
        publisher.publishEvent(TaskStopEvent.builder()
                .taskId(taskId)
                .taskCode(taskCode)
                .mongId(mongId)
                .expiration(expiration)
                .duringSeconds(duringSeconds)
                .build());

        scheduler.cancel(false);
    }
}
