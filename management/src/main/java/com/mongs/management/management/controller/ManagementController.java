package com.mongs.management.management.controller;

import com.mongs.core.security.passport.Passport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/management")
public class ManagementController {
    @GetMapping("/test")
    public String test(@AuthenticationPrincipal Passport passport) {
        // passport : 인증 객체 (gateway 로 부터 넘어오는 사용자 정보 값)
        return passport.toString();
    }
}
