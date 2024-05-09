package com.mongs.play.app.player.internal.member.service;

import com.mongs.play.app.player.internal.member.vo.IncreaseStarPointVo;
import com.mongs.play.domain.member.entity.Member;
import com.mongs.play.domain.member.service.MemberService;
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
    public IncreaseStarPointVo increaseStarPoint(Long accountId, Integer starPoint) {

        paymentService.addIncreaseStarPointLog(accountId);

        Member member = memberService.modifyIncreaseStarPoint(accountId, starPoint);

        return IncreaseStarPointVo.builder()
                .accountId(member.getAccountId())
                .starPoint(member.getStarPoint())
                .build();
    }
}
