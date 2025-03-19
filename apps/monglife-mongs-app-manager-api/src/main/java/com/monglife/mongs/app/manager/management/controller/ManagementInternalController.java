package com.monglife.mongs.app.manager.management.controller;

import com.monglife.module.common.security.principal.Passport;
import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.manager.management.dto.request.ChargePayPointRequestDto;
import com.monglife.mongs.app.manager.management.dto.request.ConsumePayPointRequestDto;
import com.monglife.mongs.app.manager.management.dto.request.PatchMongRequestDto;
import com.monglife.mongs.app.manager.management.dto.response.GetMinimalMongResponseDto;
import com.monglife.mongs.app.manager.management.enums.ManagementResponse;
import com.monglife.mongs.app.manager.management.service.ManagementService;
import com.monglife.mongs.domain.mong.dto.etc.PatchMongDto;
import com.monglife.mongs.domain.mong.vo.MongVo;
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
     * 몽 단건 조회
     * @param mongId 몽 ID
     * @return 몽 조회 응답 DTO
     */
    @GetMapping("/{mongId}")
    public ResponseEntity<ResponseDto<GetMinimalMongResponseDto>> getMong(@PathVariable("mongId") Long mongId) {

        MongVo mongVo = managementService.getMong(mongId);

        GetMinimalMongResponseDto getMinimalMongResponseDto = GetMinimalMongResponseDto.of(mongVo);

        return ResponseEntity.ok(ManagementResponse.APP_MANAGER_MANAGEMENT_GET_MONG.toResponseDto(getMinimalMongResponseDto));
    }

    /**
     * 몽 페이 포인트 증가
     * @param chargePayPointRequestDto 증가 정보
     * @return 성공 응답
     */
    @PatchMapping("/payPoint/charge/{mongId}")
    public ResponseEntity<ResponseDto<?>> chargePayPoint(@PathVariable("mongId") Long mongId, @RequestBody ChargePayPointRequestDto chargePayPointRequestDto) {

        Integer payPoint = chargePayPointRequestDto.getPayPoint();

        managementService.chargePayPoint(mongId, payPoint);

        return ResponseEntity.ok(ManagementResponse.APP_MANAGER_MANAGEMENT_CHARGE_PAY_POINT.toResponseDto());
    }

    /**
     * 몽 페이 포인트 소비
     * @param consumePayPointRequestDto 소비 정보
     * @return 성공 응답
     */
    @PatchMapping("/payPoint/consume/{mongId}")
    public ResponseEntity<ResponseDto<?>> consumePayPoint(@PathVariable("mongId") Long mongId, @RequestBody ConsumePayPointRequestDto consumePayPointRequestDto) {

        Integer payPoint = consumePayPointRequestDto.getPayPoint();

        managementService.consumePayPoint(mongId, payPoint);

        return ResponseEntity.ok(ManagementResponse.APP_MANAGER_MANAGEMENT_CONSUME_PAY_POINT.toResponseDto());
    }

    /**
     * 훈련 후 몽 정보 갱신
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @param patchMongRequestDto 훈련 후 몽 정보 Dto
     * @return 성공 응답
     */
    @PostMapping("/training/{mongId}")
    public ResponseEntity<ResponseDto<?>> patchMongAfterTraining(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId, @RequestBody PatchMongRequestDto patchMongRequestDto) {

        Long accountId = passport.getAccountId();

        PatchMongDto patchMongDto = PatchMongDto.builder()
                .exp(patchMongRequestDto.getExp())
                .weight(patchMongRequestDto.getWeight())
                .strength(patchMongRequestDto.getStrength())
                .satiety(patchMongRequestDto.getSatiety())
                .healthy(patchMongRequestDto.getHealthy())
                .fatigue(patchMongRequestDto.getFatigue())
                .poopCount(patchMongRequestDto.getPoopCount())
                .payPoint(patchMongRequestDto.getPayPoint())
                .build();

        managementService.trainingMong(accountId, mongId, patchMongDto);

        return ResponseEntity.ok(ManagementResponse.APP_MANAGER_MANAGEMENT_PATCH_MONG_AFTER_TRAINING.toResponseDto());
    }

    /**
     * 배틀 후 몽 정보 갱신
     * @param mongId 몽 ID
     * @param patchMongRequestDto 훈련 후 몽 정보 Dto
     * @return 성공 응답
     */
    @PostMapping("/battle/{mongId}")
    public ResponseEntity<ResponseDto<?>> patchMongAfterBattle(@PathVariable("mongId") Long mongId, @RequestBody PatchMongRequestDto patchMongRequestDto) {

        Double exp = patchMongRequestDto.getExp();
        Integer payPoint = patchMongRequestDto.getPayPoint();

        managementService.battleMong(mongId, exp, payPoint);

        return ResponseEntity.ok(ManagementResponse.APP_MANAGER_MANAGEMENT_PATCH_MONG_AFTER_TRAINING.toResponseDto());
    }
}
