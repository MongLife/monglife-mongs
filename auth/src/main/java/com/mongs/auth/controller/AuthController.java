package com.mongs.auth.controller;

import com.mongs.auth.dto.request.LoginReqDto;
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
}
