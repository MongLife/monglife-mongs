package com.monglife.mongs.adapter.in.member.web.player.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.common.security.principal.Passport;
import com.monglife.mongs.adapter.in.member.web.enums.AdapterInWebMemberResponse;
import com.monglife.mongs.adapter.in.member.web.player.dto.request.ExchangeStarPointRequestDto;
import com.monglife.mongs.adapter.in.member.web.player.dto.response.BuySlotResponseDto;
import com.monglife.mongs.adapter.in.member.web.player.dto.response.ExchangeStarPointResponseDto;
import com.monglife.mongs.adapter.in.member.web.player.dto.response.GetPlayerResponseDto;
import com.monglife.mongs.application.member.port.in.PlayerUseCase;
import com.monglife.mongs.application.member.port.in.command.BuySlotCommand;
import com.monglife.mongs.application.member.port.in.command.CreatePlayerCommand;
import com.monglife.mongs.application.member.port.in.command.ExchangeStarPointCommand;
import com.monglife.mongs.application.member.port.in.command.GetPlayerCommand;
import com.monglife.mongs.domain.model.Player;
import jakarta.validation.Valid;
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

    private final PlayerUseCase playerUseCase;

    /**
     * 플레이어 등록
     */
    @PostMapping("")
    public ResponseEntity<ResponseDto<?>> createPlayer(
            @AuthenticationPrincipal Passport passport
    ) {

        CreatePlayerCommand command = CreatePlayerCommand.builder()
                .accountId(passport.getAccountId())
                .build();

        playerUseCase.createPlayerUseCase(command);

        return ResponseEntity.ok(AdapterInWebMemberResponse.CREATE_PLAYER.toResponseDto());
    }

    /**
     * 플레이어 조회
     */
    @GetMapping("")
    public ResponseEntity<ResponseDto<GetPlayerResponseDto>> getPlayer(
            @AuthenticationPrincipal Passport passport
    ) {

        GetPlayerCommand command = GetPlayerCommand.builder()
                .accountId(passport.getAccountId())
                .build();

        Player player = playerUseCase.getPlayerUseCase(command);

        GetPlayerResponseDto getPlayerResponseDto = GetPlayerResponseDto.builder()
                .accountId(player.getAccountId())
                .slotCount(player.getSlotCount())
                .starPoint(player.getStarPoint())
                .build();

        return ResponseEntity.ok(AdapterInWebMemberResponse.GET_PLAYER.toResponseDto(getPlayerResponseDto));
    }

    /**
     * 슬롯 구매
     */
    @PatchMapping("/slot")
    public ResponseEntity<ResponseDto<BuySlotResponseDto>> buySlot(
            @AuthenticationPrincipal Passport passport
    ) {

        BuySlotCommand command = BuySlotCommand.builder()
                .accountId(passport.getAccountId())
                .build();

        Player player = playerUseCase.buySlotUseCase(command);

        BuySlotResponseDto buySlotResponseDto = BuySlotResponseDto.builder()
                .accountId(player.getAccountId())
                .slotCount(player.getSlotCount())
                .starPoint(player.getStarPoint())
                .build();

        return ResponseEntity.ok(AdapterInWebMemberResponse.BUY_SLOT.toResponseDto(buySlotResponseDto));
    }

    /**
     * 스타 포인트 환전
     */
    @PostMapping("/exchange/starPoint")
    public ResponseEntity<ResponseDto<ExchangeStarPointResponseDto>> exchangeStarPoint(
            @AuthenticationPrincipal Passport passport,
            @Valid @RequestBody ExchangeStarPointRequestDto exchangeStarPointRequestDto
    ) {

        ExchangeStarPointCommand command = ExchangeStarPointCommand.builder()
                .accountId(passport.getAccountId())
                .mongId(exchangeStarPointRequestDto.getMongId())
                .starPoint(exchangeStarPointRequestDto.getStarPoint())
                .build();

        Player player = playerUseCase.exchangeStarPointUseCase(command);

        ExchangeStarPointResponseDto exchangeStarPointResponseDto = ExchangeStarPointResponseDto.builder()
                .accountId(player.getAccountId())
                .starPoint(player.getStarPoint())
                .build();

        return ResponseEntity.ok(AdapterInWebMemberResponse.EXCHANGE_STAR_POINT.toResponseDto(exchangeStarPointResponseDto));
    }
}
