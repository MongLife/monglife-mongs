package com.mongs.auth.service;

import com.mongs.auth.dto.response.LoginResDto;
import com.mongs.auth.dto.response.PassportResDto;
import com.mongs.auth.dto.response.ReissueResDto;
import com.mongs.auth.entity.Member;
import com.mongs.auth.entity.Token;
import com.mongs.auth.exception.AuthorizationException;
import com.mongs.auth.exception.NotFoundException;
import com.mongs.auth.exception.PassportException;
import com.mongs.auth.repository.MemberRepository;
import com.mongs.auth.repository.TokenRepository;
import com.mongs.auth.util.HmacProvider;
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
    @Mock
    private HmacProvider hmacProvider;

    @Value("${application.security.jwt.refresh-expiration}")
    private Long expiration;

    @Test
    @DisplayName("deviceId, email, name 로 로그인 후 accessToken 과 refreshToken 을 반환 한다.")
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
        when(tokenRepository.findTokenByDeviceIdAndMemberId(deviceId, memberId))
                .thenReturn(Optional.of(Token.builder()
                        .refreshToken(refreshToken)
                        .deviceId(deviceId)
                        .memberId(memberId)
                        .createdAt(LocalDateTime.now())
                        .expiration(1L)
                        .build()));
        when(tokenRepository.save(any()))
                .thenReturn(Token.builder()
                        .refreshToken(refreshToken)
                        .deviceId(deviceId)
                        .memberId(memberId)
                        .createdAt(LocalDateTime.now())
                        .expiration(expiration)
                        .build());

        // when
        LoginResDto result = authService.login(deviceId, email, name);

        // then
        assertThat(result.accessToken()).isEqualTo(accessToken);
        assertThat(result.refreshToken()).isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("refreshToken 이 Redis 에 존재하는 경우 새로운 accessToken 과 refreshToken 을 반환 한다.")
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

    @Test
    @DisplayName("accessToken 으로 passport 정보를 반환한다.")
    void passport() throws Exception {
        // given
        String passportIntegrity = "test-passportIntegrity";
        String accessToken = "test-accessToken";
        String email = "test@test.com";
        String name = "테스트";
        Long memberId = 1L;

        when(tokenProvider.isTokenExpired(accessToken))
                .thenReturn(false);
        when(tokenProvider.getMemberId(accessToken))
                .thenReturn(memberId);
        when(memberRepository.findById(memberId))
                .thenReturn(Optional.of(Member.builder()
                        .id(memberId)
                        .email(email)
                        .name(name)
                        .build()));
        when(hmacProvider.generateHmac(any()))
                .thenReturn(passportIntegrity);

        // when
        PassportResDto passportResDto = authService.passport(accessToken);
        String expected = passportResDto.passportIntegrity();

        // then
        assertThat(expected).isEqualTo(passportIntegrity);
    }

    @Test
    @DisplayName("만료된 accessToken 으로 passport 정보를 조회하면 AuthorizationException 을 발생 시킨다.")
    void passportAccessTokenExpired() {
        // given
        String accessToken = "test-accessToken-expired";

        when(tokenProvider.isTokenExpired(accessToken))
                .thenReturn(true);

        // when
        Throwable expected = catchThrowable(() -> authService.passport(accessToken));

        // then
        assertThat(expected).isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("accessToken 으로 passport 정보를 조회할 때, 회원 정보가 없으면 NotFoundException 을 발생 시킨다.")
    void passportNotFoundMember() {
        // given
        String accessToken = "test-accessToken";
        Long memberId = 1L;

        when(tokenProvider.isTokenExpired(accessToken))
                .thenReturn(false);
        when(tokenProvider.getMemberId(accessToken))
                .thenReturn(memberId);
        when(memberRepository.findById(memberId))
                .thenReturn(Optional.empty());

        // when
        Throwable expected = catchThrowable(() -> authService.passport(accessToken));

        // then
        assertThat(expected).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("passportIntegrity 을 생성에 실패하면 PassportException 을 발생시킨다.")
    void passportRegisterFail() throws Exception {
        // given
        String accessToken = "test-accessToken";
        String email = "test@test.com";
        String name = "테스트";
        Long memberId = 1L;

        when(tokenProvider.isTokenExpired(accessToken))
                .thenReturn(false);
        when(tokenProvider.getMemberId(accessToken))
                .thenReturn(memberId);
        when(memberRepository.findById(memberId))
                .thenReturn(Optional.of(Member.builder()
                        .id(memberId)
                        .email(email)
                        .name(name)
                        .build()));
        when(hmacProvider.generateHmac(any()))
                .thenThrow(Exception.class);

        // when
        Throwable expected = catchThrowable(() -> authService.passport(accessToken));

        // then
        assertThat(expected).isInstanceOf(PassportException.class);
    }
}
