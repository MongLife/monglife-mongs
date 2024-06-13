package com.mongs.play.domain.session.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@RedisHash("session")
public class Session {
    @Id
    private String refreshToken;
    @Indexed
    private String deviceId;
    @Indexed
    private Long accountId;
    private LocalDateTime createdAt;
    @TimeToLive
    private Long expiration;
}
