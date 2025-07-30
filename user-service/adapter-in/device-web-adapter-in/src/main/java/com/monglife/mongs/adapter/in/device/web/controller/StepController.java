package com.monglife.mongs.adapter.in.device.web.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.common.logging.annotation.EntryLoggingPoint;
import com.monglife.module.common.security.principal.Passport;
import com.monglife.mongs.adapter.in.device.web.dto.request.ExchangeCurrentWalkingCountRequestDto;
import com.monglife.mongs.adapter.in.device.web.dto.request.UpdateTotalWalkingCountRequestDto;
import com.monglife.mongs.adapter.in.device.web.dto.response.ExchangeCurrentWalkingCountResponseDto;
import com.monglife.mongs.adapter.in.device.web.dto.response.GetStepResponseDto;
import com.monglife.mongs.adapter.in.device.web.dto.response.UpdateTotalWalkingCountResponseDto;
import com.monglife.mongs.adapter.in.device.web.enums.AdapterInDeviceWebResponse;
import com.monglife.mongs.application.device.port.in.StepUseCase;
import com.monglife.mongs.application.device.port.in.command.ExchangeCurrentWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.command.GetStepCommand;
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
     * 걸음 수 조회
     */
    @EntryLoggingPoint
    @GetMapping
    public ResponseEntity<ResponseDto<GetStepResponseDto>> getStep(@AuthenticationPrincipal Passport passport) {

        GetStepCommand command = GetStepCommand.builder()
                .deviceId(passport.getDeviceId())
                .build();

        Step step = stepUseCase.getStepUseCase(command);

        GetStepResponseDto getStepResponseDto = GetStepResponseDto.builder()
                .consumeWalkingCount(step.getConsumeWalkingCount())
                .walkingCount(step.getWalkingCount())
                .build();

        return ResponseEntity.ok(AdapterInDeviceWebResponse.GET_STEP.toResponseDto(getStepResponseDto));
    }

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

    /**
     * 걸음 수 동기화
     */
    @EntryLoggingPoint
    @PatchMapping
    public ResponseEntity<ResponseDto<UpdateTotalWalkingCountResponseDto>> updateTotalWalkingCount(
            @AuthenticationPrincipal Passport passport,
            @Valid @RequestBody UpdateTotalWalkingCountRequestDto updateTotalWalkingCountRequestDto
    ) {

        UpdateTotalWalkingCountCommand updateTotalWalkingCountCommand = UpdateTotalWalkingCountCommand.builder()
                .deviceId(passport.getDeviceId())
                .totalWalkingCount(updateTotalWalkingCountRequestDto.getTotalWalkingCount())
                .deviceBootedAt(updateTotalWalkingCountRequestDto.getDeviceBootedAt())
                .build();

        Step step = stepUseCase.updateTotalWalkingCountUseCase(updateTotalWalkingCountCommand);

        UpdateTotalWalkingCountResponseDto updateTotalWalkingCountResponseDto = UpdateTotalWalkingCountResponseDto.builder()
                .consumeWalkingCount(step.getConsumeWalkingCount())
                .walkingCount(step.getWalkingCount())
                .build();

        return ResponseEntity.ok(AdapterInDeviceWebResponse.UPDATE_TOTAL_WALKING_COUNT.toResponseDto(updateTotalWalkingCountResponseDto));
    }
}
