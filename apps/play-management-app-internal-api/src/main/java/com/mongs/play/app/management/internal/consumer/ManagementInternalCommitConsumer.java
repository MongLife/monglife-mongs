package com.mongs.play.app.management.internal.consumer;

import com.mongs.play.module.kafka.event.commit.DecreaseWeightCommitEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagementInternalCommitConsumer {

    @KafkaListener(topics = { "management-internal.commit.decreaseWeight" })
    public void decreaseWeight(DecreaseWeightCommitEvent payload) {
        log.info("payload: {}, createdAt: {}", payload.getId(), payload.getCreatedAt());
    }
}
