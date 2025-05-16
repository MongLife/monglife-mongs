package com.monglife.mongs.adapter.in.device.web.step.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.common.security.principal.Passport;
import com.monglife.mongs.adapter.in.device.web.enums.AdapterInWebDeviceResponse;
import com.monglife.mongs.adapter.in.device.web.step.dto.request.ExchangeCurrentWalkingCountRequestDto;
import com.monglife.mongs.adapter.in.device.web.step.dto.request.UpdateTotalWalkingCountRequestDto;
import com.monglife.mongs.adapter.in.device.web.step.dto.response.ExchangeCurrentWalkingCountResponseDto;
import com.monglife.mongs.adapter.in.device.web.step.dto.response.UpdateTotalWalkingCountResponseDto;
import com.monglife.mongs.application.device.port.in.StepUseCase;
import com.monglife.mongs.application.device.port.in.command.ExchangeCurrentWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.command.UpdateTotalWalkingCountCommand;
import com.monglife.mongs.domain.device.model.Step;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/step")
@RequiredArgsConstructor
public class StepController {

    private final StepUseCase stepUseCase;

    /**
     * 걸음 수 환전
     */
    @PostMapping("/exchange/walking")
    public ResponseEntity<ResponseDto<ExchangeCurrentWalkingCountResponseDto>> exchangeCurrentWalkingCount(
            @AuthenticationPrincipal Passport passport,
            @Valid @RequestBody ExchangeCurrentWalkingCountRequestDto exchangeCurrentWalkingCountRequestDto
    ) {

        ExchangeCurrentWalkingCountCommand command = ExchangeCurrentWalkingCountCommand.builder()
                .deviceId(passport.getDeviceId())
                .mongId(exchangeCurrentWalkingCountRequestDto.getMongId())
                .walkingCount(exchangeCurrentWalkingCountRequestDto.getWalkingCount())
                .totalWalkingCount(exchangeCurrentWalkingCountRequestDto.getTotalWalkingCount())
                .deviceBootedDt(exchangeCurrentWalkingCountRequestDto.getDeviceBootedDt())
                .build();

        Step step = stepUseCase.exchangeCurrentWalkingCountUseCase(command);

        ExchangeCurrentWalkingCountResponseDto exchangeCurrentWalkingCountResponseDto = ExchangeCurrentWalkingCountResponseDto.builder()
                .consumeWalkingCount(step.getConsumeWalkingCount())
                .walkingCount(step.getWalkingCount())
                .build();

        return ResponseEntity.ok(AdapterInWebDeviceResponse.EXCHANGE_CURRENT_WALKING_COUNT.toResponseDto(exchangeCurrentWalkingCountResponseDto));
    }

    /**
     * 걸음 수 동기화
     */
    @PatchMapping("/walking")
    public ResponseEntity<ResponseDto<UpdateTotalWalkingCountResponseDto>> updateTotalWalkingCount(
            @AuthenticationPrincipal Passport passport,
            @Valid @RequestBody UpdateTotalWalkingCountRequestDto updateTotalWalkingCountRequestDto
    ) {

        UpdateTotalWalkingCountCommand updateTotalWalkingCountCommand = UpdateTotalWalkingCountCommand.builder()
                .deviceId(passport.getDeviceId())
                .totalWalkingCount(updateTotalWalkingCountRequestDto.getTotalWalkingCount())
                .deviceBootedDt(updateTotalWalkingCountRequestDto.getDeviceBootedDt())
                .build();

        Step step = stepUseCase.updateTotalWalkingCountUseCase(updateTotalWalkingCountCommand);

        UpdateTotalWalkingCountResponseDto updateTotalWalkingCountResponseDto = UpdateTotalWalkingCountResponseDto.builder()
                .consumeWalkingCount(step.getConsumeWalkingCount())
                .walkingCount(step.getWalkingCount())
                .build();

        return ResponseEntity.ok(AdapterInWebDeviceResponse.UPDATE_TOTAL_WALKING_COUNT.toResponseDto(updateTotalWalkingCountResponseDto));
    }
}
