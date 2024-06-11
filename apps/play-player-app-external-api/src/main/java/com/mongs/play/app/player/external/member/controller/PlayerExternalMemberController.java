package com.mongs.play.app.player.external.member.controller;

import com.mongs.play.app.player.external.member.dto.req.ChargeStarPointReqDto;
import com.mongs.play.app.player.external.member.dto.req.ExchangePayPointReqDto;
import com.mongs.play.app.player.external.member.dto.req.ExchangePayPointWalkingReqDto;
import com.mongs.play.app.player.external.member.dto.res.*;
import com.mongs.play.app.player.external.member.service.PlayerExternalMemberService;
import com.mongs.play.app.player.external.member.vo.*;
import com.mongs.play.module.feign.service.ManagementInternalFeignService;
import com.mongs.play.module.security.principal.PassportDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/member")
@RequiredArgsConstructor
@RestController
public class PlayerExternalMemberController {

    private final PlayerExternalMemberService playerExternalMemberService;

    @GetMapping("")
    public ResponseEntity<FindMemberResDto> findMember(@AuthenticationPrincipal PassportDetail passportDetail) {

        Long accountId = passportDetail.getId();

        FindMemberVo findMemberVo = playerExternalMemberService.findMember(accountId);

        return ResponseEntity.ok().body(FindMemberResDto.builder()
                .accountId(findMemberVo.accountId())
                .maxSlot(findMemberVo.maxSlot())
                .starPoint(findMemberVo.starPoint())
                .build());
    }

    @GetMapping("/charge")
    public ResponseEntity<List<FindChargeItemResDto>> findChargeItem() {

        List<FindChargeItemVo> findChargeItemVoList = playerExternalMemberService.findChargeItem();

        return ResponseEntity.ok().body(FindChargeItemResDto.toList(findChargeItemVoList));
    }

    @GetMapping("/exchange")
    public ResponseEntity<List<FindExchangeItemResDto>> findExchangeItem() {

        List<FindExchangeItemVo> findExchangeItemVoList = playerExternalMemberService.findExchangeItem();

        return ResponseEntity.ok().body(FindExchangeItemResDto.toList(findExchangeItemVoList));
    }

    @PutMapping("/slot")
    public ResponseEntity<BuySlotResDto> buySlot(@AuthenticationPrincipal PassportDetail passportDetail) {

        Long accountId = passportDetail.getId();
        String deviceId = passportDetail.getDeviceId();

        BuySlotVo buySlotVo = playerExternalMemberService.buySlot(accountId, deviceId);

        return ResponseEntity.ok().body(BuySlotResDto.builder()
                .accountId(buySlotVo.accountId())
                .maxSlot(buySlotVo.maxSlot())
                .starPoint(buySlotVo.starPoint())
                .build());
    }

    @PostMapping("/charge/starPoint")
    public ResponseEntity<IncreaseStarPointResDto> chargeStarPoint(
            @AuthenticationPrincipal PassportDetail passportDetail,
            @RequestBody ChargeStarPointReqDto chargeStarPointReqDto
    ) {

        Long accountId = passportDetail.getId();
        String deviceId = passportDetail.getDeviceId();
        String receipt = chargeStarPointReqDto.receipt();
        String chargeItemId = chargeStarPointReqDto.chargeItemId();

        ChargeStarPointVo chargeStarPointVo = playerExternalMemberService.chargeStarPoint(accountId, deviceId, receipt, chargeItemId);

        return ResponseEntity.ok().body(IncreaseStarPointResDto.builder()
                .accountId(chargeStarPointVo.accountId())
                .starPoint(chargeStarPointVo.starPoint())
                .build());
    }

    @PutMapping("/exchange/payPoint")
    public ResponseEntity<ExchangePayPointResDto> exchangePayPoint(
            @AuthenticationPrincipal PassportDetail passportDetail,
            @RequestBody ExchangePayPointReqDto exchangePayPointReqDto
    ) {

        Long accountId = passportDetail.getId();
        String deviceId = passportDetail.getDeviceId();
        Long mongId = exchangePayPointReqDto.mongId();
        String exchangeItemId = exchangePayPointReqDto.exchangeItemId();

        ExchangePayPointVo exchangePayPointVo = playerExternalMemberService.exchangePayPoint(accountId, deviceId, mongId, exchangeItemId);

        return ResponseEntity.ok().body(ExchangePayPointResDto.builder()
                .accountId(exchangePayPointVo.accountId())
                .mongId(exchangePayPointVo.mongId())
                .starPoint(exchangePayPointVo.starPoint())
                .build());
    }

    @PutMapping("/exchange/payPoint/walking")
    public ResponseEntity<ExchangePayPointWalkingResDto> exchangePayPoint(
            @AuthenticationPrincipal PassportDetail passportDetail,
            @RequestBody ExchangePayPointWalkingReqDto exchangePayPointWalkingReqDto
    ) {

        Long accountId = passportDetail.getId();
        String deviceId = passportDetail.getDeviceId();
        Long mongId = exchangePayPointWalkingReqDto.mongId();
        Integer walkingCount = exchangePayPointWalkingReqDto.walkingCount();

        ExchangePayPointWalkingVo exchangePayPointWalkingVo = playerExternalMemberService.exchangePayPointWalking(accountId, deviceId, mongId, walkingCount);

        return ResponseEntity.ok().body(ExchangePayPointWalkingResDto.builder()
                .accountId(exchangePayPointWalkingVo.accountId())
                .mongId(exchangePayPointWalkingVo.mongId())
                .subWalkingCount(exchangePayPointWalkingVo.subWalkingCount())
                .build());
    }
}
