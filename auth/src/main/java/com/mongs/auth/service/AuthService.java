package com.mongs.auth.service;

import com.mongs.auth.entity.AccountLog;
import com.mongs.auth.exception.*;
import com.mongs.auth.repository.AccountLogRepository;
import com.mongs.auth.repository.TokenRepository;
import com.mongs.auth.entity.Account;
import com.mongs.auth.entity.Token;
import com.mongs.core.vo.passport.PassportVo;
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

    @Value("${application.security.jwt.refresh-expiration}")
    private Long expiration;

    /**
     * 계정 등록 (Account)
     *
     * @param email 이메일
     * @param name 이름
     * @return Account 생성에 성공하면 저장한 {@link Account}를 반환한다.
     */
    private Account registerAccount(String email, String name) {
        return accountRepository.save(Account.builder()
                .name(name)
                .email(email)
                .build());
    }

    /**
     * 로그인
     * 계정 정보가 없는 경우 생성한다.
     * 발급한 RefreshToken 을 redis 에 저장하는데 기존에 있는 RefreshToken 은 삭제한다.
     * 로그인한 내역을 저장한다.
     *
     * @param deviceId 기기 ID
     * @param email 이메일
     * @param name 이름
     * @return 로그인에 성공하면 {@link LoginVo}를 반환한다.
     */
    @Transactional
    public LoginVo login(String deviceId, String email, String name) {
        /* 회원 가입 (계정 정보가 없는 경우) */
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
        
        /* 로그인 로그 저장 */
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
                .accountId(account.getId())
                .accessToken(tokenProvider.generateAccessToken(token.getAccountId(), token.getDeviceId()))
                .refreshToken(token.getRefreshToken())
                .build();
    }

    /**
     * 로그아웃
     * 기존에 저장되어 있는 RefreshToken 을 삭제한다.
     *
     * @param refreshToken 리프래시 토큰
     * @return Account 생성에 성공하면 저장한 {@link Account}를 반환한다.
     */
    @Transactional
    public LogoutVo logout(String refreshToken) throws RuntimeException {
        Token token = tokenRepository.findById(refreshToken)
                .orElseThrow(() -> new AuthorizationException(AuthErrorCode.REFRESH_TOKEN_EXPIRED));

        tokenRepository.deleteById(token.getRefreshToken());

        return LogoutVo.builder()
                .accountId(token.getAccountId())
                .build();
    }

    /**
     * 토큰 재발급
     * redis 에 RefreshToken 존재 여부(만료 여부)를 확인 후 재발급을 진행한다.
     * 
     * @param refreshToken 리프래시 토큰
     * @return 토큰 재발급에 성공하면 {@link ReissueVo}를 반환한다.
     * @throws AuthorizationException RefreshToken 이 만료되었을 때 발생한다.
     */
    @Transactional
    public ReissueVo reissue(String refreshToken) throws AuthorizationException {
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

    /**
     * Passport 발행
     * 
     * @param accessToken 엑세스 토큰
     * @return Passport 발행에 성공하면 {@link PassportVo}를 반환한다.
     * @throws AuthorizationException AccessToken 이 만료되었을 때 발생한다.
     * @throws NotFoundAccountException Account 정보를 찾을 수 없을 때 발생한다.
     * @throws NotFoundAccountLogException Account 로그 정보를 찾을 수 없을 때 발생한다.
     * @throws PassportException Passport 발급에 실패할 때 발생한다.
     */
    @Transactional
    public PassportVo passport(String accessToken) throws AuthorizationException, NotFoundAccountException, NotFoundAccountLogException {
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
                .orElseThrow(() -> new NotFoundAccountException(AuthErrorCode.ACCOUNT_NOT_FOUND));

        AccountLog accountLog = accountLogRepository.findTopByAccountIdAndDeviceIdOrderByLoginAt(accountId, deviceId)
                .orElseThrow(() -> new NotFoundAccountLogException(AuthErrorCode.ACCOUNT_LOG_NOT_FOUND));

        PassportVo passportVO = PassportVo.builder()
                .data(PassportData.builder()
                        .account(PassportAccount.builder()
                                .id(accountId)
                                .deviceId(deviceId)
                                .email(account.getEmail())
                                .name(account.getName())
                                .loginCount(accountLog.getLoginCount())
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
