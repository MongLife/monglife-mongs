package com.mongs.play.module.task.entity;

import com.mongs.play.module.task.enums.TaskCode;
import com.mongs.play.module.task.enums.TaskStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@RedisHash("task_event")
public class TaskEvent {
    @Id
    @Builder.Default
    private String taskId = UUID.randomUUID().toString();
    @Builder.Default
    @Indexed
    private TaskStatus taskStatus = TaskStatus.WAIT;
    @Indexed
    private TaskCode taskCode;
    @Indexed
    private Long mongId;
    private Long expiration;
    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;

    public static TaskEvent of(Long mongId, TaskCode taskCode, Long expiration) {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiredAt = createdAt.plusSeconds(expiration);
        return TaskEvent.builder()
                .taskCode(taskCode)
                .mongId(mongId)
                .expiration(expiration)
                .expiredAt(expiredAt)
                .createdAt(createdAt)
                .build();
    }
}
