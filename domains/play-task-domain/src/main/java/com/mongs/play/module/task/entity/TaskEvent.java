package com.mongs.play.module.task.entity;

import com.mongs.play.module.task.enums.TaskCode;
import com.mongs.play.module.task.enums.TaskStatus;
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
    @Builder.Default
    private TaskStatus taskStatus = TaskStatus.WAIT;
    private TaskCode taskCode;
    private Long mongId;
    private Long expiration;
    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;

    public static TaskEvent of(Long mongId, TaskCode taskCode) {
        Long expiration = taskCode.getExpiration();
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
