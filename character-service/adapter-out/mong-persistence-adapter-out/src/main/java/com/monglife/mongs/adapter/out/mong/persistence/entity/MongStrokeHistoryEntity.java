package com.monglife.mongs.adapter.out.mong.persistence.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@Getter
@Builder
@RedisHash("mongs_stroke_history")
public class MongStrokeHistoryEntity {

    @Id
    private String mongStrokeHistoryId;

    @Indexed
    private Long mongId;

    private LocalDateTime strokedAt;

    @TimeToLive
    private Long expiration;

    @Builder
    public MongStrokeHistoryEntity(String mongStrokeHistoryId, Long mongId, LocalDateTime strokedAt, Long expiration) {
        this.mongStrokeHistoryId = mongStrokeHistoryId;
        this.mongId = mongId;
        this.strokedAt = strokedAt;
        this.expiration = expiration;
    }
}
