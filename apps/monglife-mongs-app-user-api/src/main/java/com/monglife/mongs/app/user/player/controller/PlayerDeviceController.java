package com.monglife.mongs.app.user.player.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.user.player.dto.request.CreateDeviceRequestDto;
import com.monglife.mongs.app.user.player.dto.request.ExchangeWalkingCountRequestDto;
import com.monglife.mongs.app.user.player.dto.response.ExchangeWalkingCountResponseDto;
import com.monglife.mongs.app.user.player.enums.PlayerResponse;
import com.monglife.mongs.app.user.player.service.PlayerDeviceService;
import com.monglife.mongs.app.user.player.vo.PlayerStepVo;
import com.monglife.mongs.module.security.global.principal.Passport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Validated
@RestController
@RequestMapping("/device")
@RequiredArgsConstructor
public class PlayerDeviceController {

    private final PlayerDeviceService playerDeviceService;

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

        PlayerStepVo playerStepVo = playerDeviceService.exchangeWalkingCount(deviceId, mongId, totalWalkingCount, walkingCount, deviceBootedDt);

        ExchangeWalkingCountResponseDto exchangeWalkingCountResponseDto = ExchangeWalkingCountResponseDto.builder()
                .consumeWalkingCount(playerStepVo.getConsumeWalkingCount())
                .walkingCount(playerStepVo.getWalkingCount())
                .build();

        return ResponseEntity.ok(PlayerResponse.APP_USER_PLAYER_EXCHANGE_WALKING_COUNT.toResponseDto(exchangeWalkingCountResponseDto));
    }
}
