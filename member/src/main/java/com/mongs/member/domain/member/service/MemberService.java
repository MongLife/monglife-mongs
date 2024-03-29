package com.mongs.member.domain.member.service;

import com.mongs.core.enums.member.SlotCountCode;
import com.mongs.member.domain.member.service.vo.FindMemberVo;
import com.mongs.member.domain.member.service.vo.ModifySlotCountVo;
import com.mongs.member.domain.member.entity.Member;
import com.mongs.member.domain.member.exception.MemberErrorCode;
import com.mongs.member.domain.member.exception.NotFoundMemberException;
import com.mongs.member.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public FindMemberVo findMember(Long accountId) {
        Member member = memberRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .accountId(accountId)
                        .build()));

        return FindMemberVo.builder()
                .accountId(member.getAccountId())
                .maxSlot(member.getMaxSlot())
                .starPoint(member.getStarPoint())
                .build();
    }

    public ModifySlotCountVo modifySlotCount(Long accountId, SlotCountCode slotCountCode) {
        Member member = memberRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .orElseThrow(() -> new NotFoundMemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        int slotCount = slotCountCode.getCount();

        Member modifiedMember = memberRepository.save(member.toBuilder()
                .maxSlot(slotCount)
                .build());

        return ModifySlotCountVo.builder()
                .accountId(modifiedMember.getAccountId())
                .maxSlot(modifiedMember.getMaxSlot())
                .starPoint(modifiedMember.getStarPoint())
                .build();
    }
}
