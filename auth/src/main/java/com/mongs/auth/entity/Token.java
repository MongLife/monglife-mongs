package com.mongs.auth.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@RedisHash("token")
public class Token {
    @Id
    private String refreshToken;
    private String deviceId;
    private Long memberId;
    private LocalDateTime createdAt;
    @TimeToLive
    private Long expiration;
}
