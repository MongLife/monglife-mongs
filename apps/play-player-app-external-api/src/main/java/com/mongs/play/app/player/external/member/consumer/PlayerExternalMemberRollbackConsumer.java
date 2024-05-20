package com.mongs.play.app.player.external.member.consumer;

import com.mongs.play.app.player.external.member.service.PlayerExternalMemberService;
import com.mongs.play.module.kafka.event.playerExternal.ExchangePayPointEvent;
import com.mongs.play.module.kafka.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlayerExternalMemberRollbackConsumer {

    private final PlayerExternalMemberService playerExternalMemberService;

    @KafkaListener(topics = { KafkaService.RollbackTopic.EXCHANGE_PAY_POINT })
    public void exchangePayPoint(ExchangePayPointEvent payload) {

        Long paymentLogId = payload.getPaymentLogId();
        Integer subStarPoint = payload.getSubStarPoint();

        playerExternalMemberService.exchangePayPoint(paymentLogId, subStarPoint);
    }
}
