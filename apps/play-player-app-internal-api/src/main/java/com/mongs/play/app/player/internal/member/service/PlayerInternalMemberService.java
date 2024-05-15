package com.mongs.play.app.player.internal.member.service;

import com.mongs.play.app.player.internal.member.vo.IncreaseStarPointVo;
import com.mongs.play.domain.member.entity.Member;
import com.mongs.play.domain.member.service.MemberService;
import com.mongs.play.domain.payment.entity.PaymentLog;
import com.mongs.play.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlayerInternalMemberService {

    private final PaymentService paymentService;
    private final MemberService memberService;

    @Transactional
    public IncreaseStarPointVo increaseStarPointMapCollection(Long accountId) {

        int starPoint = 10;
        PaymentLog paymentLog = paymentService.addIncreaseStarPointLog(accountId);
        Member member = memberService.increaseStarPoint(accountId, starPoint);
        paymentService.itemReward(paymentLog.getId());

        return IncreaseStarPointVo.builder()
                .accountId(member.getAccountId())
                .starPoint(member.getStarPoint())
                .build();
    }

    @Transactional
    public IncreaseStarPointVo increaseStarPointMongCollection(Long accountId) {

        int starPoint = 10;
        PaymentLog paymentLog = paymentService.addIncreaseStarPointLog(accountId);
        Member member = memberService.increaseStarPoint(accountId, starPoint);
        paymentService.itemReward(paymentLog.getId());

        return IncreaseStarPointVo.builder()
                .accountId(member.getAccountId())
                .starPoint(member.getStarPoint())
                .build();
    }
}
