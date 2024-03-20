package com.mongs.auth.service;

import com.mongs.auth.entity.AccountLog;
import com.mongs.auth.exception.AuthorizationException;
import com.mongs.auth.exception.AuthErrorCode;
import com.mongs.auth.repository.AccountLogRepository;
import com.mongs.auth.repository.TokenRepository;
import com.mongs.auth.entity.Account;
import com.mongs.auth.entity.Token;
import com.mongs.auth.exception.NotFoundException;
import com.mongs.auth.exception.PassportException;
import com.mongs.core.vo.passport.PassportVO;
import com.mongs.core.vo.passport.PassportData;
import com.mongs.core.vo.passport.PassportAccount;
import com.mongs.auth.repository.AccountRepository;
import com.mongs.core.utils.HmacProvider;
import com.mongs.core.utils.TokenProvider;
import com.mongs.auth.service.vo.LoginVo;
import com.mongs.auth.service.vo.LogoutVo;
import com.mongs.auth.service.vo.ReissueVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AccountRepository accountRepository;
    private final AccountLogRepository accountLogRepository;
    private final TokenRepository tokenRepository;
    private final TokenProvider tokenProvider;
    private final HmacProvider hmacProvider;

    private final MemberService memberService;

    @Value("${application.security.jwt.refresh-expiration}")
    private Long expiration;

    private Account registerAccount(String email, String name) throws RuntimeException {
        Account registerAccount = accountRepository.save(Account.builder()
                .name(name)
                .email(email)
                .build());

        memberService.registerMember(registerAccount.getId())
                .orElseThrow(() -> new NotFoundException(AuthErrorCode.REGISTER_MEMBER_FAIL));

        return registerAccount;
    }

    @Transactional
    public LoginVo login(String deviceId, String email, String name) throws RuntimeException {
        /* 회원 가입 (회원 정보가 없는 경우) */
        Account account = accountRepository.findByEmailAndIsDeletedFalse(email)
                .orElseGet(() -> this.registerAccount(email, name));

        /* 이전 RefreshToken 삭제 */
        tokenRepository.findTokenByDeviceIdAndAccountId(deviceId, account.getId())
                .ifPresent(token -> tokenRepository.deleteById(token.getRefreshToken()));

        /* AccessToken 및 RefreshToken 발급 */
        Token token = Token.builder()
                .refreshToken(tokenProvider.generateRefreshToken())
                .deviceId(deviceId)
                .accountId(account.getId())
                .createdAt(LocalDateTime.now())
                .expiration(expiration)
                .build();
        token = tokenRepository.save(token);

        LocalDate today = LocalDate.now();
        AccountLog accountLog = accountLogRepository.findByAccountIdAndDeviceIdAndLoginAt(account.getId(), deviceId, today)
                                    .orElseGet(() -> AccountLog.builder()
                                            .accountId(account.getId())
                                            .deviceId(deviceId)
                                            .loginAt(today)
                                            .build());

        accountLogRepository.save(accountLog.toBuilder()
                .loginCount(accountLog.getLoginCount() + 1)
                .build());

        return LoginVo.builder()
                .accessToken(tokenProvider.generateAccessToken(token.getAccountId(), token.getDeviceId()))
                .refreshToken(token.getRefreshToken())
                .build();
    }

    @Transactional
    public LogoutVo logout(String refreshToken) throws RuntimeException {
        Token token = tokenRepository.findById(refreshToken)
                .orElseThrow(() -> new AuthorizationException(AuthErrorCode.REFRESH_TOKEN_EXPIRED));

        tokenRepository.deleteById(token.getRefreshToken());

        return LogoutVo.builder()
                .accountId(token.getAccountId())
                .build();
    }

    @Transactional
    public ReissueVo reissue(String refreshToken) throws RuntimeException {
        /* RefreshToken Redis 존재 여부 확인 */
        Token token = tokenRepository.findById(refreshToken)
                .orElseThrow(() -> new AuthorizationException(AuthErrorCode.REFRESH_TOKEN_EXPIRED));

        tokenRepository.deleteById(refreshToken);

        /* AccessToken 및 RefreshToken 발급 */
        Token newToken = Token.builder()
                .refreshToken(tokenProvider.generateRefreshToken())
                .deviceId(token.getDeviceId())
                .accountId(token.getAccountId())
                .createdAt(LocalDateTime.now())
                .expiration(expiration)
                .build();
        newToken = tokenRepository.save(newToken);

        return ReissueVo.builder()
                .accessToken(tokenProvider.generateAccessToken(newToken.getAccountId(), newToken.getDeviceId()))
                .refreshToken(newToken.getRefreshToken())
                .build();
    }

    @Transactional
    public PassportVO passport(String accessToken) throws RuntimeException {
        /* AccessToken 검증 */
        if (tokenProvider.isTokenExpired(accessToken)) {
            throw new AuthorizationException(AuthErrorCode.ACCESS_TOKEN_EXPIRED);
        }

        Long accountId = tokenProvider.getMemberId(accessToken)
                .orElseThrow(() -> new AuthorizationException(AuthErrorCode.ACCESS_TOKEN_EXPIRED));
        String deviceId = tokenProvider.getDeviceId(accessToken)
                .orElseThrow(() -> new AuthorizationException(AuthErrorCode.ACCESS_TOKEN_EXPIRED));

        /* AccessToken 의 accountId 로 account 조회 */
        Account account = accountRepository.findByIdAndIsDeletedFalse(accountId)
                .orElseThrow(() -> new NotFoundException(AuthErrorCode.ACCOUNT_NOT_FOUND));

        AccountLog accountLog = accountLogRepository.findTopByAccountIdAndDeviceIdOrderByLoginAt(accountId, deviceId)
                .orElseThrow(() -> new NotFoundException(AuthErrorCode.ACCOUNT_LOG_NOT_FOUND));

        PassportVO passportVO = PassportVO.builder()
                .data(PassportData.builder()
                        .account(PassportAccount.builder()
                                .id(accountId)
                                .deviceId(deviceId)
                                .email(account.getEmail())
                                .name(account.getName())
                                .loginAt(accountLog.getLoginAt())
                                .role(account.getRole())
                                .build())
                        .build())
                .build();

        String passportIntegrity = hmacProvider.generateHmac(passportVO.data())
                .orElseThrow(() -> new PassportException(AuthErrorCode.PASSPORT_GENERATE_FAIL));

        /* passport 생성 및 dto 반환 */
        return passportVO.toBuilder()
                .passportIntegrity(passportIntegrity)
                .build();
    }
}
