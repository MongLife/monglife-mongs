package com.mongs.play.app.player.external.member.service;

import com.mongs.play.app.player.external.member.vo.*;
import com.mongs.play.client.publisher.annotation.RealTimeMember;
import com.mongs.play.client.publisher.code.PublishCode;
import com.mongs.play.domain.member.entity.Member;
import com.mongs.play.domain.member.service.MemberService;
import com.mongs.play.domain.payment.entity.ChargeItem;
import com.mongs.play.domain.payment.entity.ExchangeItem;
import com.mongs.play.domain.payment.entity.PaymentLog;
import com.mongs.play.domain.payment.service.ChargeItemService;
import com.mongs.play.domain.payment.service.ExchangeItemService;
import com.mongs.play.domain.payment.service.PaymentService;
import com.mongs.play.module.feign.dto.res.IncreasePayPointResDto;
import com.mongs.play.module.feign.service.ManagementInternalFeignService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerExternalMemberService {

    private final MemberService memberService;
    private final PaymentService paymentService;
    private final ChargeItemService chargeItemService;
    private final ExchangeItemService exchangeItemService;
    private final ManagementInternalFeignService managementInternalFeignService;

    @Transactional
    public FindMemberVo findMember(Long accountId) {

        Member member = memberService.getMember(accountId);

        return FindMemberVo.builder()
                .accountId(member.getAccountId())
                .maxSlot(member.getSlotCount())
                .starPoint(member.getStarPoint())
                .build();
    }


    @Transactional(readOnly = true)
    public List<FindChargeItemVo> findChargeItem() {

        List<ChargeItem> chargeItemList = chargeItemService.getChargeItem();

        return FindChargeItemVo.toList(chargeItemList);
    }

    @Transactional(readOnly = true)
    public List<FindExchangeItemVo> findExchangeItem() {

        List<ExchangeItem> exchangeItemList = exchangeItemService.getExchangeItem();

        return FindExchangeItemVo.toList(exchangeItemList);
    }

    @Transactional
    public BuySlotVo buySlot(Long accountId, String deviceId) {

        PaymentLog paymentLog = paymentService.addBuySlotLog(accountId, deviceId);

        Member member = memberService.increaseSlotCount(accountId, 1);

        paymentService.itemReward(paymentLog.getId());

        return BuySlotVo.builder()
                .accountId(member.getAccountId())
                .maxSlot(member.getSlotCount())
                .starPoint(member.getStarPoint())
                .build();
    }

    @RealTimeMember(codes = { PublishCode.MEMBER_STAR_POINT })
    @Transactional
    public ChargeStarPointVo chargeStarPoint(Long accountId, String deviceId, String receipt, String chargeItemId) {

        PaymentLog paymentLog = paymentService.addChargeStarPointLog(accountId, deviceId, receipt);

        ChargeItem chargeItem = chargeItemService.getChargeItem(chargeItemId);

        Member member = memberService.increaseStarPoint(accountId, chargeItem.getStarPoint());

        paymentService.itemReward(paymentLog.getId());

        return ChargeStarPointVo.builder()
                .accountId(member.getAccountId())
                .starPoint(member.getStarPoint())
                .build();
    }

    @RealTimeMember(codes = { PublishCode.MEMBER_STAR_POINT })
    @Transactional
    public ExchangePayPointVo exchangePayPoint(Long accountId, String deviceId, Long mongId, String exchangeItemId) {

        PaymentLog paymentLog = paymentService.addExchangePayPointLog(accountId, deviceId);

        ExchangeItem exchangeItem = exchangeItemService.getExchangeItem(exchangeItemId);

        Member member = memberService.decreaseStarPoint(accountId, exchangeItem.getStarPoint());

        managementInternalFeignService.increasePayPoint(mongId, exchangeItem.getPayPoint());

        paymentService.itemReward(paymentLog.getId());

        return ExchangePayPointVo.builder()
                .accountId(member.getAccountId())
                .mongId(mongId)
                .starPoint(member.getStarPoint())
                .build();
    }

    @Transactional
    public ExchangePayPointWalkingVo exchangePayPointWalking(Long accountId, String deviceId, Long mongId, Integer walkingCount) {

        PaymentLog paymentLog = paymentService.addExchangePayPointWalkingLog(accountId, deviceId);

        int subWalkingCount = walkingCount / 100;
        int addPayPoint = 10 * subWalkingCount;

        IncreasePayPointResDto increasePayPointResDto = managementInternalFeignService.increasePayPoint(mongId, addPayPoint);

        paymentService.itemReward(paymentLog.getId());

        return ExchangePayPointWalkingVo.builder()
                .accountId(accountId)
                .mongId(mongId)
                .subWalkingCount(subWalkingCount)
                .payPoint(increasePayPointResDto.payPoint())
                .build();
    }
}
