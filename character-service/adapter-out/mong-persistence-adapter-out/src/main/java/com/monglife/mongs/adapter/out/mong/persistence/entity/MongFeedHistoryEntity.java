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
    private String code;

    private LocalDateTime boughtAt;

    @TimeToLive
    private Long expiration;

    @Builder
    public MongFeedHistoryEntity(String mongFeedHistoryId, Long mongId, String code, LocalDateTime boughtAt, Long expiration) {
        this.mongFeedHistoryId = mongFeedHistoryId;
        this.mongId = mongId;
        this.code = code;
        this.boughtAt = boughtAt;
        this.expiration = expiration;
    }
}
