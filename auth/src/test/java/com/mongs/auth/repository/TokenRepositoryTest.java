package com.mongs.auth.repository;

import com.mongs.auth.config.RedisContainer;
import com.mongs.auth.entity.Token;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;
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

    @Test
    @DisplayName("토큰을 저장하면 expired 시간 이후에 소멸된다.")
    void autoCreatedAt() {
        // given
        String deviceId = "test-deviceId";
        Long memberId = 1L;
        String refreshToken = "test-refreshToken";
        Token token = Token.builder()
                .refreshToken(refreshToken)
                .deviceId(deviceId)
                .memberId(memberId)
                .createdAt(LocalDateTime.now())
                .expiration(expiration)
                .build();

        // when
        tokenRepository.save(token);
        boolean expected1 = tokenRepository.findById(refreshToken).isPresent();
        Awaitility.await().pollDelay(Duration.ofSeconds(expiration)).until(() -> true);
        boolean expected2 = tokenRepository.findById(refreshToken).isPresent();

        // then
        assertThat(expected1).isTrue();
        assertThat(expected2).isFalse();
    }

    @Test
    @DisplayName("토큰 삭제 시, 즉시 소멸 된다.")
    void delete() {
        // given
        String deviceId = "test-deviceId";
        Long memberId = 1L;
        String refreshToken = "test-refreshToken";
        Token token = Token.builder()
                .refreshToken(refreshToken)
                .deviceId(deviceId)
                .memberId(memberId)
                .createdAt(LocalDateTime.now())
                .expiration(expiration)
                .build();
        tokenRepository.save(token);

        // when
        boolean expected1 = tokenRepository.findById(refreshToken).isPresent();
        tokenRepository.deleteById(refreshToken);
        boolean expected2 = tokenRepository.findById(refreshToken).isPresent();

        // then
        assertThat(expected1).isTrue();
        assertThat(expected2).isFalse();
    }

    @Nested
    public class Find {
        String deviceId = "test-deviceId";
        Long memberId = 1L;
        String refreshToken = "test-refreshToken";
        Long expiration = 1000000L;

        @BeforeEach
        void beforeEach() {
            Token token = Token.builder()
                    .refreshToken(refreshToken)
                    .deviceId(deviceId)
                    .memberId(memberId)
                    .createdAt(LocalDateTime.now())
                    .expiration(expiration)
                    .build();
            tokenRepository.save(token);
        }
        @AfterEach
        void after() {
            tokenRepository.deleteById(refreshToken);
        }

        @Test
        @DisplayName("deviceId, memberId 로 토큰을 조회한다.")
        void findByDeviceIdAndMemberId() {
            // given
            String deviceId = "test-deviceId";
            Long memberId = 1L;

            // when
            Token token = tokenRepository.findTokenByDeviceIdAndMemberId(deviceId, memberId)
                    .orElseGet(null);

            // then
            assertThat(token).isNotNull();
            assertThat(token.getRefreshToken()).isEqualTo(refreshToken);
            assertThat(token.getDeviceId()).isEqualTo(deviceId);
            assertThat(token.getMemberId()).isEqualTo(memberId);
        }
    }
}
