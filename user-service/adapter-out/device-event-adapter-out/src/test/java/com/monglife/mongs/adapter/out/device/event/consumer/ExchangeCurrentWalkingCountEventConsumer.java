package com.monglife.mongs.adapter.out.device.event.consumer;

import com.monglife.module.common.kafka.event.TransactionEvent;
import com.monglife.mongs.adapter.transaction.ExchangeCurrentWalkingCountEventDto;
import com.monglife.mongs.core.kafka.event.enums.EventTopic;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * Kafka Consume Component
 */
@Component
public class ExchangeCurrentWalkingCountEventConsumer {

    private ExchangeCurrentWalkingCountEventDto exchangeCurrentWalkingCountEventDto;

    private CountDownLatch countDownLatch;

    public ExchangeCurrentWalkingCountEventConsumer() {}

    @KafkaListener(topics = "${spring.config.activate.on-profile}." + EventTopic.COMMIT_EXCHANGE_CURRENT_WALKING_COUNT)
    public void exchangeCurrentWalkingCountEvent(TransactionEvent<ExchangeCurrentWalkingCountEventDto> event) {

        if (event.getData() != null) {
            BeanUtils.copyProperties(event.getData(), exchangeCurrentWalkingCountEventDto);
            countDownLatch.countDown();
        }
    }

    public void reset(ExchangeCurrentWalkingCountEventDto exchangeCurrentWalkingCountEventDto, CountDownLatch countDownLatch) {
        this.exchangeCurrentWalkingCountEventDto = exchangeCurrentWalkingCountEventDto;
        this.countDownLatch = countDownLatch;
    }
}
