package com.monglife.mongs.domain.mong.entity.history;

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
@RedisHash("mongs_stroke_history")
public class MongStrokeHistoryEntity {

    @Id
    private String mongStrokeHistoryId;

    @Indexed
    private Long mongId;

    private LocalDateTime strokeAt;

    @TimeToLive
    private Long expiration;
}
