package com.mongs.member.domain.member.controller;

import com.mongs.member.domain.member.controller.dto.response.ChargeStarPointResDto;
import com.mongs.member.domain.member.controller.dto.response.ExchangeStarPointResDto;
import com.mongs.member.domain.member.service.vo.ChargeStarPointVo;
import com.mongs.member.domain.member.service.vo.ExchangeStarPointVo;
import com.mongs.member.domain.member.service.vo.FindMemberVo;
import com.mongs.member.domain.member.service.vo.AddSlotCountVo;
import com.mongs.member.domain.member.controller.dto.response.FindMemberResDto;
import com.mongs.member.domain.member.controller.dto.response.AddSlotCountResDto;
import com.mongs.member.domain.member.service.MemberService;
import com.mongs.core.security.principal.PassportDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("")
    public ResponseEntity<FindMemberResDto> findMember(@AuthenticationPrincipal PassportDetail passportDetail) {
        FindMemberVo findMemberVo = memberService.findMember(passportDetail.getId());
        return ResponseEntity.ok().body(FindMemberResDto.builder()
                .accountId(findMemberVo.accountId())
                .maxSlot(findMemberVo.maxSlot())
                .starPoint(findMemberVo.starPoint())
                .buySlotPrice(findMemberVo.buySlotPrice())
                .build());
    }

    @PutMapping("/slot")
    public ResponseEntity<AddSlotCountResDto> addSlotCount(@AuthenticationPrincipal PassportDetail passportDetail) {

        AddSlotCountVo addSlotCountVo = memberService.addSlotCount(passportDetail.getId(), 1);

        return ResponseEntity.ok().body(AddSlotCountResDto.builder()
                .accountId(addSlotCountVo.accountId())
                .maxSlot(addSlotCountVo.maxSlot())
                .starPoint(addSlotCountVo.starPoint())
                .build());
    }

    @PostMapping("/starPoint")
    public ResponseEntity<ChargeStarPointResDto> chargeStartPoint(@AuthenticationPrincipal PassportDetail passportDetail) {

        ChargeStarPointVo chargeStarPointVo = memberService.chargeStarPoint(passportDetail.getId(), 10);

        return ResponseEntity.ok().body(ChargeStarPointResDto.builder()
                .accountId(chargeStarPointVo.accountId())
                .starPoint(chargeStarPointVo.starPoint())
                .build());
    }

    @PutMapping("/payPoint/{mongId}")
    public ResponseEntity<ExchangeStarPointResDto> exchangeStartPoint(
            @AuthenticationPrincipal PassportDetail passportDetail,
            @PathVariable("mongId") Long mongId
    ) {
        ExchangeStarPointVo exchangeStarPointVo = memberService.exchangeStarPoint(passportDetail.getId(), mongId, 10);

        return ResponseEntity.ok().body(ExchangeStarPointResDto.builder()
                .accountId(exchangeStarPointVo.accountId())
                .starPoint(exchangeStarPointVo.starPoint())
                .build());
    }
}
