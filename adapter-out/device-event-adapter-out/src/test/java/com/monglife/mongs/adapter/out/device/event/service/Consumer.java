package com.monglife.mongs.adapter.out.device.event.service;

import com.monglife.module.common.kafka.event.TransactionEvent;
import com.monglife.mongs.adapter.out.device.event.dto.ExchangeCurrentWalkingCountEventDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * Kafka Consume Component
 */
@Component
public class Consumer {

    private ExchangeCurrentWalkingCountEventDto exchangeCurrentWalkingCountEventDto;

    private CountDownLatch countDownLatch;

    public Consumer() {}

    @KafkaListener(topics = "commit.exchangeCurrentWalkingCount")
    public void exchangeCurrentWalkingCountEvent(TransactionEvent<ExchangeCurrentWalkingCountEventDto> event) {

        if (event.getData() != null) {
            exchangeCurrentWalkingCountEventDto.setMongId(event.getData().getMongId());
            exchangeCurrentWalkingCountEventDto.setWalkingCount(event.getData().getWalkingCount());
            exchangeCurrentWalkingCountEventDto.setPayPoint(event.getData().getPayPoint());
            countDownLatch.countDown();
        }
    }

    public void reset(ExchangeCurrentWalkingCountEventDto exchangeCurrentWalkingCountEventDto, CountDownLatch countDownLatch) {
        this.exchangeCurrentWalkingCountEventDto = exchangeCurrentWalkingCountEventDto;
        this.countDownLatch = countDownLatch;
    }
}
