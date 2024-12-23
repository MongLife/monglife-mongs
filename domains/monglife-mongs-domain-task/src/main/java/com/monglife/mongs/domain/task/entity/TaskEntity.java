package com.monglife.mongs.domain.task.entity;

import com.monglife.mongs.domain.task.enums.TaskStateCode;
import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import com.monglife.mongs.domain.task.listener.TaskEntityListener;
import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import com.monglife.mongs.module.jpa.entity.ComnCodeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class, TaskEntityListener.class })
@Table(name = "mongs_task")
@ToString
public class TaskEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", updatable = false)
    private Long taskId;

    @Column(name = "app_ackage_name", updatable = false)
    private String appPackageName;

    @Column(name = "task_owner_id", updatable = false)
    private String taskOwnerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_code", updatable = false)
    private ComnCodeEntity comn;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status_code")
    private TaskStatusCode taskStatusCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_state_code")
    private TaskStateCode taskStateCode;

    @Column(name = "rest_expiration_seconds")
    private Long restExpirationSeconds; // 남은 만료 시간

    @Column(name = "expiration_seconds")
    private Long expirationSeconds;     // 만료 시간

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;    // 만료 시각

    @Column(name = "fix_time", updatable = false)
    private LocalTime fixTime;

    public TaskEntity(String appPackageName, String taskOwnerId, ComnCodeEntity comn, TaskStateCode taskStateCode, LocalTime fixTime) {
        this.appPackageName = appPackageName;
        this.taskOwnerId = taskOwnerId;
        this.comn = comn;
        this.taskStateCode = taskStateCode;
        this.fixTime = fixTime == null ? LocalTime.of(0, 0) : fixTime;
        this.taskStatusCode = TaskStatusCode.PROCESSING;

        LocalDateTime now = LocalDateTime.now();
        this.resetFixTime(now);
    }

    public TaskEntity(String appPackageName, String taskOwnerId, ComnCodeEntity comn, TaskStateCode taskStateCode, Long expirationSeconds) {
        this.appPackageName = appPackageName;
        this.taskOwnerId = taskOwnerId;
        this.comn = comn;
        this.taskStateCode = taskStateCode;
        this.expirationSeconds = expirationSeconds;
        this.taskStatusCode = TaskStatusCode.PROCESSING;

        this.restExpirationSeconds = this.expirationSeconds;
        this.expiredAt = LocalDateTime.now().plusSeconds(this.restExpirationSeconds);
    }

    public Boolean isFixTime() {
        return this.taskStateCode.equals(TaskStateCode.FIX_TIME_CYCLE) || this.taskStateCode.equals(TaskStateCode.FIX_TIME);
    }

    public Boolean isCycle() {
        return this.taskStateCode.equals(TaskStateCode.FIX_TIME_CYCLE) || this.taskStateCode.equals(TaskStateCode.NONE_FIX_TIME_CYCLE);
    }

    public Boolean isProcessing() {
        return this.taskStatusCode.equals(TaskStatusCode.PROCESSING);
    }

    public void cycle() {

        if (!this.isProcessing() || !this.isCycle()) return;

        LocalDateTime now = LocalDateTime.now();

        if (this.isFixTime()) {
            this.expiredAt = LocalDateTime.of(now.toLocalDate().plusDays(1), this.fixTime);
            this.restExpirationSeconds = Duration.between(now, this.expiredAt).toSeconds();
        } else {
            this.restExpirationSeconds = this.expirationSeconds;
            this.expiredAt = now.plusSeconds(this.restExpirationSeconds);
        }
    }

    public void pause() {

        if (!this.isProcessing()) return;

        LocalDateTime now = LocalDateTime.now();

        if (this.isFixTime()) {
            this.expirationSeconds = null;
            this.restExpirationSeconds = null;
            this.expiredAt = null;
        } else {
            this.restExpirationSeconds = Math.max(1, Duration.between(now, this.expiredAt).toSeconds());
            this.expiredAt = null;
        }

        this.taskStatusCode = TaskStatusCode.PAUSE;
    }

    public void resume() {

        if (this.isProcessing()) return;

        LocalDateTime now = LocalDateTime.now();

        if (this.isFixTime()) {
            this.resetFixTime(now);
        } else {
            this.expiredAt = now.plusSeconds(this.restExpirationSeconds);
        }

        this.taskStatusCode = TaskStatusCode.PROCESSING;
    }

    public void appStopPause() {

        if (this.isProcessing()) {

            LocalDateTime now = LocalDateTime.now();

            if (this.isFixTime()) {
                this.expirationSeconds = null;
                this.restExpirationSeconds = null;
                this.expiredAt = null;
            } else {
                this.restExpirationSeconds = Math.max(1, Duration.between(now, this.expiredAt).toSeconds());
                this.expiredAt = null;
            }

            this.taskStatusCode = TaskStatusCode.APP_STOP_PROCESSING;
        } else {
            this.taskStatusCode = TaskStatusCode.APP_STOP_PAUSE;
        }
    }

    public void appStopResume() {

        if (TaskStatusCode.APP_STOP_PROCESSING.equals(this.taskStatusCode) || TaskStatusCode.PROCESSING.equals(this.taskStatusCode)) {

            LocalDateTime now = LocalDateTime.now();

            if (this.isFixTime()) {
                this.resetFixTime(now);
            } else {
                this.restExpirationSeconds = Math.max(1, this.restExpirationSeconds);
                this.expiredAt = now.plusSeconds(this.restExpirationSeconds);
            }

            this.taskStatusCode = TaskStatusCode.PROCESSING;

        } else if (TaskStatusCode.APP_STOP_PAUSE.equals(this.taskStatusCode)) {
            this.taskStatusCode = TaskStatusCode.PAUSE;
        }
    }

    private void resetFixTime(LocalDateTime now) {

        if (!this.isFixTime()) return;

        this.expiredAt = LocalDateTime.of(now.toLocalDate(), this.fixTime);

        if (this.expiredAt.isBefore(now)) {
            this.expiredAt = this.expiredAt.plusDays(1);
            this.expirationSeconds = Duration.between(now, this.expiredAt).toSeconds();
        } else if (this.expiredAt.isEqual(now)) {
            this.expirationSeconds = 1L;
        } else {
            this.expirationSeconds = Duration.between(now, this.expiredAt).toSeconds();
        }

        this.restExpirationSeconds = this.expirationSeconds;
    }
}
