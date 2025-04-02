package com.mongs.play.app.auth.controller;

import com.mongs.play.app.auth.dto.req.LoginReqDto;
import com.mongs.play.app.auth.dto.req.LogoutReqDto;
import com.mongs.play.app.auth.dto.req.ReissueReqDto;
import com.mongs.play.app.auth.dto.res.LoginResDto;
import com.mongs.play.app.auth.dto.res.LogoutResDto;
import com.mongs.play.app.auth.dto.res.ReissueResDto;
import com.mongs.play.app.auth.service.AuthExternalService;
import com.mongs.play.app.auth.vo.LoginVo;
import com.mongs.play.app.auth.vo.LogoutVo;
import com.mongs.play.app.auth.vo.ReissueVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthExternalController {

    private final AuthExternalService authExternalService;

    /**
     * 계정이 존재하면 로그인을 진행하고, 없으면 회원가입 후 로그인을 진행한다.
     *
     * @param loginReqDto 기기 ID, 이메일, 이름
     * @return 로그인에 성공하는 경우 {@link LoginResDto}를 반환한다.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login(@RequestBody @Validated LoginReqDto loginReqDto) {

        LoginVo loginVo = authExternalService.login(loginReqDto.deviceId(), loginReqDto.email(), loginReqDto.name());

        return ResponseEntity.ok().body(LoginResDto.builder()
                .accountId(loginVo.accountId())
                .accessToken(loginVo.accessToken())
                .refreshToken(loginVo.refreshToken())
                .build());
    }

    /**
     * 로그아웃을 진행한다.
     *
     * @param logoutReqDto 리프래시 토큰
     * @return 로그아웃에 성공하는 경우 {@link LogoutResDto}를 반환한다.
     */
    @PostMapping("/logout")
    public ResponseEntity<LogoutResDto> logout(@RequestBody @Validated LogoutReqDto logoutReqDto) {

        LogoutVo logoutVo = authExternalService.logout(logoutReqDto.refreshToken());

        return ResponseEntity.ok().body(LogoutResDto.builder()
                .accountId(logoutVo.accountId())
                .build());
    }

    /**
     * 토큰 재발급을 진행한다.
     *
     * @param reissueReqDto 리프래시 토큰
     * @return 토큰 재발급에 성공하는 경우 {@link ReissueResDto}를 반환한다.
     */
    @PostMapping("/reissue")
    public ResponseEntity<ReissueResDto> reissue(@RequestBody @Validated ReissueReqDto reissueReqDto) {

        ReissueVo reissueVo = authExternalService.reissue(reissueReqDto.refreshToken());

        return ResponseEntity.ok().body(ReissueResDto.builder()
                .accessToken(reissueVo.accessToken())
                .refreshToken(reissueVo.refreshToken())
                .build());
    }
}
