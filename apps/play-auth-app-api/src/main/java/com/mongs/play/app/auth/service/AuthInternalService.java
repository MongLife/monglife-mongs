package com.mongs.play.app.auth.service;

import com.mongs.play.core.error.app.AuthInternalErrorCode;
import com.mongs.play.core.exception.app.AuthInternalException;
import com.mongs.play.core.vo.PassportAccountVo;
import com.mongs.play.core.vo.PassportDataVo;
import com.mongs.play.core.vo.PassportVo;
import com.mongs.play.domain.account.entity.Account;
import com.mongs.play.domain.account.service.AccountService;
import com.mongs.play.module.hmac.HmacProvider;
import com.mongs.play.module.jwt.provider.AuthorizationTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthInternalService {

    private final AccountService accountService;
    private final AuthorizationTokenProvider authorizationTokenProvider;
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
        if (authorizationTokenProvider.isTokenExpired(accessToken)) {
            throw new AuthInternalException(AuthInternalErrorCode.ACCESS_TOKEN_EXPIRED);
        }

        Long accountId = authorizationTokenProvider.getMemberId(accessToken)
                .orElseThrow(() -> new AuthInternalException(AuthInternalErrorCode.ACCESS_TOKEN_EXPIRED));
        String deviceId = authorizationTokenProvider.getDeviceId(accessToken)
                .orElseThrow(() -> new AuthInternalException(AuthInternalErrorCode.ACCESS_TOKEN_EXPIRED));

        /* AccessToken 의 accountId 로 account 조회 */
        Account account = accountService.getAccountById(accountId);

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
