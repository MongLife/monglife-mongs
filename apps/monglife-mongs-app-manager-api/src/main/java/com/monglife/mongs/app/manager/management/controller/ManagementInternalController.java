package com.monglife.mongs.app.manager.management.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.manager.management.dto.request.ChargePayPointRequestDto;
import com.monglife.mongs.app.manager.management.enums.ManagementResponse;
import com.monglife.mongs.app.manager.management.service.ManagementService;
import com.monglife.mongs.module.security.global.principal.Passport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/internal/management")
@RequiredArgsConstructor
public class ManagementInternalController {

    private final ManagementService managementService;

    /**
     * 몽 페이 포인트 증가
     * @param passport 패스 포트
     * @param chargePayPointRequestDto 증가 정보
     * @return 성공 응답
     */
    @PostMapping("/payPoint")
    public ResponseEntity<ResponseDto<?>> chargePayPoint(@AuthenticationPrincipal Passport passport, @RequestBody ChargePayPointRequestDto chargePayPointRequestDto) {

        Long accountId = passport.getAccountId();
        Long mongId = chargePayPointRequestDto.getMongId();
        Integer payPoint = chargePayPointRequestDto.getPayPoint();

        managementService.chargePayPoint(accountId, mongId, payPoint);

        return ResponseEntity.ok(ManagementResponse.APP_MANAGER_MANAGEMENT_CHARGE_PAY_POINT.toResponseDto());
    }
}
