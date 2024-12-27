package com.monglife.mongs.app.user.player.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.user.player.dto.etc.GetPlayerDto;
import com.monglife.mongs.app.user.player.vo.PlayerStepVo;
import com.monglife.mongs.app.user.player.dto.request.*;
import com.monglife.mongs.app.user.player.dto.response.ExchangeWalkingCountResponseDto;
import com.monglife.mongs.app.user.player.dto.response.GetPlayerResponseDto;
import com.monglife.mongs.app.user.player.dto.response.SyncWalkingCountResponseDto;
import com.monglife.mongs.app.user.player.enums.PlayerResponse;
import com.monglife.mongs.app.user.player.service.PlayerService;
import com.monglife.mongs.module.security.global.principal.Passport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Validated
@RestController
@RequestMapping("/player")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    /**
     * 플레이어 정보 조회
     * @param passport 패스 포트
     * @return 플레이어 정보
     */
    @GetMapping("")
    public ResponseEntity<ResponseDto<GetPlayerResponseDto>> getPlayer(@AuthenticationPrincipal Passport passport) {

        Long accountId = passport.getAccountId();

        GetPlayerDto getPlayerDto = playerService.getPlayer(accountId);

        GetPlayerResponseDto getPlayerResponseDto = GetPlayerResponseDto.builder()
                .accountId(getPlayerDto.getAccountId())
                .slotCount(getPlayerDto.getSlotCount())
                .starPoint(getPlayerDto.getStarPoint())
                .build();

        return ResponseEntity.ok(PlayerResponse.APP_USER_PLAYER_GET_PLAYER.toResponseDto(getPlayerResponseDto));
    }

    @PatchMapping("/slot")
    public ResponseEntity<ResponseDto<?>> buySlot(@AuthenticationPrincipal Passport passport) {

        playerService.buySlot(passport.getAccountId());

        return ResponseEntity.ok(PlayerResponse.APP_USER_PLAYER_INCREASE_SLOT.toResponseDto());
    }

    @PostMapping("/exchange/walking")
    public ResponseEntity<ResponseDto<ExchangeWalkingCountResponseDto>> exchangeWalkingCount(@AuthenticationPrincipal Passport passport, @RequestBody ExchangeWalkingCountRequestDto exchangeWalkingCountRequestDto) {

        String deviceId = passport.getDeviceId();
        Long mongId = exchangeWalkingCountRequestDto.getMongId();
        Integer totalWalkingCount = exchangeWalkingCountRequestDto.getTotalWalkingCount();
        Integer walkingCount = exchangeWalkingCountRequestDto.getWalkingCount();
        LocalDateTime deviceBootedDt = exchangeWalkingCountRequestDto.getDeviceBootedDt();

        PlayerStepVo playerStepVo = playerService.exchangeWalkingCount(deviceId, mongId, totalWalkingCount, walkingCount, deviceBootedDt);

        ExchangeWalkingCountResponseDto exchangeWalkingCountResponseDto = ExchangeWalkingCountResponseDto.builder()
                .consumeWalkingCount(playerStepVo.getConsumeWalkingCount())
                .walkingCount(playerStepVo.getWalkingCount())
                .build();

        return ResponseEntity.ok(PlayerResponse.APP_USER_PLAYER_EXCHANGE_WALKING_COUNT.toResponseDto(exchangeWalkingCountResponseDto));
    }

    @PostMapping("/exchange/starPoint")
    public ResponseEntity<ResponseDto<?>> exchangeStarPoint(@AuthenticationPrincipal Passport passport, @RequestBody ExchangeStarPointRequestDto exchangeStarPointRequestDto) {

        Long accountId = passport.getAccountId();
        Long mongId = exchangeStarPointRequestDto.getMongId();
        Integer starPoint = exchangeStarPointRequestDto.getStarPoint();

        playerService.exchangeStarPoint(accountId, mongId, starPoint);

        return ResponseEntity.ok(PlayerResponse.APP_USER_PLAYER_EXCHANGE_STAR_POINT.toResponseDto());
    }
}
