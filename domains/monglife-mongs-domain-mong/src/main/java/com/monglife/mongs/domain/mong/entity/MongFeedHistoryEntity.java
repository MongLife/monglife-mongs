package com.monglife.mongs.domain.mong.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@RedisHash("mongs_feed_history")
public class MongFeedHistoryEntity {

    @Id
    private String mongFeedHistoryId;

    @Indexed
    private Long mongId;

    @Indexed
    private String foodTypeCode;

    private LocalDateTime buyAt;

    @TimeToLive
    private Long expiration;
}
