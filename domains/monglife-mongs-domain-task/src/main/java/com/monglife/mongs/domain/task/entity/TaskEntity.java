package com.monglife.mongs.domain.task.entity;

import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import com.monglife.mongs.domain.task.listener.TaskEntityListener;
import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import com.monglife.mongs.module.jpa.entity.ComnCodeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class, TaskEntityListener.class })
@Table(name = "mongs_task")
@ToString(exclude = "previousTaskEntity")
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

    @Setter
    @Column(name = "task_status_code")
    private TaskStatusCode taskStatusCode;

    @Column(name = "expiration_seconds", updatable = false)
    private Long expirationSeconds;

    @Column(name = "expired_at", updatable = false)
    private LocalDateTime expiredAt;

    @Column(name = "is_cycle", updatable = false)
    private Boolean isCycle;

    @Column(name = "cycle_seconds", updatable = false)
    private Long cycleSeconds;

    @Setter
    @Transient
    private TaskEntity previousTaskEntity;

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

    public TaskEntity clone() {
        return TaskEntity.builder()
                .taskId(this.taskId)
                .appCode(this.appCode)
                .taskOwnerId(this.taskOwnerId)
                .taskCode(this.taskCode)
                .taskStatusCode(this.taskStatusCode)
                .expirationSeconds(this.expirationSeconds)
                .expiredAt(this.expiredAt)
                .isCycle(this.isCycle)
                .cycleSeconds(this.cycleSeconds)
                .build();
    }
}
