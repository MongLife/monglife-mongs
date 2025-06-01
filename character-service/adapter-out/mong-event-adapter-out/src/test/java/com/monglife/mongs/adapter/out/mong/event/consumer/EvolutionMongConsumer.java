package com.monglife.mongs.adapter.out.mong.event.consumer;

import com.monglife.module.common.kafka.event.TransactionEvent;
import com.monglife.mongs.adapter.transaction.EvolutionMongEventDto;
import com.monglife.mongs.core.kafka.event.enums.EventTopic;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class EvolutionMongConsumer {

    private EvolutionMongEventDto evolutionMongEventDto;

    private CountDownLatch countDownLatch;

    @KafkaListener(topics = EventTopic.COMMIT_EVOLUTION_MONG)
    public void evolutionMongEvent(@Payload TransactionEvent<EvolutionMongEventDto> event) {

        if (event.getData() != null) {
            BeanUtils.copyProperties(event.getData(), evolutionMongEventDto);
            countDownLatch.countDown();
        }
    }

    public void reset(EvolutionMongEventDto evolutionMongEventDto, CountDownLatch countDownLatch) {
        this.evolutionMongEventDto = evolutionMongEventDto;
        this.countDownLatch = countDownLatch;
    }
}
