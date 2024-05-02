package com.mongs.play.app.auth.external.service;

import com.mongs.play.app.auth.external.vo.LoginVo;
import com.mongs.play.app.auth.external.vo.LogoutVo;
import com.mongs.play.app.auth.external.vo.ReissueVo;
import com.mongs.play.domain.account.entity.Account;
import com.mongs.play.domain.account.service.AccountLogService;
import com.mongs.play.domain.account.service.AccountService;
import com.mongs.play.module.jwt.JwtTokenProvider;
import com.mongs.play.session.entity.Session;
import com.mongs.play.session.service.SessionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthExternalService {

    private final AccountService accountService;
    private final AccountLogService accountLogService;
    private final SessionService sessionService;
    private final JwtTokenProvider jwtTokenProvider;

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
        Account account = accountService.getAccountByEmailAddIfNotExist(email, name);

        /* 이전 RefreshToken 삭제 */
        sessionService.removeSessionIfExists(deviceId, account.getId());

        /* AccessToken 및 RefreshToken 발급 */
        Long refreshTokenExpiration = jwtTokenProvider.getRefreshTokenExpiration();
        String newRefreshToken = jwtTokenProvider.generateRefreshToken();
        String newAccessToken = jwtTokenProvider.generateAccessToken(account.getId(), deviceId);

        sessionService.addSession(newRefreshToken, deviceId, account.getId(), refreshTokenExpiration);

        /* 로그인 로그 저장 */
        accountLogService.modifyLoginCountAddAccountLogIfNotExist(account.getId(), deviceId);

        return LoginVo.builder()
                .accountId(account.getId())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
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
    public LogoutVo logout(String refreshToken) {

        Session session = sessionService.getSession(refreshToken);

        sessionService.removeSession(session.getRefreshToken());

        return LogoutVo.builder()
                .accountId(session.getAccountId())
                .build();
    }

    /**
     * 토큰 재발급
     * redis 에 RefreshToken 존재 여부(만료 여부)를 확인 후 재발급을 진행한다.
     *
     * @param refreshToken 리프래시 토큰
     * @return 토큰 재발급에 성공하면 {@link ReissueVo}를 반환한다.
     */
    @Transactional
    public ReissueVo reissue(String refreshToken) {

        /* RefreshToken Redis 존재 여부 확인 */
        Session session = sessionService.getSession(refreshToken);

        sessionService.removeSession(session.getRefreshToken());

        /* AccessToken 및 RefreshToken 발급 */
        Long refreshTokenExpiration = jwtTokenProvider.getRefreshTokenExpiration();
        String newRefreshToken = jwtTokenProvider.generateRefreshToken();
        String newAccessToken = jwtTokenProvider.generateAccessToken(session.getAccountId(), session.getDeviceId());

        sessionService.addSession(newRefreshToken, session.getDeviceId(), session.getAccountId(), refreshTokenExpiration);

        return ReissueVo.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
