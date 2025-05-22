package com.monglife.mongs.adapter.out.member.event.consumer;

import com.monglife.module.common.kafka.event.TransactionEvent;
import com.monglife.mongs.adapter.transaction.ExchangeStarPointEventDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * Kafka Consume Component
 */
@Component
public class ExchangeStarPointConsumer {

    private ExchangeStarPointEventDto exchangeStarPointEventDto;

    private CountDownLatch countDownLatch;

    public ExchangeStarPointConsumer() {}

    @KafkaListener(topics = "commit.exchangeStarPoint")
    public void exchangeStarPointEvent(TransactionEvent<ExchangeStarPointEventDto> event) {

        if (event.getData() != null) {
            exchangeStarPointEventDto.setMongId(event.getData().getMongId());
            exchangeStarPointEventDto.setStarPoint(event.getData().getStarPoint());
            exchangeStarPointEventDto.setPayPoint(event.getData().getPayPoint());
            countDownLatch.countDown();
        }
    }

    public void reset(ExchangeStarPointEventDto exchangeStarPointEventDto, CountDownLatch countDownLatch) {
        this.exchangeStarPointEventDto = exchangeStarPointEventDto;
        this.countDownLatch = countDownLatch;
    }
}
