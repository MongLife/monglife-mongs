package com.mongs.member.domain.member.service;

import com.mongs.member.domain.member.exception.InvalidModifySlotCountException;
import com.mongs.member.domain.member.service.vo.FindMemberVo;
import com.mongs.member.domain.member.service.vo.ModifySlotCountVo;
import com.mongs.member.domain.member.entity.Member;
import com.mongs.member.domain.member.exception.MemberErrorCode;
import com.mongs.member.domain.member.exception.NotFoundMemberException;
import com.mongs.member.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Value("${application.buy-slot-price}")
    private Integer buySlotPrice;

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

    public ModifySlotCountVo modifySlotCount(Long accountId, int slotCount) {
        Member member = memberRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .orElseThrow(() -> new NotFoundMemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        if (member.getStarPoint() < buySlotPrice) {
            throw new InvalidModifySlotCountException(MemberErrorCode.INVALID_MODIFY_SLOT_COUNT);
        }

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
