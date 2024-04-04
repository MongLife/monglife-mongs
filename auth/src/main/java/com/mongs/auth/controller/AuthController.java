package com.mongs.auth.controller;

import com.mongs.auth.controller.dto.request.LoginReqDto;
import com.mongs.auth.controller.dto.request.LogoutReqDto;
import com.mongs.auth.controller.dto.request.PassportReqDto;
import com.mongs.auth.controller.dto.request.ReissueReqDto;
import com.mongs.auth.controller.dto.response.LoginResDto;
import com.mongs.auth.controller.dto.response.LogoutResDto;
import com.mongs.auth.controller.dto.response.ReissueResDto;
import com.mongs.auth.service.AuthService;
import com.mongs.core.vo.passport.PassportVo;
import com.mongs.auth.service.vo.LoginVo;
import com.mongs.auth.service.vo.LogoutVo;
import com.mongs.auth.service.vo.ReissueVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    /**
     * 계정이 존재하면 로그인을 진행하고, 없으면 회원가입 후 로그인을 진행한다.
     * 
     * @param loginReqDto 기기 ID, 이메일, 이름
     * @return 로그인에 성공하는 경우 {@link LoginResDto}를 반환한다.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login(@RequestBody @Validated LoginReqDto loginReqDto) {
        LoginVo loginVo = authService.login(loginReqDto.deviceId(), loginReqDto.email(), loginReqDto.name());
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
        LogoutVo logoutVo = authService.logout(logoutReqDto.refreshToken());
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
        ReissueVo reissueVo = authService.reissue(reissueReqDto.refreshToken());
        return ResponseEntity.ok().body(ReissueResDto.builder()
                .accessToken(reissueVo.accessToken())
                .refreshToken(reissueVo.refreshToken())
                .build());
    }

    /**
     * Passport 발급을 진행한다.
     *
     * @param passportReqDto 엑세스 토큰
     * @return Passport 발급에 성공하는 경우 {@link PassportVo}를 반환한다.
     */
    @PostMapping("/passport")
    public ResponseEntity<PassportVo> passport(@RequestBody @Validated PassportReqDto passportReqDto) {
        return ResponseEntity.ok().body(authService.passport(passportReqDto.accessToken()));
    }
}


