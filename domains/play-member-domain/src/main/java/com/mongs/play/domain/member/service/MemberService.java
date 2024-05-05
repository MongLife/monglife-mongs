package com.mongs.play.domain.member.service;

import com.mongs.play.core.error.domain.MemberErrorCode;
import com.mongs.play.core.exception.domain.InvalidException;
import com.mongs.play.core.exception.domain.NotFoundException;
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

    public Member modifyIncrementSlotCount(Long accountId, Integer addSlotCount) throws NotFoundException {

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
                .slotCount(member.getSlotCount() + addSlotCount)
                .build());
    }

    public Member modifyDecrementSlotCount(Long accountId, Integer subSlotCount) throws NotFoundException {

        Member member = memberRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .orElseThrow(() -> new NotFoundException(MemberErrorCode.NOT_FOUND_MEMBER));

        if (member.getSlotCount() == 1) {
            throw new InvalidException(MemberErrorCode.ALREADY_MIN_SLOT_COUNT);
        }

        return memberRepository.save(member.toBuilder()
                .starPoint(member.getStarPoint() + buySlotPrice)
                .slotCount(member.getSlotCount() - subSlotCount)
                .build());

    }

    public Member modifyIncrementStarPoint(Long accountId, Integer addStarPoint) throws NotFoundException {

        Member member = memberRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .orElseThrow(() -> new NotFoundException(MemberErrorCode.NOT_FOUND_MEMBER));

        return memberRepository.save(member.toBuilder()
                .starPoint(member.getStarPoint() + addStarPoint)
                .build());
    }

    public Member modifyDecrementStarPoint(Long accountId, Integer subStarPoint) throws NotFoundException {

        Member member = memberRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .orElseThrow(() -> new NotFoundException(MemberErrorCode.NOT_FOUND_MEMBER));

        if (member.getStarPoint() <= 0) {
            throw new InvalidException(MemberErrorCode.NOT_ENOUGH_STAR_POINT);
        }

        return memberRepository.save(member.toBuilder()
                .starPoint(member.getStarPoint() - subStarPoint)
                .build());
    }
}
