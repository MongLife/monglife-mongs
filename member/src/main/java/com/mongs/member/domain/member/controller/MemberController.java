package com.mongs.member.domain.member.controller;

import com.mongs.core.code.enums.member.SlotCountCode;
import com.mongs.member.domain.member.dto.request.ModifyMemberReqDto;
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
    public ResponseEntity<Object> findMember(@AuthenticationPrincipal PassportDetail passportDetail) {
        Long accountId = passportDetail.getId();
        return ResponseEntity.ok().body(memberService.findMember(accountId));
    }

    @PostMapping("/{accountId}")
    public ResponseEntity<Object> registerMember(@PathVariable Long accountId) {
        return ResponseEntity.ok().body(memberService.registerMember(accountId));
    }

    @PutMapping("")
    public ResponseEntity<Object> modifySlotCount(
            @AuthenticationPrincipal PassportDetail passportDetail,
            @RequestBody @Validated ModifyMemberReqDto modifyMemberReqDto
    ) {
        Long accountId = passportDetail.getId();
        SlotCountCode slotCountCode = modifyMemberReqDto.slotCountCode();

        return ResponseEntity.ok().body(memberService.modifySlotCount(accountId, slotCountCode));
    }

    @DeleteMapping("")
    public ResponseEntity<Object> removeMember(@AuthenticationPrincipal PassportDetail passportDetail) {
        Long accountId = passportDetail.getId();
        return ResponseEntity.ok().body(memberService.removeMember(accountId));
    }
}
