package com.mongs.play.module.task.entity;

import com.mongs.play.module.task.enums.TaskCode;
import com.mongs.play.module.task.enums.TaskStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@Document(collection = "task_event")
public class TaskEvent {
    private static final Random random = new Random();

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
        long expiration = 1000 * (taskCode.getExpiration() != 0L ? taskCode.getExpiration() : random.nextLong(60 * 60 * 4, 60 * 60 * 6));
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
