package com.mongs.lifecycle.entity;

import com.mongs.lifecycle.code.TaskStatusCode;
import com.mongs.lifecycle.code.TaskCode;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@Document(collection = "task_event")
public class TaskEvent {
    @Id
    @Builder.Default
    private String taskId = UUID.randomUUID().toString();
    private TaskCode taskCode;
    private Long mongId;
    private Long expiration;
    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;
    @Builder.Default
    private TaskStatusCode statusCode = TaskStatusCode.WAIT;

    public static TaskEvent of(Long mongId, TaskCode taskCode, LocalDateTime createdAt, Long expiration) {
        LocalDateTime expiredAt = createdAt.plusSeconds(expiration);

        return TaskEvent.builder()
                .taskCode(taskCode)
                .mongId(mongId)
                .expiration(expiration)
                .expiredAt(expiredAt)
                .createdAt(createdAt)
                .statusCode(TaskStatusCode.WAIT)
                .build();
    }
}
