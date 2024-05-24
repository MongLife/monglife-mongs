package com.mongs.play.domain.member.service;

import com.mongs.play.core.error.domain.MemberErrorCode;
import com.mongs.play.core.exception.common.InvalidException;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.member.entity.Member;
import com.mongs.play.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Value("${env.member.buy-slot-price}")
    private Integer buySlotPrice;

    @Value("${env.member.max-slot-count}")
    private Integer maxSlotCount;

    private final MemberRepository memberRepository;

    @Transactional
    public Member addMember(Member member) {
        return memberRepository.save(member);
    }

    @Transactional
    public Member getMember(Long accountId) {
        return memberRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .orElseGet(() -> this.addMember(Member.builder()
                        .accountId(accountId)
                        .build()));
    }

    @Transactional
    public Member increaseSlotCount(Long accountId, Integer slotCount) throws NotFoundException {

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

    @Transactional
    public Member decreaseSlotCount(Long accountId, Integer slotCount) throws NotFoundException {

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

    @Transactional
    public Member increaseStarPoint(Long accountId, Integer starPoint) throws NotFoundException {

        Member member = memberRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .orElseThrow(() -> new NotFoundException(MemberErrorCode.NOT_FOUND_MEMBER));

        return memberRepository.save(member.toBuilder()
                .starPoint(member.getStarPoint() + starPoint)
                .build());
    }

    @Transactional
    public Member decreaseStarPoint(Long accountId, Integer starPoint) throws NotFoundException {

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
