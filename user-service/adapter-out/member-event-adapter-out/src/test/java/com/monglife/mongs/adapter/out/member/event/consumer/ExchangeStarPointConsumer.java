package com.monglife.mongs.adapter.out.member.event.consumer;

import com.monglife.module.common.kafka.event.TransactionEvent;
import com.monglife.mongs.adapter.transaction.ExchangeStarPointEventDto;
import com.monglife.mongs.core.kafka.event.enums.EventTopic;
import org.springframework.beans.BeanUtils;
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

    @KafkaListener(topics = "${spring.config.activate.on-profile}." + EventTopic.COMMIT_EXCHANGE_STAR_POINT)
    public void exchangeStarPointEvent(TransactionEvent<ExchangeStarPointEventDto> event) {

        if (event.getData() != null) {
            BeanUtils.copyProperties(event.getData(), exchangeStarPointEventDto);
            countDownLatch.countDown();
        }
    }

    public void reset(ExchangeStarPointEventDto exchangeStarPointEventDto, CountDownLatch countDownLatch) {
        this.exchangeStarPointEventDto = exchangeStarPointEventDto;
        this.countDownLatch = countDownLatch;
    }
}
