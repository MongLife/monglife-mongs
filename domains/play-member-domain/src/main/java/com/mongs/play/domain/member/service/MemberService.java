package com.mongs.play.domain.member.service;

import com.mongs.play.client.publisher.event.annotation.RealTimeMember;
import com.mongs.play.client.publisher.event.code.PublishEventCode;
import com.mongs.play.core.error.domain.MemberErrorCode;
import com.mongs.play.core.exception.common.InvalidException;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.member.entity.Member;
import com.mongs.play.domain.member.repository.MemberRepository;
import com.mongs.play.domain.member.vo.MemberVo;
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

    @Transactional(transactionManager = "memberTransactionManager")
    public MemberVo getMember(Long accountId) {
        Member member = memberRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .orElseGet(() -> memberRepository.save(Member.builder().accountId(accountId).build()));
        return MemberVo.of(member);
    }

    @Transactional(transactionManager = "memberTransactionManager")
    public MemberVo increaseSlotCount(Long accountId, Integer slotCount) throws NotFoundException {

        Member member = memberRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .orElseGet(() -> Member.builder().accountId(accountId).build());

        int starPoint = member.getStarPoint() - (buySlotPrice * slotCount);

        if (starPoint < 0) {
            throw new InvalidException(MemberErrorCode.NOT_ENOUGH_STAR_POINT);
        }
        if (member.getSlotCount() >= maxSlotCount) {
            throw new InvalidException(MemberErrorCode.ALREADY_MAX_SLOT_COUNT);
        }

        member = memberRepository.save(member.toBuilder()
                .starPoint(starPoint)
                .slotCount(member.getSlotCount() + slotCount)
                .build());

        return MemberVo.of(member);
    }

    @Transactional(transactionManager = "memberTransactionManager")
    public MemberVo decreaseSlotCount(Long accountId, Integer slotCount) throws NotFoundException {

        Member member = memberRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .orElseGet(() -> Member.builder().accountId(accountId).build());

        if (member.getSlotCount() == 1) {
            throw new InvalidException(MemberErrorCode.ALREADY_MIN_SLOT_COUNT);
        }

        member = memberRepository.save(member.toBuilder()
                .starPoint(member.getStarPoint() + buySlotPrice)
                .slotCount(member.getSlotCount() - slotCount)
                .build());

        return MemberVo.of(member);
    }

    @RealTimeMember(codes = { PublishEventCode.MEMBER_STAR_POINT })
    @Transactional(transactionManager = "memberTransactionManager")
    public MemberVo increaseStarPoint(Long accountId, Integer addStarPoint) throws NotFoundException {

        Member member = memberRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .orElseGet(() -> Member.builder().accountId(accountId).build());

        int starPoint = member.getStarPoint() + addStarPoint;

        member = memberRepository.save(member.toBuilder()
                .starPoint(starPoint)
                .build());

        return MemberVo.of(member);
    }

    @RealTimeMember(codes = { PublishEventCode.MEMBER_STAR_POINT })
    @Transactional(transactionManager = "memberTransactionManager")
    public MemberVo decreaseStarPoint(Long accountId, Integer subStarPoint) throws NotFoundException {

        Member member = memberRepository.findByAccountIdAndIsDeletedIsFalse(accountId)
                .orElseGet(() -> Member.builder().accountId(accountId).build());

        int starPoint = member.getStarPoint() - subStarPoint;

        if (starPoint < 0) {
            throw new InvalidException(MemberErrorCode.NOT_ENOUGH_STAR_POINT);
        }

        member = memberRepository.save(member.toBuilder()
                .starPoint(starPoint)
                .build());

        return MemberVo.of(member);
    }
}
