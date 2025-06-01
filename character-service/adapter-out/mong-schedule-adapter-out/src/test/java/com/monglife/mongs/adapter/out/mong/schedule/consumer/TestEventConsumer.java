package com.monglife.mongs.adapter.out.mong.schedule.consumer;

import com.monglife.module.common.kafka.event.TransactionEvent;
import com.monglife.mongs.adapter.transaction.TestEventDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Kafka Consume Component
 */
@Component
public class TestEventConsumer {

    private TestEventDto testEventDto;

    private CountDownLatch countDownLatch;

    public TestEventConsumer() {}

    @KafkaListener(topics = "commit.test")
    public void testEvent(TransactionEvent<Map<String, Integer>> event) {

        if (event.getData() != null) {
            testEventDto.setTaskId((long) event.getData().getOrDefault("taskId", -1));
            countDownLatch.countDown();
        }
    }

    public void reset(TestEventDto testEventDto, CountDownLatch countDownLatch) {
        this.testEventDto = testEventDto;
        this.countDownLatch = countDownLatch;
    }
}
