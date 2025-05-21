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
@RedisHash("mongs_feed_history")
public class MongFeedHistoryEntity {

    @Id
    private String mongFeedHistoryId;

    @Indexed
    private Long mongId;

    @Indexed
    private String typeCode;

    private LocalDateTime buyAt;

    @TimeToLive
    private Long expiration;

    @Builder
    public MongFeedHistoryEntity(String mongFeedHistoryId, Long mongId, String typeCode, LocalDateTime buyAt, Long expiration) {
        this.mongFeedHistoryId = mongFeedHistoryId;
        this.mongId = mongId;
        this.typeCode = typeCode;
        this.buyAt = buyAt;
        this.expiration = expiration;
    }
}
