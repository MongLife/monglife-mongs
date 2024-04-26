package com.mongs.member.domain.member.service;

import com.mongs.member.domain.member.exception.InvalidModifySlotCountException;
import com.mongs.member.domain.member.service.vo.ChargeStarPointVo;
import com.mongs.member.domain.member.service.vo.ExchangeStarPointVo;
import com.mongs.member.domain.member.service.vo.FindMemberVo;
import com.mongs.member.domain.member.service.vo.AddSlotCountVo;
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
    private Integer BUY_SLOT_PRICE;

    @Value("${application.max-slot-count}")
    private Integer MAX_SLOT_COUNT;

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
                .buySlotPrice(BUY_SLOT_PRICE)
                .build();
    }

    public AddSlotCountVo addSlotCount(Long accountId, int slotCount) {
        Member member = memberRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .orElseThrow(() -> new NotFoundMemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        if (member.getStarPoint() < BUY_SLOT_PRICE * slotCount) {
            throw new InvalidModifySlotCountException(MemberErrorCode.INVALID_MODIFY_SLOT_COUNT);
        }
        if (member.getMaxSlot() >= MAX_SLOT_COUNT) {
            throw new InvalidModifySlotCountException(MemberErrorCode.INVALID_MODIFY_SLOT_COUNT);
        }

        Member modifiedMember = memberRepository.save(member.toBuilder()
                .maxSlot(member.getMaxSlot() + slotCount)
                .starPoint(member.getStarPoint() - BUY_SLOT_PRICE * slotCount)
                .build());

        return AddSlotCountVo.builder()
                .accountId(modifiedMember.getAccountId())
                .maxSlot(modifiedMember.getMaxSlot())
                .starPoint(modifiedMember.getStarPoint())
                .build();
    }

    public ChargeStarPointVo chargeStarPoint(Long accountId, int starPoint) {
        Member member = memberRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .orElseThrow(() -> new NotFoundMemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        // TODO("구글 플레이 영수증 검증 로직")

        Member modifiedMember = memberRepository.save(member.toBuilder()
                .starPoint(member.getStarPoint() + starPoint)
                .build());

        return ChargeStarPointVo.builder()
                .accountId(modifiedMember.getAccountId())
                .starPoint(modifiedMember.getStarPoint())
                .build();
    }

    public ExchangeStarPointVo exchangeStarPoint(Long accountId, Long mongId, int starPoint) {
        Member member = memberRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .orElseThrow(() -> new NotFoundMemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        if (member.getStarPoint() < starPoint) {
            throw new InvalidModifySlotCountException(MemberErrorCode.INVALID_MODIFY_STAR_POINT);
        }
        
        // TODO("몽에 해당하는 paypoint 증가")

        Member modifiedMember = memberRepository.save(member.toBuilder()
                .starPoint(member.getStarPoint() - starPoint)
                .build());

        return ExchangeStarPointVo.builder()
                .accountId(modifiedMember.getAccountId())
                .starPoint(modifiedMember.getStarPoint())
                .build();
        
    }
}
