package com.mongs.play.domain.member.service;

import com.mongs.play.core.error.domain.MemberErrorCode;
import com.mongs.play.core.exception.common.InvalidException;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.member.entity.Member;
import com.mongs.play.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Value("${env.member.buy-slot-price}")
    private Integer buySlotPrice;

    @Value("${env.member.max-slot-count}")
    private Integer maxSlotCount;

    private final MemberRepository memberRepository;

    public Member addMember(Member member) {
        return memberRepository.save(member);
    }

    public Member getMember(Long accountId) {
        return memberRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .orElseGet(() -> this.addMember(Member.builder()
                        .accountId(accountId)
                        .build()));
    }

    public Member modifyIncreaseSlotCount(Long accountId, Integer slotCount) throws NotFoundException {

        Member member = memberRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .orElseThrow(() -> new NotFoundException(MemberErrorCode.NOT_FOUND_MEMBER));

        if (member.getStarPoint() < buySlotPrice) {
            throw new InvalidException(MemberErrorCode.NOT_ENOUGH_STAR_POINT);
        }
        if (member.getSlotCount() >= maxSlotCount) {
            throw new InvalidException(MemberErrorCode.ALREADY_MAX_SLOT_COUNT);
        }

        return memberRepository.save(member.toBuilder()
                .starPoint(member.getStarPoint() - buySlotPrice)
                .slotCount(member.getSlotCount() + slotCount)
                .build());
    }

    public Member modifyDecreaseSlotCount(Long accountId, Integer slotCount) throws NotFoundException {

        Member member = memberRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .orElseThrow(() -> new NotFoundException(MemberErrorCode.NOT_FOUND_MEMBER));

        if (member.getSlotCount() == 1) {
            throw new InvalidException(MemberErrorCode.ALREADY_MIN_SLOT_COUNT);
        }

        return memberRepository.save(member.toBuilder()
                .starPoint(member.getStarPoint() + buySlotPrice)
                .slotCount(member.getSlotCount() - slotCount)
                .build());

    }

    public Member modifyIncreaseStarPoint(Long accountId, Integer starPoint) throws NotFoundException {

        Member member = memberRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .orElseThrow(() -> new NotFoundException(MemberErrorCode.NOT_FOUND_MEMBER));

        return memberRepository.save(member.toBuilder()
                .starPoint(member.getStarPoint() + starPoint)
                .build());
    }

    public Member modifyDecreaseStarPoint(Long accountId, Integer starPoint) throws NotFoundException {

        Member member = memberRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .orElseThrow(() -> new NotFoundException(MemberErrorCode.NOT_FOUND_MEMBER));

        if (member.getStarPoint() <= 0) {
            throw new InvalidException(MemberErrorCode.NOT_ENOUGH_STAR_POINT);
        }

        return memberRepository.save(member.toBuilder()
                .starPoint(member.getStarPoint() - starPoint)
                .build());
    }
}
