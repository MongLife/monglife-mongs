package com.monglife.mongs.adapter.out.mong.schedule.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
public class TaskScheduleEntity {

    private final Long taskId;

    private final String appPackageName;

    private final Long mongId;

    private final Long accountId;

    private final String schedulerTypeCode;

    private final Boolean isCycle;

    private final LocalDateTime expiredAt;

    private Long expirationSeconds;

    @JsonIgnore
    private ScheduledFuture<?> scheduler;

    @Builder
    private TaskScheduleEntity(Long taskId, String appPackageName, Long mongId, Long accountId, String schedulerTypeCode, Boolean isCycle, LocalDateTime expiredAt) {
        this.taskId = taskId;
        this.appPackageName = appPackageName;
        this.mongId = mongId;
        this.accountId = accountId;
        this.schedulerTypeCode = schedulerTypeCode;
        this.isCycle = isCycle;
        this.expiredAt = expiredAt;
    }

    public void start(ScheduledExecutorService executor, Runnable runnable) {
        this.expirationSeconds = Math.max(1, Duration.between(LocalDateTime.now(), this.expiredAt).getSeconds());
        // executor 에 넘긴 러너블에서 난 예외는 아무도 get() 하지 않는 Future 안에 갇혀
        // 로그에 한 줄도 남지 않는다. 스케줄이 조용히 죽는 것을 막으려고 여기서 잡아 남긴다.
        this.scheduler = executor.schedule(() -> {
            try {
                runnable.run();
            } catch (Exception exception) {
                log.error("스케줄 실행 실패 taskId={}", this.taskId, exception);
            }
        }, this.expirationSeconds, TimeUnit.SECONDS);
    }

    public void stop() {
        this.scheduler.cancel(false);
    }

    public static TaskScheduleEntity of(TaskEntity taskEntity) {
        return TaskScheduleEntity.builder()
                .taskId(taskEntity.getTaskId())
                .appPackageName(taskEntity.getAppPackageName())
                .mongId(taskEntity.getMongId())
                .accountId(taskEntity.getAccountId())
                .schedulerTypeCode(taskEntity.getSchedulerTypeCode())
                .isCycle(taskEntity.isCycle())
                .expiredAt(taskEntity.getExpiredAt())
                .build();
    }
}
