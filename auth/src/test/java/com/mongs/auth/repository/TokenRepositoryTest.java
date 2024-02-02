package com.mongs.auth.repository;

import com.mongs.auth.config.RedisContainer;
import com.mongs.auth.entity.Token;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class TokenRepositoryTest extends RedisContainer {
    @Autowired
    private TokenRepository tokenRepository;

    @Value("${application.security.jwt.refresh-expiration}")
    private Long expiration;

    private final String deviceId = "test-deviceId";
    private final String refreshToken = "test-refreshToken";

    @Test
    @DisplayName("토큰을 저장하면 expired 시간 이후에 소멸된다.")
    void autoCreatedAt() {
        // given
        Token token = Token.builder()
                .deviceId(deviceId)
                .refreshToken(refreshToken)
                .createdAt(LocalDateTime.now())
                .expiration(expiration)
                .build();

        // when
        tokenRepository.save(token);
        boolean expected1 = tokenRepository.findById(deviceId).isPresent();
        Awaitility.await().pollDelay(Duration.ofSeconds(expiration)).until(() -> true);
        boolean expected2 = tokenRepository.findById(deviceId).isPresent();

        // then
        assertThat(expected1).isTrue();
        assertThat(expected2).isFalse();
    }
}
