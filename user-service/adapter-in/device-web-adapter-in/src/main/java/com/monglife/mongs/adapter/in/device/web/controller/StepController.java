package com.monglife.mongs.adapter.in.device.web.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.common.logging.annotation.EntryLoggingPoint;
import com.monglife.module.common.security.principal.Passport;
import com.monglife.mongs.adapter.in.device.web.dto.request.ExchangeCurrentWalkingCountRequestDto;
import com.monglife.mongs.adapter.in.device.web.dto.response.ExchangeCurrentWalkingCountResponseDto;
import com.monglife.mongs.adapter.in.device.web.enums.AdapterInDeviceWebResponse;
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
    @EntryLoggingPoint
    @PostMapping("/exchange")
    public ResponseEntity<ResponseDto<ExchangeCurrentWalkingCountResponseDto>> exchangeCurrentWalkingCount(
            @AuthenticationPrincipal Passport passport,
            @Valid @RequestBody ExchangeCurrentWalkingCountRequestDto exchangeCurrentWalkingCountRequestDto
    ) {

        UpdateTotalWalkingCountCommand updateTotalWalkingCountCommand = UpdateTotalWalkingCountCommand.builder()
                .deviceId(passport.getDeviceId())
                .totalWalkingCount(exchangeCurrentWalkingCountRequestDto.getTotalWalkingCount())
                .deviceBootedAt(exchangeCurrentWalkingCountRequestDto.getDeviceBootedAt())
                .build();

        // 걸음 수 동기화
        stepUseCase.updateTotalWalkingCountUseCase(updateTotalWalkingCountCommand);

        ExchangeCurrentWalkingCountCommand command = ExchangeCurrentWalkingCountCommand.builder()
                .accountId(passport.getAccountId())
                .deviceId(passport.getDeviceId())
                .mongId(exchangeCurrentWalkingCountRequestDto.getMongId())
                .walkingCount(exchangeCurrentWalkingCountRequestDto.getWalkingCount())
                .build();

        // 걸음 수 환전
        Step step = stepUseCase.exchangeCurrentWalkingCountUseCase(command);

        ExchangeCurrentWalkingCountResponseDto exchangeCurrentWalkingCountResponseDto = ExchangeCurrentWalkingCountResponseDto.builder()
                .consumeWalkingCount(step.getConsumeWalkingCount())
                .walkingCount(step.getWalkingCount())
                .build();

        return ResponseEntity.ok(AdapterInDeviceWebResponse.EXCHANGE_CURRENT_WALKING_COUNT.toResponseDto(exchangeCurrentWalkingCountResponseDto));
    }
}
