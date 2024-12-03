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

    @Column(name = "task_status_code")
    private TaskStatusCode taskStatusCode;

    @Column(name = "expiration_seconds")
    private Long expirationSeconds;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "is_cycle", updatable = false)
    private Boolean isCycle;

    @Column(name = "cycle_seconds", updatable = false)
    private Long cycleSeconds;

    @Builder
    public TaskEntity(Long taskId, String appCode, String taskOwnerId, ComnCodeEntity taskCode, TaskStatusCode taskStatusCode, Long expirationSeconds, LocalDateTime expiredAt, Boolean isCycle, Long cycleSeconds) {
        this.taskId = taskId;
        this.appCode = appCode;
        this.taskOwnerId = taskOwnerId;
        this.taskCode = taskCode;
        this.taskStatusCode = taskStatusCode;
        this.expirationSeconds = expirationSeconds;
        this.expiredAt = expiredAt;
        this.isCycle = isCycle;
        this.cycleSeconds = cycleSeconds;
    }

    public void pause() {
        this.taskStatusCode = TaskStatusCode.PAUSE;
        this.expirationSeconds = Duration.between(LocalDateTime.now(), this.expiredAt).toSeconds();
        this.expiredAt = null;
    }

    public void resume() {
        this.taskStatusCode = TaskStatusCode.PROCESSING;
        this.expiredAt = LocalDateTime.now().plusSeconds(this.expirationSeconds);
    }

    public void appStopPause() {
        if (TaskStatusCode.PROCESSING.equals(this.taskStatusCode)) {
            this.taskStatusCode = TaskStatusCode.APP_STOP_PROCESSING;
        } else {
            this.taskStatusCode = TaskStatusCode.APP_STOP_PAUSE;
        }
        this.expirationSeconds = Duration.between(LocalDateTime.now(), this.expiredAt).toSeconds();
        this.expiredAt = null;
    }

    public void appStopResume() {
        if (TaskStatusCode.APP_STOP_PROCESSING.equals(this.taskStatusCode)) {
            this.taskStatusCode = TaskStatusCode.PROCESSING;
        } else {
            this.taskStatusCode = TaskStatusCode.PAUSE;
        }
        this.expiredAt = LocalDateTime.now().plusSeconds(this.expirationSeconds);
    }
}
