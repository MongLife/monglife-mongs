package com.monglife.mongs.app.manager.management.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.manager.management.dto.request.ChargePayPointRequestDto;
import com.monglife.mongs.app.manager.management.dto.request.CreateMongRequestDto;
import com.monglife.mongs.app.manager.management.dto.request.FeedMongRequestDto;
import com.monglife.mongs.app.manager.management.dto.response.GetFeedItemsResponseDto;
import com.monglife.mongs.app.manager.management.dto.response.GetMongResponseDto;
import com.monglife.mongs.app.manager.management.enums.ManagementResponse;
import com.monglife.mongs.app.manager.management.service.ManagementService;
import com.monglife.mongs.client.user.client.CollectionClient;
import com.monglife.mongs.domain.mong.dto.etc.GetFeedItemDto;
import com.monglife.mongs.domain.mong.dto.etc.GetMongDto;
import com.monglife.mongs.module.security.global.principal.Passport;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@Validated
@RestController
@RequestMapping("/management/internal")
@RequiredArgsConstructor
public class ManagementInternalController {

    private final ManagementService managementService;

    /**
     * For Test
     * TODO: 삭제 필요
     */
    @GetMapping("/health")
    public String health() {
        return "manager health";
    }

    /**
     * 몽 페이 포인트 증가
     * @param passport 패스 포트
     * @param chargePayPointRequestDto 증가 정보
     * @return 성공 응답
     */
    @PatchMapping("/payPoint")
    public ResponseEntity<ResponseDto<?>> chargePayPoint(@AuthenticationPrincipal Passport passport, @RequestBody ChargePayPointRequestDto chargePayPointRequestDto) {

        Long accountId = passport.getAccountId();
        Long mongId = chargePayPointRequestDto.getMongId();
        Integer payPoint = chargePayPointRequestDto.getPayPoint();

        managementService.chargePayPoint(accountId, mongId, payPoint);

        return ResponseEntity.ok(ManagementResponse.APP_MANAGER_MANAGEMENT_CHARGE_PAY_POINT.toResponseDto());
    }
}
