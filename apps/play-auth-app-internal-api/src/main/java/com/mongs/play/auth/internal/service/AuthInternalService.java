package com.mongs.play.auth.internal.service;

import com.mongs.play.app.core.error.AuthInternalErrorCode;
import com.mongs.play.app.core.exception.AuthInternalException;
import com.mongs.play.app.core.vo.PassportAccountVo;
import com.mongs.play.app.core.vo.PassportDataVo;
import com.mongs.play.app.core.vo.PassportVo;
import com.mongs.play.domain.account.entity.Account;
import com.mongs.play.domain.account.service.AccountService;
import com.mongs.play.hmac.HmacProvider;
import com.mongs.play.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthInternalService {

    private final AccountService accountService;
    private final JwtTokenProvider jwtTokenProvider;
    private final HmacProvider hmacProvider;

    /**
     * Passport 발행
     *
     * @param accessToken 엑세스 토큰
     * @return Passport 발행에 성공하면 {@link PassportVo}를 반환한다.
     * @throws AuthInternalException AccessToken 이 만료되었을 때 발생한다.
     */
    @Transactional(readOnly = true)
    public PassportVo passport(String accessToken) throws AuthInternalException {
        /* AccessToken 검증 */
        if (jwtTokenProvider.isTokenExpired(accessToken)) {
            throw new AuthInternalException(AuthInternalErrorCode.ACCESS_TOKEN_EXPIRED);
        }

        Long accountId = jwtTokenProvider.getMemberId(accessToken)
                .orElseThrow(() -> new AuthInternalException(AuthInternalErrorCode.ACCESS_TOKEN_EXPIRED));
        String deviceId = jwtTokenProvider.getDeviceId(accessToken)
                .orElseThrow(() -> new AuthInternalException(AuthInternalErrorCode.ACCESS_TOKEN_EXPIRED));

        /* AccessToken 의 accountId 로 account 조회 */
        Account account = accountService.getAccountById(accountId)
                .orElseThrow(() -> new AuthInternalException(AuthInternalErrorCode.ACCOUNT_NOT_FOUND));

        PassportVo passportVo = PassportVo.builder()
                .data(PassportDataVo.builder()
                        .account(PassportAccountVo.builder()
                                .id(accountId)
                                .deviceId(deviceId)
                                .email(account.getEmail())
                                .name(account.getName())
                                .role(account.getRole())
                                .build())
                        .build())
                .build();

        String passportIntegrity = hmacProvider.generateHmac(passportVo.data())
                .orElseThrow(() -> new AuthInternalException(AuthInternalErrorCode.PASSPORT_GENERATE_FAIL));

        /* passport 생성 및 dto 반환 */
        return passportVo.toBuilder()
                .passportIntegrity(passportIntegrity)
                .build();
    }
}
