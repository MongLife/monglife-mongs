package com.monglife.mongs.app.user.player.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.user.player.dto.etc.GetPlayerDto;
import com.monglife.mongs.app.user.player.dto.request.ExchangeStarPointRequestDto;
import com.monglife.mongs.app.user.player.dto.request.ExchangeWalkingCountRequestDto;
import com.monglife.mongs.app.user.player.dto.request.ChargeStarPointRequestDto;
import com.monglife.mongs.app.user.player.dto.request.ChargeWalkingCountRequestDto;
import com.monglife.mongs.app.user.player.dto.response.GetPlayerResponseDto;
import com.monglife.mongs.app.user.player.enums.PlayerResponse;
import com.monglife.mongs.app.user.player.service.PlayerService;
import com.monglife.mongs.module.security.global.principal.Passport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
                .walkingCount(getPlayerDto.getWalkingCount())
                .build();

        return ResponseEntity.ok(PlayerResponse.USER_PLAYER_GET_PLAYER.toResponseDto(getPlayerResponseDto));
    }

    @PatchMapping("/slot")
    public ResponseEntity<ResponseDto<?>> increaseSlot(@AuthenticationPrincipal Passport passport) {

        playerService.increaseSlot(passport.getAccountId());

        return ResponseEntity.ok(PlayerResponse.USER_PLAYER_INCREASE_SLOT.toResponseDto());
    }

    @PostMapping("/charge/starPoint")
    public ResponseEntity<ResponseDto<?>> chargeStarPoint(@AuthenticationPrincipal Passport passport, @RequestBody ChargeStarPointRequestDto chargeStarPointRequestDto) {

        Long accountId = passport.getAccountId();
        String receipt = chargeStarPointRequestDto.getReceipt();
        Integer starPoint = chargeStarPointRequestDto.getStarPoint();

        playerService.chargeStarPoint(accountId, receipt, starPoint);

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

    @PatchMapping("/charge/walking")
    public ResponseEntity<ResponseDto<?>> chargeWalkingCount(@AuthenticationPrincipal Passport passport, @RequestBody ChargeWalkingCountRequestDto chargeWalkingCountRequestDto) {

        Long accountId = passport.getAccountId();
        Integer walkingCount = chargeWalkingCountRequestDto.getWalkingCount();

        playerService.chargeWalkingCount(accountId, walkingCount);

        return ResponseEntity.ok(PlayerResponse.USER_PLAYER_INCREASE_WALKING_COUNT.toResponseDto());
    }

    @PostMapping("/exchange/walking")
    public ResponseEntity<ResponseDto<?>> exchangeWalkingCount(@AuthenticationPrincipal Passport passport, @RequestBody ExchangeWalkingCountRequestDto exchangeWalkingCountRequestDto) {

        Long accountId = passport.getAccountId();
        Long mongId = exchangeWalkingCountRequestDto.getMongId();
        Integer walkingCount = exchangeWalkingCountRequestDto.getWalkingCount();

        playerService.exchangeWalkingCount(accountId, mongId, walkingCount);

        return ResponseEntity.ok(PlayerResponse.USER_PLAYER_DECREASE_WALKING_COUNT.toResponseDto());
    }
}
