package com.mongs.member.domain.member.controller;

import com.mongs.core.enums.member.SlotCountCode;
import com.mongs.member.domain.member.service.vo.FindMemberVo;
import com.mongs.member.domain.member.service.vo.ModifySlotCountVo;
import com.mongs.member.domain.member.service.vo.RegisterMemberVo;
import com.mongs.member.domain.member.controller.dto.request.ModifyMemberReqDto;
import com.mongs.member.domain.member.controller.dto.response.FindMemberResDto;
import com.mongs.member.domain.member.controller.dto.response.ModifySlotCountResDto;
import com.mongs.member.domain.member.controller.dto.response.RegisterMemberResDto;
import com.mongs.member.domain.member.service.MemberService;
import com.mongs.core.security.principal.PassportDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
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
                .startPoint(findMemberVo.startPoint())
                .build());
    }

    @PostMapping("/admin/{accountId}")
    public ResponseEntity<RegisterMemberResDto> registerMember(@PathVariable("accountId") Long accountId) {
        RegisterMemberVo registerMemberVo = memberService.registerMember(accountId);
        return ResponseEntity.ok().body(RegisterMemberResDto.builder()
                .accountId(registerMemberVo.accountId())
                .maxSlot(registerMemberVo.maxSlot())
                .startPoint(registerMemberVo.startPoint())
                .build());
    }

    @PutMapping("/admin/{accountId}")
    public ResponseEntity<ModifySlotCountResDto> modifySlotCount(
            @PathVariable("accountId") Long accountId,
            @RequestBody @Validated ModifyMemberReqDto modifyMemberReqDto
    ) {
        SlotCountCode slotCountCode = modifyMemberReqDto.slotCountCode();

        ModifySlotCountVo modifySlotCountVo = memberService.modifySlotCount(accountId, slotCountCode);

        return ResponseEntity.ok().body(ModifySlotCountResDto.builder()
                .accountId(modifySlotCountVo.accountId())
                .maxSlot(modifySlotCountVo.maxSlot())
                .startPoint(modifySlotCountVo.startPoint())
                .build());
    }
}
