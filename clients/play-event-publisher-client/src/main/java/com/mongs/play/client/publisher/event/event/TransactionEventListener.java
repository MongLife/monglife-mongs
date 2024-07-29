package com.mongs.play.client.publisher.event.event;

import com.mongs.play.client.publisher.event.service.MqttEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionEventListener {

    private final MqttEventService mqttEventService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void mongTransactionEventListener(RealTimeMongEvent event) {
        Long mongId = event.mongId();
        List<Object> publishVoList = event.publishVoList();
        mqttEventService.sendMong(mongId, publishVoList);
        log.info("mongTransactionEventListener Call");
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void memberTransactionEventListener(RealTimeMemberEvent event) {
        Long accountId = event.accountId();
        List<Object> publishVoList = event.publishVoList();
        mqttEventService.sendMember(accountId, publishVoList);
        log.info("memberTransactionEventListener Call");
    }
}
