package com.mongs.management.domain.controller;

import com.mongs.core.security.principal.PassportDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/management")
public class ManagementController {
    @GetMapping("/test")
    public String test(@AuthenticationPrincipal PassportDetail passportDetail) {
        // passport : 인증 객체 (gateway 로 부터 넘어오는 사용자 정보 값)
        return passportDetail.toString();
    }
}
