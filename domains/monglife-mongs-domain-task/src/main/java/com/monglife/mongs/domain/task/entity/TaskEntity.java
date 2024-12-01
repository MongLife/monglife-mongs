package com.monglife.mongs.domain.task.entity;

import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class })
@Table(name = "mongs_task")
public class TaskEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "app_code")
    private String appCode;

    @Column(name = "task_owner_id")
    private String taskOwnerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_code")
    private ComnCodeEntity taskCode;

    @Setter
    @Column(name = "task_status_code")
    private TaskStatusCode taskStatusCode;

    @Column(name = "expiration_seconds")
    private Long expirationSeconds;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "is_cycle")
    private Boolean isCycle;

    @Column(name = "cycle_seconds")
    private Long cycleSeconds;

    @Builder
    public TaskEntity(String appCode, String taskOwnerId, ComnCodeEntity taskCode, TaskStatusCode taskStatusCode, Long expirationSeconds, LocalDateTime expiredAt, Boolean isCycle, Long cycleSeconds) {
        this.appCode = appCode;
        this.taskOwnerId = taskOwnerId;
        this.taskCode = taskCode;
        this.taskStatusCode = taskStatusCode;
        this.expirationSeconds = expirationSeconds;
        this.expiredAt = expiredAt;
        this.isCycle = isCycle;
        this.cycleSeconds = cycleSeconds;
    }
}
