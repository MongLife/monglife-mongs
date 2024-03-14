package com.mongs.auth.controller;

import com.mongs.auth.dto.request.LoginReqDto;
import com.mongs.auth.dto.request.LogoutReqDto;
import com.mongs.auth.dto.request.PassportReqDto;
import com.mongs.auth.dto.request.ReissueReqDto;
import com.mongs.auth.dto.response.LoginResDto;
import com.mongs.auth.dto.response.LogoutResDto;
import com.mongs.auth.dto.response.ReissueResDto;
import com.mongs.auth.service.AuthService;
import com.mongs.core.vo.passport.PassportVO;
import com.mongs.core.vo.auth.LoginVo;
import com.mongs.core.vo.auth.LogoutVo;
import com.mongs.core.vo.auth.ReissueVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login(@RequestBody @Validated LoginReqDto loginReqDto) {
        LoginVo loginVo = authService.login(loginReqDto.deviceId(), loginReqDto.email(), loginReqDto.name());
        return ResponseEntity.ok().body(LoginResDto.builder()
                .accessToken(loginVo.accessToken())
                .refreshToken(loginVo.refreshToken())
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResDto> logout(@RequestBody @Validated LogoutReqDto logoutReqDto) {
        LogoutVo logoutVo = authService.logout(logoutReqDto.refreshToken());
        return ResponseEntity.ok().body(LogoutResDto.builder()
                .accountId(logoutVo.accountId())
                .build());
    }

    @PostMapping("/reissue")
    public ResponseEntity<ReissueResDto> reissue(@RequestBody @Validated ReissueReqDto reissueReqDto) {
        ReissueVo reissueVo = authService.reissue(reissueReqDto.refreshToken());
        return ResponseEntity.ok().body(ReissueResDto.builder()
                .accessToken(reissueVo.accessToken())
                .refreshToken(reissueVo.refreshToken())
                .build());
    }

    @PostMapping("/passport")
    public ResponseEntity<PassportVO> passport(@RequestBody @Validated PassportReqDto passportReqDto) {
        return ResponseEntity.ok().body(authService.passport(passportReqDto.accessToken()));
    }
}


