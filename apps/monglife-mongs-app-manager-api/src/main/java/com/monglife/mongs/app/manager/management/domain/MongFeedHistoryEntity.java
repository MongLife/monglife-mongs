package com.monglife.mongs.app.manager.management.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Builder
@AllArgsConstructor
@RedisHash("mong_feed_history")
public class MongFeedHistoryEntity {

    @Id
    private String mongFeedHistoryId;

    @Indexed
    private Long mongId;

    @Indexed
    private String foodTypeCode;

    @TimeToLive
    private Long expiration;
}
