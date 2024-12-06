package com.monglife.mongs.domain.task.entity;

import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import com.monglife.mongs.domain.task.listener.TaskEntityListener;
import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import com.monglife.mongs.module.jpa.entity.ComnCodeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.time.LocalDateTime;

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

    @Column(name = "app_code", updatable = false)
    private String appCode;

    @Column(name = "task_owner_id", updatable = false)
    private String taskOwnerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_code", updatable = false)
    private ComnCodeEntity taskCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status_code")
    private TaskStatusCode taskStatusCode;

    @Column(name = "rest_expiration_seconds")
    private Long restExpirationSeconds;

    @Column(name = "expiration_seconds", updatable = false)
    private Long expirationSeconds;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "is_cycle", updatable = false)
    private Boolean isCycle;

    @Column(name = "cycle_seconds", updatable = false)
    private Long cycleSeconds;

    @Builder
    public TaskEntity(Long taskId, String appCode, String taskOwnerId, ComnCodeEntity taskCode, TaskStatusCode taskStatusCode, Long expirationSeconds, Boolean isCycle, Long cycleSeconds) {
        this.taskId = taskId;
        this.appCode = appCode;
        this.taskOwnerId = taskOwnerId;
        this.taskCode = taskCode;
        this.taskStatusCode = taskStatusCode;
        this.expirationSeconds = expirationSeconds <= 0 ? 1 : expirationSeconds;
        this.restExpirationSeconds = this.expirationSeconds;
        this.isCycle = isCycle;
        this.cycleSeconds = cycleSeconds <= 0 ? 1 : cycleSeconds;;

        if (TaskStatusCode.PROCESSING.equals(this.taskStatusCode)) {
            this.expiredAt = LocalDateTime.now().plusSeconds(this.expirationSeconds);
        }
    }

    public void cycle() {
        if (TaskStatusCode.PROCESSING.equals(this.taskStatusCode)) {
            this.expirationSeconds = this.cycleSeconds;
            this.restExpirationSeconds = this.expirationSeconds;
            this.expiredAt = LocalDateTime.now().plusSeconds(this.restExpirationSeconds);
        }
    }

    public void pause() {
        if (TaskStatusCode.PROCESSING.equals(this.taskStatusCode)) {
            this.taskStatusCode = TaskStatusCode.PAUSE;
            this.restExpirationSeconds = Math.max(1, Duration.between(LocalDateTime.now(), this.expiredAt).toSeconds());
            this.expiredAt = null;
        }
    }

    public void resume() {
        if (TaskStatusCode.PAUSE.equals(this.taskStatusCode)) {
            this.taskStatusCode = TaskStatusCode.PROCESSING;
            this.expiredAt = LocalDateTime.now().plusSeconds(this.restExpirationSeconds);
        }
    }

    public void delete() {
        this.taskStatusCode = TaskStatusCode.DELETE;
    }

    public void appStopPause() {
        if (TaskStatusCode.PROCESSING.equals(this.taskStatusCode)) {
            this.taskStatusCode = TaskStatusCode.APP_STOP_PROCESSING;
            this.restExpirationSeconds = Math.max(1, Duration.between(LocalDateTime.now(), this.expiredAt).toSeconds());
            this.expiredAt = null;
        } else if (TaskStatusCode.PAUSE.equals(this.taskStatusCode)) {
            this.taskStatusCode = TaskStatusCode.APP_STOP_PAUSE;
        }
    }

    public void appStopResume() {
        if (TaskStatusCode.APP_STOP_PROCESSING.equals(this.taskStatusCode)) {
            Long shutDownSeconds = Duration.between(this.getUpdatedAt(), LocalDateTime.now()).toSeconds();
            this.restExpirationSeconds = Math.max(1, this.restExpirationSeconds - shutDownSeconds);
            this.expirationSeconds = Math.max(1, this.expirationSeconds - shutDownSeconds);

            this.taskStatusCode = TaskStatusCode.PROCESSING;
            this.expiredAt = LocalDateTime.now().plusSeconds(this.restExpirationSeconds);
        } else if (TaskStatusCode.APP_STOP_PAUSE.equals(this.taskStatusCode)) {
            this.taskStatusCode = TaskStatusCode.PAUSE;
        }
    }
}
