package com.mongs.play.app.management.internal.consumer;

import com.mongs.play.module.kafka.event.commit.DecreaseWeightCommitEvent;
import com.mongs.play.module.kafka.event.rollback.DecreaseWeightRollbackEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagementInternalRollbackConsumer {

    @KafkaListener(topics = { "management-internal.rollback.decreaseWeight" })
    public void decreaseWeight(DecreaseWeightRollbackEvent payload) {
        log.info("payload: {}, createdAt: {}", payload.getId(), payload.getCreatedAt());
    }
}
