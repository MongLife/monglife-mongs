package com.mongs.auth.service;

import com.mongs.auth.dto.response.ReissueResDto;
import com.mongs.auth.exception.AuthorizationException;
import com.mongs.auth.exception.AuthErrorCode;
import com.mongs.auth.repository.MemberRepository;
import com.mongs.auth.repository.TokenRepository;
import com.mongs.auth.dto.response.LoginResDto;
import com.mongs.auth.entity.Account;
import com.mongs.auth.entity.Token;
import com.mongs.auth.exception.NotFoundException;
import com.mongs.auth.exception.PassportException;
import com.mongs.core.passport.PassportVO;
import com.mongs.core.passport.PassportData;
import com.mongs.core.passport.PassportAccount;
import com.mongs.auth.repository.AccountRepository;
import com.mongs.core.util.HmacProvider;
import com.mongs.core.util.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final TokenProvider tokenProvider;
    private final HmacProvider hmacProvider;

    @Value("${application.security.jwt.refresh-expiration}")
    private Long expiration;

    public LoginResDto login(String deviceId, String email, String name) throws RuntimeException {
        /* 회원 가입 (회원 정보가 없는 경우) */
        Account account = accountRepository.findByEmail(email)
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

        return LoginResDto.builder()
                .accessToken(tokenProvider.generateAccessToken(token.getAccountId(), token.getDeviceId()))
                .refreshToken(token.getRefreshToken())
                .build();
    }

    public ReissueResDto reissue(String refreshToken) throws RuntimeException {
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

        return ReissueResDto.builder()
                .accessToken(tokenProvider.generateAccessToken(newToken.getAccountId(), newToken.getDeviceId()))
                .refreshToken(newToken.getRefreshToken())
                .build();
    }
    
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
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException(AuthErrorCode.ACCOUNT_NOT_FOUND));

        PassportVO passportVO = PassportVO.builder()
                .data(PassportData.builder()
                        .account(PassportAccount.builder()
                                .id(accountId)
                                .deviceId(deviceId)
                                .email(account.getEmail())
                                .name(account.getName())
                                .role("NORMAL")
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
  
    private Account registerAccount(String email, String name) throws RuntimeException {
        Account registerAccount = accountRepository.save(Account.builder()
                .name(name)
                .email(email)
                .build());

        memberRepository.registerMember(registerAccount.getId());

        return registerAccount;
    }
}
