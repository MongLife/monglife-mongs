package com.mongs.play.domain.task.entity;

import com.mongs.play.domain.task.enums.TaskCode;
import com.mongs.play.domain.task.enums.TaskStatus;
import com.mongs.play.module.jpa.baseEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder(toBuilder = true)
@Table(name = "task_event")
public class TaskEvent extends BaseTimeEntity implements Persistable<String> {
    @Id
    @Builder.Default
    private String taskId = UUID.randomUUID().toString();
    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus = TaskStatus.WAIT;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskCode taskCode;
    @Column(nullable = false)
    private Long mongId;
    @Column(nullable = false)
    private Long expiration;

    private LocalDateTime expiredAt;

    public static TaskEvent of(Long mongId, TaskCode taskCode, Long expiration) {
        LocalDateTime expiredAt = LocalDateTime.now().plusSeconds(expiration);
        return TaskEvent.builder()
                .taskCode(taskCode)
                .mongId(mongId)
                .expiration(expiration)
                .expiredAt(expiredAt)
                .build();
    }

    @Override
    public String getId() {
        return this.taskId;
    }

    @Override
    public boolean isNew() {
        return this.getCreatedAt() == null;
    }
}
