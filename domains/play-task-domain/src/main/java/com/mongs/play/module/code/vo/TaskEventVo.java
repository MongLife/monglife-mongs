package com.mongs.play.module.code.vo;

import com.mongs.play.module.code.entity.TaskEvent;
import com.mongs.play.module.code.enums.TaskCode;
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
