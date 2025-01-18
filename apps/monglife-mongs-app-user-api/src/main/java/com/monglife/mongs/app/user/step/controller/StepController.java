package com.monglife.mongs.app.user.step.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.user.player.vo.PlayerStepVo;
import com.monglife.mongs.app.user.step.dto.request.ExchangeWalkingCountRequestDto;
import com.monglife.mongs.app.user.step.dto.request.UpdateWalkingCountRequestDto;
import com.monglife.mongs.app.user.step.dto.response.ExchangeWalkingCountResponseDto;
import com.monglife.mongs.app.user.step.dto.response.UpdateWalkingCountResponseDto;
import com.monglife.mongs.app.user.step.enums.StepResponse;
import com.monglife.mongs.app.user.step.service.StepService;
import com.monglife.mongs.module.security.global.principal.Passport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Validated
@RestController
@RequestMapping("/open/step")
@RequiredArgsConstructor
public class StepController {

    private final StepService stepService;

    /**
     * 걸음 수 환전
     * @param passport 패스 포트
     * @param exchangeWalkingCountRequestDto 걸음 수 환전 요청 Dto
     * @return 성공 응답
     */
    @PostMapping("/exchange/walking")
    public ResponseEntity<ResponseDto<ExchangeWalkingCountResponseDto>> exchangeWalkingCount(@AuthenticationPrincipal Passport passport, @RequestBody ExchangeWalkingCountRequestDto exchangeWalkingCountRequestDto) {

        String deviceId = passport.getDeviceId();
        Long mongId = exchangeWalkingCountRequestDto.getMongId();
        Integer totalWalkingCount = exchangeWalkingCountRequestDto.getTotalWalkingCount();
        Integer walkingCount = exchangeWalkingCountRequestDto.getWalkingCount();
        LocalDateTime deviceBootedDt = exchangeWalkingCountRequestDto.getDeviceBootedDt();

        PlayerStepVo playerStepVo = stepService.exchangeWalkingCount(deviceId, mongId, totalWalkingCount, walkingCount, deviceBootedDt);

        ExchangeWalkingCountResponseDto exchangeWalkingCountResponseDto = ExchangeWalkingCountResponseDto.builder()
                .consumeWalkingCount(playerStepVo.getConsumeWalkingCount())
                .walkingCount(playerStepVo.getWalkingCount())
                .build();

        return ResponseEntity.ok(StepResponse.APP_USER_STEP_EXCHANGE_WALKING_COUNT.toResponseDto(exchangeWalkingCountResponseDto));
    }

    /**
     * 걸음 수 동기화 (실시간)
     * @param updateWalkingCountRequestDto 걸음 수 동기화 요청 Dto
     * @return 동기화 후 잔여 걸음 수 응답 Dto
     */
    @PatchMapping("/walking")
    public ResponseEntity<ResponseDto<UpdateWalkingCountResponseDto>> updateWalkingCount(@AuthenticationPrincipal Passport passport, @RequestBody UpdateWalkingCountRequestDto updateWalkingCountRequestDto) {

        String deviceId = passport.getDeviceId();
        Integer totalWalkingCount = updateWalkingCountRequestDto.getTotalWalkingCount();
        LocalDateTime deviceBootedDt = updateWalkingCountRequestDto.getDeviceBootedDt();

        PlayerStepVo playerStepVo = stepService.updateWalkingCount(deviceId, totalWalkingCount, deviceBootedDt);

        UpdateWalkingCountResponseDto updateWalkingCountResponseDto = UpdateWalkingCountResponseDto.builder()
                .consumeWalkingCount(playerStepVo.getConsumeWalkingCount())
                .walkingCount(playerStepVo.getWalkingCount())
                .build();

        return ResponseEntity.ok(StepResponse.APP_USER_STEP_UPDATE_WALKING_COUNT.toResponseDto(updateWalkingCountResponseDto));
    }
}
