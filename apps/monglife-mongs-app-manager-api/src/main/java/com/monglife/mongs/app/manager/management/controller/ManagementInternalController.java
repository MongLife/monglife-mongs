package com.monglife.mongs.app.manager.management.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.manager.management.dto.request.ChargePayPointRequestDto;
import com.monglife.mongs.app.manager.management.dto.request.PatchMongAfterTrainingRequestDto;
import com.monglife.mongs.app.manager.management.enums.ManagementResponse;
import com.monglife.mongs.app.manager.management.service.ManagementService;
import com.monglife.mongs.domain.mong.dto.etc.PatchMongDto;
import com.monglife.mongs.module.security.global.principal.Passport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @PatchMapping("/payPoint/{mongId}")
    public ResponseEntity<ResponseDto<?>> chargePayPoint(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId, @RequestBody ChargePayPointRequestDto chargePayPointRequestDto) {

        Long accountId = passport.getAccountId();
        Integer payPoint = chargePayPointRequestDto.getPayPoint();

        managementService.chargePayPoint(accountId, mongId, payPoint);

        return ResponseEntity.ok(ManagementResponse.APP_MANAGER_MANAGEMENT_CHARGE_PAY_POINT.toResponseDto());
    }

    /**
     * 훈련 후 몽 정보 갱신
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @param patchMongAfterTrainingRequestDto 훈련 후 몽 정보 Dto
     * @return 성공 응답
     */
    @PostMapping("/{mongId}")
    public ResponseEntity<ResponseDto<?>> patchMongAfterTraining(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId, @RequestBody PatchMongAfterTrainingRequestDto patchMongAfterTrainingRequestDto) {

        Long accountId = passport.getAccountId();

        PatchMongDto patchMongDto = PatchMongDto.builder()
                .exp(patchMongAfterTrainingRequestDto.getExp())
                .weight(patchMongAfterTrainingRequestDto.getWeight())
                .strength(patchMongAfterTrainingRequestDto.getStrength())
                .satiety(patchMongAfterTrainingRequestDto.getSatiety())
                .healthy(patchMongAfterTrainingRequestDto.getHealthy())
                .fatigue(patchMongAfterTrainingRequestDto.getFatigue())
                .poopCount(patchMongAfterTrainingRequestDto.getPoopCount())
                .payPoint(patchMongAfterTrainingRequestDto.getPayPoint())
                .build();

        managementService.trainingMong(accountId, mongId, patchMongDto);

        return ResponseEntity.ok(ManagementResponse.APP_MANAGER_MANAGEMENT_PATCH_MONG_AFTER_TRAINING.toResponseDto());
    }
}
