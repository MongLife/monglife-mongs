package com.mongs.auth.controller;

import com.mongs.auth.dto.request.LoginReqDto;
import com.mongs.auth.dto.request.PassportReqDto;
import com.mongs.auth.dto.request.ReissueReqDto;
import com.mongs.auth.service.AuthService;
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
    public ResponseEntity<Object> login(@RequestBody @Validated LoginReqDto loginReqDto) {
        return ResponseEntity.ok().body(authService.login(loginReqDto.deviceId(), loginReqDto.email(), loginReqDto.name()));
    }

    @PostMapping("/reissue")
    public ResponseEntity<Object> reissue(@RequestBody @Validated ReissueReqDto reissueReqDto) {
        return ResponseEntity.ok().body(authService.reissue(reissueReqDto.refreshToken()));
    }

    @PostMapping("/passport")
    public ResponseEntity<Object> passport(@RequestBody @Validated PassportReqDto passportReqDto) {
        return ResponseEntity.ok().body(authService.passport(passportReqDto.accessToken()));
    }
}


