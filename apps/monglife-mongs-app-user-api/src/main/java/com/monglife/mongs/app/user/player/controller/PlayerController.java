package com.monglife.mongs.app.user.player.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.user.player.dto.etc.GetPlayerDto;
import com.monglife.mongs.app.user.player.vo.PlayerStepVo;
import com.monglife.mongs.app.user.player.dto.request.*;
import com.monglife.mongs.app.user.player.dto.response.ExchangeWalkingCountResponseDto;
import com.monglife.mongs.app.user.player.dto.response.GetPlayerResponseDto;
import com.monglife.mongs.app.user.player.dto.response.ResetWalkingCountResponseDto;
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
@RequiredArgsConstructor
@RequestMapping("/user/player")
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("")
    public ResponseEntity<ResponseDto<GetPlayerResponseDto>> getPlayer(@AuthenticationPrincipal Passport passport) {

        Long accountId = passport.getAccountId();

        GetPlayerDto getPlayerDto = playerService.getPlayer(accountId);

        GetPlayerResponseDto getPlayerResponseDto = GetPlayerResponseDto.builder()
                .accountId(getPlayerDto.getAccountId())
                .slotCount(getPlayerDto.getSlotCount())
                .starPoint(getPlayerDto.getStarPoint())
                .build();

        return ResponseEntity.ok(PlayerResponse.USER_PLAYER_GET_PLAYER.toResponseDto(getPlayerResponseDto));
    }

    @PatchMapping("/slot")
    public ResponseEntity<ResponseDto<?>> buySlot(@AuthenticationPrincipal Passport passport) {

        playerService.buySlot(passport.getAccountId());

        return ResponseEntity.ok(PlayerResponse.USER_PLAYER_INCREASE_SLOT.toResponseDto());
    }

    @PostMapping("/charge/starPoint")
    public ResponseEntity<ResponseDto<?>> chargeStarPoint(@AuthenticationPrincipal Passport passport, @RequestBody ChargeStarPointRequestDto chargeStarPointRequestDto) {

        Long accountId = passport.getAccountId();
        Integer starPoint = chargeStarPointRequestDto.getStarPoint();

        playerService.chargeStarPoint(accountId, starPoint);

        return ResponseEntity.ok(PlayerResponse.USER_PLAYER_INCREASE_STAR_POINT.toResponseDto());
    }

    @PostMapping("/exchange/starPoint")
    public ResponseEntity<ResponseDto<?>> exchangeStarPoint(@AuthenticationPrincipal Passport passport, @RequestBody ExchangeStarPointRequestDto exchangeStarPointRequestDto) {

        Long accountId = passport.getAccountId();
        Long mongId = exchangeStarPointRequestDto.getMongId();
        Integer starPoint = exchangeStarPointRequestDto.getStarPoint();

        playerService.exchangeStarPoint(accountId, mongId, starPoint);

        return ResponseEntity.ok(PlayerResponse.USER_PLAYER_DECREASE_STAR_POINT.toResponseDto());
    }

    @PatchMapping("/sync/walking")
    public ResponseEntity<ResponseDto<SyncWalkingCountResponseDto>> syncWalking(@RequestBody SyncWalkingCountRequestDto syncWalkingCountRequestDto) {

        String deviceId = syncWalkingCountRequestDto.getDeviceId();
        Integer totalWalkingCount = syncWalkingCountRequestDto.getTotalWalkingCount();
        LocalDateTime deviceBootedDt = syncWalkingCountRequestDto.getDeviceBootedDt();

        PlayerStepVo playerStepVo = playerService.syncWalkingCount(deviceId, totalWalkingCount, deviceBootedDt);

        SyncWalkingCountResponseDto syncWalkingCountResponseDto = SyncWalkingCountResponseDto.builder()
                .consumeWalkingCount(playerStepVo.getConsumeWalkingCount())
                .walkingCount(playerStepVo.getWalkingCount())
                .build();

        return ResponseEntity.ok(PlayerResponse.USER_PLAYER_SYNC_WALKING_COUNT.toResponseDto(syncWalkingCountResponseDto));
    }

    @PatchMapping("/reset/walking")
    public ResponseEntity<ResponseDto<ResetWalkingCountResponseDto>> resetWalkingCount(@AuthenticationPrincipal Passport passport, @RequestBody ResetWalkingCountRequestDto resetWalkingCountRequestDto) {

        String deviceId = passport.getDeviceId();
        Long accountId = passport.getAccountId();
        Integer totalWalkingCount = resetWalkingCountRequestDto.getTotalWalkingCount();
        LocalDateTime deviceBootedDt = resetWalkingCountRequestDto.getDeviceBootedDt();

        PlayerStepVo playerStepVo = playerService.resetWalkingCount(deviceId, accountId, totalWalkingCount, deviceBootedDt);

        ResetWalkingCountResponseDto resetWalkingCountResponseDto = ResetWalkingCountResponseDto.builder()
                .consumeWalkingCount(playerStepVo.getConsumeWalkingCount())
                .walkingCount(playerStepVo.getWalkingCount())
                .build();

        return ResponseEntity.ok(PlayerResponse.USER_PLAYER_RESET_WALKING_COUNT.toResponseDto(resetWalkingCountResponseDto));
    }

    @PostMapping("/exchange/walking")
    public ResponseEntity<ResponseDto<ExchangeWalkingCountResponseDto>> exchangeWalkingCount(@AuthenticationPrincipal Passport passport, @RequestBody ExchangeWalkingCountRequestDto exchangeWalkingCountRequestDto) {

        String deviceId = passport.getDeviceId();
        Long accountId = passport.getAccountId();
        Long mongId = exchangeWalkingCountRequestDto.getMongId();
        Integer totalWalkingCount = exchangeWalkingCountRequestDto.getTotalWalkingCount();
        Integer walkingCount = exchangeWalkingCountRequestDto.getWalkingCount();
        LocalDateTime deviceBootedDt = exchangeWalkingCountRequestDto.getDeviceBootedDt();

        PlayerStepVo playerStepVo = playerService.exchangeWalkingCount(deviceId, accountId, mongId, totalWalkingCount, walkingCount, deviceBootedDt);

        ExchangeWalkingCountResponseDto exchangeWalkingCountResponseDto = ExchangeWalkingCountResponseDto.builder()
                .consumeWalkingCount(playerStepVo.getConsumeWalkingCount())
                .walkingCount(playerStepVo.getWalkingCount())
                .build();

        return ResponseEntity.ok(PlayerResponse.USER_PLAYER_DECREASE_WALKING_COUNT.toResponseDto(exchangeWalkingCountResponseDto));
    }
}
