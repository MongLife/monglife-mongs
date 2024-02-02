package com.mongs.auth.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
    @DisplayName("Access Token 의 payload 에 memberId, deviceId 를 저장 한다.")
    void registerMemberIdAndDeviceId() {
        // given
        Long memberId = 1L;
        String deviceId = "test-deviceId";
        String accessToken = tokenProvider.generateAccessToken(memberId, deviceId);

        // when
        Long tokenMemberId = tokenProvider.getMemberId(accessToken);
        String tokenDeviceId = tokenProvider.getDeviceId(accessToken);

        // then
        assertThat(tokenMemberId).isNotNull();
        assertThat(tokenMemberId).isEqualTo(memberId);
        assertThat(tokenDeviceId).isNotNull();
        assertThat(tokenDeviceId).isEqualTo(deviceId);
    }

    @Test
    @DisplayName("AccessToken 의 만료 시간이 access_expiration 와 일치 여부를 확인 한다. (오차 5s)")
    void accessTokenExpiration() {
        // given
        Long memberId = 1L;
        String deviceId = "test-deviceId";
        String accessToken = tokenProvider.generateAccessToken(memberId, deviceId);

        // when
        Long expiration = tokenProvider.getExpiredSeconds(accessToken);
        Long expected1 = access_expiration - 5;
        Long expected2 = access_expiration;

        // then
        assertThat(expiration).isGreaterThan(expected1);
        assertThat(expiration).isLessThan(expected2);
    }

    @Test
    @DisplayName("RefreshToken 의 만료 시간이 refresh_expiration 와 일치 여부를 확인 한다. (오차 5s)")
    void refreshTokenExpiration() {
        // given
        String refreshToken = tokenProvider.generateRefreshToken();

        // when
        Long expiration = tokenProvider.getExpiredSeconds(refreshToken);
        Long expected1 = refresh_expiration - 5;
        Long expected2 = refresh_expiration;

        // then
        assertThat(expiration).isGreaterThan(expected1);
        assertThat(expiration).isLessThan(expected2);
    }
}
