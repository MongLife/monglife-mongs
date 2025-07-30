package com.monglife.mongs.adapter.out.mong.event.consumer;

import com.monglife.module.common.kafka.event.TransactionEvent;
import com.monglife.mongs.adapter.transaction.RandomDrawMapEventDto;
import com.monglife.mongs.core.kafka.event.enums.EventTopic;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class RandomDrawMapConsumer {

    private RandomDrawMapEventDto randomDrawMapEventDto;

    private CountDownLatch countDownLatch;

    @KafkaListener(topics = "${spring.config.activate.on-profile}." + EventTopic.COMMIT_RANDOM_DRAW_MAP)
    public void randomDrawMapEvent(@Payload TransactionEvent<RandomDrawMapEventDto> event) {

        if (event.getData() != null) {
            BeanUtils.copyProperties(event.getData(), randomDrawMapEventDto);
            countDownLatch.countDown();
        }
    }

    public void reset(RandomDrawMapEventDto randomDrawMapEventDto, CountDownLatch countDownLatch) {
        this.randomDrawMapEventDto = randomDrawMapEventDto;
        this.countDownLatch = countDownLatch;
    }
}
