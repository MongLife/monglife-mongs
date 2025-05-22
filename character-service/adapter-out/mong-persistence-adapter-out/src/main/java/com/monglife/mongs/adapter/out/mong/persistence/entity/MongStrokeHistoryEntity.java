package com.monglife.mongs.adapter.out.mong.persistence.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@Getter
@Builder
@RedisHash("mongs_stroke_history")
@ToString
public class MongStrokeHistoryEntity {

    @Id
    private String mongStrokeHistoryId;

    @Indexed
    private Long mongId;

    private LocalDateTime strokeAt;

    @TimeToLive
    private Long expiration;

    @Builder
    public MongStrokeHistoryEntity(String mongStrokeHistoryId, Long mongId, LocalDateTime strokeAt, Long expiration) {
        this.mongStrokeHistoryId = mongStrokeHistoryId;
        this.mongId = mongId;
        this.strokeAt = strokeAt;
        this.expiration = expiration;
    }
}
