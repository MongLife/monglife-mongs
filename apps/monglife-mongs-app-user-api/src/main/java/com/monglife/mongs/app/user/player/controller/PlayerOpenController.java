package com.monglife.mongs.app.user.player.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.user.player.dto.etc.GetPlayerDto;
import com.monglife.mongs.app.user.player.dto.request.ExchangeStarPointRequestDto;
import com.monglife.mongs.app.user.player.dto.request.ExchangeWalkingCountRequestDto;
import com.monglife.mongs.app.user.player.dto.request.SyncWalkingCountRequestDto;
import com.monglife.mongs.app.user.player.dto.response.ExchangeWalkingCountResponseDto;
import com.monglife.mongs.app.user.player.dto.response.GetPlayerResponseDto;
import com.monglife.mongs.app.user.player.dto.response.SyncWalkingCountResponseDto;
import com.monglife.mongs.app.user.player.enums.PlayerResponse;
import com.monglife.mongs.app.user.player.service.PlayerService;
import com.monglife.mongs.app.user.player.vo.PlayerStepVo;
import com.monglife.mongs.module.security.global.principal.Passport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Validated
@RestController
@RequestMapping("/player/open")
@RequiredArgsConstructor
public class PlayerOpenController {

    private final PlayerService playerService;

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

        return ResponseEntity.ok(PlayerResponse.APP_USER_PLAYER_SYNC_WALKING_COUNT.toResponseDto(syncWalkingCountResponseDto));
    }
}
