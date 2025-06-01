package com.monglife.mongs.adapter.out.mong.event.consumer;

import com.monglife.module.common.kafka.event.TransactionEvent;
import com.monglife.mongs.adapter.transaction.CreateMongEventDto;
import com.monglife.mongs.core.kafka.event.enums.EventTopic;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class CreateMongConsumer {

    private CreateMongEventDto createMongEventDto;

    private CountDownLatch countDownLatch;

    @KafkaListener(topics = EventTopic.COMMIT_CREATE_MONG)
    public void createMongEvent(@Payload TransactionEvent<CreateMongEventDto> event) {

        if (event.getData() != null) {
            BeanUtils.copyProperties(event.getData(), createMongEventDto);
            countDownLatch.countDown();
        }
    }

    public void reset(CreateMongEventDto createMongEventDto, CountDownLatch countDownLatch) {
        this.createMongEventDto = createMongEventDto;
        this.countDownLatch = countDownLatch;
    }
}
