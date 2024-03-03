package com.mongs.member.domain.member.service;

import com.mongs.core.code.enums.member.SlotCountCode;
import com.mongs.member.domain.member.dto.response.FindMemberResDto;
import com.mongs.member.domain.member.dto.response.ModifyMemberResDto;
import com.mongs.member.domain.member.dto.response.RegisterMemberResDto;
import com.mongs.member.domain.member.dto.response.RemoveMemberResDto;
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

    public FindMemberResDto findMember(Long accountId) {
        Member member = memberRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundMemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        return FindMemberResDto.of(member);
    }

    public RegisterMemberResDto registerMember(Long accountId) {
        Member savedMember = memberRepository.findById(accountId)
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .accountId(accountId)
                        .build()));

        return RegisterMemberResDto.of(savedMember);
    }

    public ModifyMemberResDto modifySlotCount(Long accountId, SlotCountCode slotCountCode) {
        int slotCount = 0;

        switch (slotCountCode) {
            case NORMAL -> slotCount = 1;
            case SPECIAL -> slotCount = 3;
            case ADMIN -> slotCount = 10;
        }

        Member member = memberRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundMemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        Member modifiedMember = memberRepository.save(member.toBuilder()
                .maxSlot(slotCount)
                .build());

        return ModifyMemberResDto.of(modifiedMember);
    }

    public RemoveMemberResDto removeMember(Long accountId) {
        Member member = memberRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundMemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        Member deletedMember = memberRepository.save(member.toBuilder()
                .isDeleted(true)
                .build());

        return RemoveMemberResDto.of(deletedMember);
    }
}
