package com.mongs.lifecycle.vo;

import com.mongs.lifecycle.code.TaskCode;
import com.mongs.lifecycle.entity.TaskEvent;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskEventVo(
        String taskId,
        TaskCode taskCode,
        Long mongId,
        Long expiration,
        LocalDateTime expiredAt,
        LocalDateTime createdAt
) {
    public static TaskEventVo of(TaskEvent taskEvent) {
        return TaskEventVo.builder()
                .taskId(taskEvent.getTaskId())
                .taskCode(taskEvent.getTaskCode())
                .mongId(taskEvent.getMongId())
                .expiration(taskEvent.getExpiration())
                .expiredAt(taskEvent.getExpiredAt())
                .createdAt(taskEvent.getCreatedAt())
                .build();
    }
}
