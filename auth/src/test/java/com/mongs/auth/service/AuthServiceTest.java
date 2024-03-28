package com.mongs.auth.service;

import com.mongs.auth.entity.Account;
import com.mongs.auth.entity.AccountLog;
import com.mongs.auth.entity.Token;
import com.mongs.auth.exception.AuthorizationException;
import com.mongs.auth.exception.NotFoundException;
import com.mongs.auth.exception.PassportException;
import com.mongs.auth.repository.AccountLogRepository;
import com.mongs.auth.repository.AccountRepository;
import com.mongs.auth.repository.TokenRepository;
import com.mongs.core.utils.HmacProvider;
import com.mongs.core.utils.TokenProvider;
import com.mongs.core.vo.passport.PassportVo;
import com.mongs.auth.service.vo.LoginVo;
import com.mongs.auth.service.vo.ReissueVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
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
    private AccountRepository accountRepository;
    @Mock
    private AccountLogRepository accountLogRepository;
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
        Long accountId = 1L;
        String accessToken = "test-accessToken";
        String refreshToken = "test-refreshToken";

        when(accountRepository.findByEmailAndIsDeletedFalse(email))
                .thenReturn(Optional.of(Account.builder()
                        .id(accountId)
                        .email(email)
                        .name(name)
                        .build()));
        when(tokenProvider.generateAccessToken(accountId, deviceId))
                .thenReturn(accessToken);
        when(tokenProvider.generateRefreshToken())
                .thenReturn(refreshToken);
        when(tokenRepository.findTokenByDeviceIdAndAccountId(deviceId, accountId))
                .thenReturn(Optional.of(Token.builder()
                        .refreshToken(refreshToken)
                        .deviceId(deviceId)
                        .accountId(accountId)
                        .createdAt(LocalDateTime.now())
                        .expiration(1L)
                        .build()));
        when(tokenRepository.save(any()))
                .thenReturn(Token.builder()
                        .refreshToken(refreshToken)
                        .deviceId(deviceId)
                        .accountId(accountId)
                        .createdAt(LocalDateTime.now())
                        .expiration(expiration)
                        .build());

        // when
        LoginVo result = authService.login(deviceId, email, name);

        // then
        assertThat(result.accessToken()).isEqualTo(accessToken);
        assertThat(result.refreshToken()).isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("refreshToken 이 Redis 에 존재하는 경우 새로운 accessToken 과 refreshToken 을 반환 한다.")
    void reissue() {
        // given
        String deviceId = "test-deviceId";
        Long accountId = 1L;
        String refreshToken = "test-refreshToken";
        String newAccessToken = "test-new-accessToken";
        String newRefreshToken = "test-new-refreshToken";

        when(tokenRepository.findById(refreshToken))
                .thenReturn(Optional.of(Token.builder()
                        .refreshToken(refreshToken)
                        .deviceId(deviceId)
                        .accountId(accountId)
                        .createdAt(LocalDateTime.now())
                        .expiration(expiration)
                        .build()));
        when(tokenProvider.generateAccessToken(accountId, deviceId))
                .thenReturn(newAccessToken);
        when(tokenProvider.generateRefreshToken())
                .thenReturn(newRefreshToken);
        when(tokenRepository.save(any()))
                .thenReturn(Token.builder()
                        .refreshToken(newRefreshToken)
                        .deviceId(deviceId)
                        .accountId(accountId)
                        .createdAt(LocalDateTime.now())
                        .expiration(expiration)
                        .build());

        // when
        ReissueVo result = authService.reissue(refreshToken);

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
    void passport() {
        // given
        String passportIntegrity = "test-passportIntegrity";
        String accessToken = "test-accessToken";
        String email = "test@test.com";
        String name = "테스트";
        String deviceId = "testDeviceId";
        Long accountId = 1L;
        Long accountLogId = 1L;

        when(tokenProvider.isTokenExpired(accessToken))
                .thenReturn(false);
        when(tokenProvider.getMemberId(accessToken))
                .thenReturn(Optional.of(accountId));
        when(tokenProvider.getDeviceId(accessToken))
                .thenReturn(Optional.of(deviceId));
        when(accountRepository.findByIdAndIsDeletedFalse(accountId))
                .thenReturn(Optional.of(Account.builder()
                        .id(accountId)
                        .email(email)
                        .name(name)
                        .build()));
        when(accountLogRepository.findTopByAccountIdAndDeviceIdOrderByLoginAt(accountId, deviceId))
                .thenReturn(Optional.of(AccountLog.builder()
                        .id(accountLogId)
                        .accountId(accountId)
                        .deviceId(deviceId)
                        .loginAt(LocalDate.now())
                        .loginCount(1)
                        .build()));
        when(hmacProvider.generateHmac(any()))
                .thenReturn(Optional.of(passportIntegrity));

        // when
        PassportVo passportVO = authService.passport(accessToken);
        String expected = passportVO.passportIntegrity();

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
        String deviceId = "testDeviceId";
        Long accountId = 1L;

        when(tokenProvider.isTokenExpired(accessToken))
                .thenReturn(false);
        when(tokenProvider.getMemberId(accessToken))
                .thenReturn(Optional.of(accountId));
        when(tokenProvider.getDeviceId(accessToken))
                .thenReturn(Optional.of(deviceId));
        when(accountRepository.findByIdAndIsDeletedFalse(accountId))
                .thenReturn(Optional.empty());

        // when
        Throwable expected = catchThrowable(() -> authService.passport(accessToken));

        // then
        assertThat(expected).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("passportIntegrity 을 생성에 실패하면 PassportException 을 발생시킨다.")
    void passportRegisterFail() {
        // given
        String accessToken = "test-accessToken";
        String email = "test@test.com";
        String name = "테스트";
        String deviceId = "testDeviceId";
        Long accountId = 1L;
        Long accountLogId = 1L;

        when(tokenProvider.isTokenExpired(accessToken))
                .thenReturn(false);
        when(tokenProvider.getMemberId(accessToken))
                .thenReturn(Optional.of(accountId));
        when(tokenProvider.getDeviceId(accessToken))
                .thenReturn(Optional.of(deviceId));
        when(accountRepository.findByIdAndIsDeletedFalse(accountId))
                .thenReturn(Optional.of(Account.builder()
                        .id(accountId)
                        .email(email)
                        .name(name)
                        .build()));
        when(accountLogRepository.findTopByAccountIdAndDeviceIdOrderByLoginAt(accountId, deviceId))
                .thenReturn(Optional.of(AccountLog.builder()
                        .id(accountLogId)
                        .accountId(accountId)
                        .deviceId(deviceId)
                        .loginAt(LocalDate.now())
                        .loginCount(1)
                        .build()));
        when(hmacProvider.generateHmac(any()))
                .thenReturn(Optional.empty());

        // when
        Throwable expected = catchThrowable(() -> authService.passport(accessToken));

        // then
        assertThat(expected).isInstanceOf(PassportException.class);
    }
}
