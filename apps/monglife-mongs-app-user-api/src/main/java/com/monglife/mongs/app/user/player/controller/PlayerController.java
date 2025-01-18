package com.monglife.mongs.app.user.player.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.user.player.dto.etc.GetPlayerDto;
import com.monglife.mongs.app.user.player.dto.request.ExchangeStarPointRequestDto;
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
@RequestMapping("/player")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    /**
     * 플레이어 정보 등록
     * @param passport 패스 포트
     * @return 성공 응답
     */
    @PostMapping("")
    public ResponseEntity<ResponseDto<?>> createPlayer(@AuthenticationPrincipal Passport passport) {

        Long accountId = passport.getAccountId();

        playerService.createPlayer(accountId);

        return ResponseEntity.ok(PlayerResponse.APP_USER_PLAYER_CREATE_PLAYER.toResponseDto());
    }

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

    /**
     * 슬롯 구매
     * @param passport 패스 포트
     * @return 성공 응답
     */
    @PatchMapping("/slot")
    public ResponseEntity<ResponseDto<?>> buySlot(@AuthenticationPrincipal Passport passport) {

        playerService.buySlot(passport.getAccountId());

        return ResponseEntity.ok(PlayerResponse.APP_USER_PLAYER_INCREASE_SLOT.toResponseDto());
    }

    /**
     * 스타 포인트 환전
     * @param passport 패스 포트
     * @param exchangeStarPointRequestDto 스타 포인트 환전 요청 Dto
     * @return 성공 응답
     */
    @PostMapping("/exchange/starPoint")
    public ResponseEntity<ResponseDto<?>> exchangeStarPoint(@AuthenticationPrincipal Passport passport, @RequestBody ExchangeStarPointRequestDto exchangeStarPointRequestDto) {

        Long accountId = passport.getAccountId();
        Long mongId = exchangeStarPointRequestDto.getMongId();
        Integer starPoint = exchangeStarPointRequestDto.getStarPoint();

        playerService.exchangeStarPoint(accountId, mongId, starPoint);

        return ResponseEntity.ok(PlayerResponse.APP_USER_PLAYER_EXCHANGE_STAR_POINT.toResponseDto());
    }
}
