package com.mongs.auth.service;

import com.mongs.auth.dto.response.LoginResDto;
import com.mongs.auth.dto.response.ReissueResDto;
import com.mongs.auth.entity.Member;
import com.mongs.auth.entity.Token;
import com.mongs.auth.exception.AuthorizationException;
import com.mongs.auth.repository.MemberRepository;
import com.mongs.auth.repository.TokenRepository;
import com.mongs.auth.util.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private TokenProvider tokenProvider;

    @Value("${application.security.jwt.refresh-expiration}")
    private Long expiration;

    @Test
    @DisplayName("deviceId, email, name 로 로그인하고 accessToken 과 refreshToken 을 반환 한다.")
    void login() {
        // given
        String deviceId = "test-deviceId";
        String email = "test@test.com";
        String name = "테스트";
        Long memberId = 1L;
        String accessToken = "test-accessToken";
        String refreshToken = "test-refreshToken";

        when(memberRepository.findByEmail(email))
                .thenReturn(Optional.of(Member.builder()
                        .id(memberId)
                        .email(email)
                        .name(name)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()));
        when(tokenProvider.generateAccessToken(memberId, deviceId))
                .thenReturn(accessToken);
        when(tokenProvider.generateRefreshToken())
                .thenReturn(refreshToken);
        when(tokenRepository.save(any()))
                .thenReturn(Token.builder()
                        .refreshToken(refreshToken)
                        .deviceId(deviceId)
                        .memberId(memberId)
                        .createdAt(LocalDateTime.now())
                        .expiration(1L)
                        .build());

        // when
        LoginResDto result = authService.login(deviceId, email, name);

        // then
        assertThat(result.accessToken()).isEqualTo(accessToken);
        assertThat(result.refreshToken()).isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("refreshToken 이 Redis 에 존재하면 새로운 accessToken 과 refreshToken 을 반환 한다.")
    void reissue() {
        // given
        String deviceId = "test-deviceId";
        Long memberId = 1L;
        String refreshToken = "test-refreshToken";
        String newAccessToken = "test-new-accessToken";
        String newRefreshToken = "test-new-refreshToken";

        when(tokenRepository.findById(refreshToken))
                .thenReturn(Optional.of(Token.builder()
                        .refreshToken(refreshToken)
                        .deviceId(deviceId)
                        .memberId(memberId)
                        .createdAt(LocalDateTime.now())
                        .expiration(expiration)
                        .build()));
        when(tokenProvider.generateAccessToken(memberId, deviceId))
                .thenReturn(newAccessToken);
        when(tokenProvider.generateRefreshToken())
                .thenReturn(newRefreshToken);
        when(tokenRepository.save(any()))
                .thenReturn(Token.builder()
                        .refreshToken(newRefreshToken)
                        .deviceId(deviceId)
                        .memberId(memberId)
                        .createdAt(LocalDateTime.now())
                        .expiration(expiration)
                        .build());

        // when
        ReissueResDto result = authService.reissue(refreshToken);

        // then
        assertThat(result.accessToken()).isEqualTo(newAccessToken);
        assertThat(result.refreshToken()).isEqualTo(newRefreshToken);
    }

    @Test
    @DisplayName("refreshToken 이 Redis 에 존재하지 않으면 AuthorizationException 을 발생 시킨다.")
    void reissueNotExistInRedis() {
        // given
        String refreshToken = "test-refreshToken";

        when(tokenRepository.findById(refreshToken))
                .thenReturn(Optional.empty());

        // when
        Throwable expected = catchThrowable(() -> authService.reissue(refreshToken));

        // then
        assertThat(expected).isInstanceOf(AuthorizationException.class);
    }
}
