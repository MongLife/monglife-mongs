package com.mongs.auth.util;

import com.mongs.core.utils.TokenProvider;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;

    @Value("${application.security.jwt.access-expiration}")
    private Long access_expiration;

    @Value("${application.security.jwt.refresh-expiration}")
    private Long refresh_expiration;

    @Test
    @DisplayName("Token 의 만료 여부를 확인한다. (오차 범위 1s)")
    void verifyTokenExpired() {
        // given
        String refreshToken = tokenProvider.generateRefreshToken();

        // when
        Long expiration = tokenProvider.getExpiredSeconds(refreshToken)
                .orElse(-1L);
        Awaitility.await().pollDelay(Duration.ofSeconds(expiration + 1L)).until(() -> true);
        boolean expected = tokenProvider.isTokenExpired(refreshToken);

        // then
        assertThat(expiration).isNotEqualTo(-1L);
        assertThat(expected).isTrue();
    }

    @Test
    @DisplayName("Access Token 의 payload 에 accountId, deviceId 를 저장 한다.")
    void registerMemberIdAndDeviceId() {
        // given
        Long accountId = 1L;
        String deviceId = "test-deviceId";
        String accessToken = tokenProvider.generateAccessToken(accountId, deviceId);

        // when
        Long tokenMemberId = tokenProvider.getMemberId(accessToken)
                .orElse(null);
        String tokenDeviceId = tokenProvider.getDeviceId(accessToken)
                .orElse(null);

        // then
        assertThat(tokenMemberId).isNotNull();
        assertThat(tokenMemberId).isEqualTo(accountId);
        assertThat(tokenDeviceId).isNotNull();
        assertThat(tokenDeviceId).isEqualTo(deviceId);
    }

    @Test
    @DisplayName("AccessToken 의 만료 시간이 access_expiration 와 일치 여부를 확인 한다. (오차 5s)")
    void accessTokenExpiration() {
        // given
        Long accountId = 1L;
        String deviceId = "test-deviceId";
        String accessToken = tokenProvider.generateAccessToken(accountId, deviceId);

        // when
        Long expiration = tokenProvider.getExpiredSeconds(accessToken)
                .orElse(-1L);
        Long expected1 = access_expiration - 5;
        Long expected2 = access_expiration;

        // then
        assertThat(expiration).isNotEqualTo(-1L);
        assertThat(expiration).isGreaterThan(expected1);
        assertThat(expiration).isLessThan(expected2);
    }

    @Test
    @DisplayName("RefreshToken 의 만료 시간이 refresh_expiration 와 일치 여부를 확인 한다. (오차 5s)")
    void refreshTokenExpiration() {
        // given
        String refreshToken = tokenProvider.generateRefreshToken();

        // when
        Long expiration = tokenProvider.getExpiredSeconds(refreshToken)
                .orElse(-1L);
        Long expected1 = refresh_expiration - 5;
        Long expected2 = refresh_expiration;

        // then
        assertThat(expiration).isNotEqualTo(-1L);
        assertThat(expiration).isGreaterThan(expected1);
        assertThat(expiration).isLessThan(expected2);
    }
}
