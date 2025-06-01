package com.monglife.mongs.adapter.out.mong.publish.consumer;

import com.monglife.core.dto.event.SendNotificationDto;
import com.monglife.module.common.kafka.event.TransactionEvent;
import com.monglife.mongs.core.kafka.event.enums.EventTopic;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * Kafka Consume Component
 */
@Component
public class NotificationConsumer {

    private SendNotificationDto sendNotificationDto;

    private CountDownLatch countDownLatch;

    public NotificationConsumer() {}

    @KafkaListener(topics = EventTopic.NOTIFICATION_MONGS)
    public void exchangeStarPointEvent(TransactionEvent<SendNotificationDto> event) {

        if (event.getData() != null) {
            BeanUtils.copyProperties(event.getData(), this.sendNotificationDto);
            countDownLatch.countDown();
        }
    }

    public void reset(SendNotificationDto sendNotificationDto, CountDownLatch countDownLatch) {
        this.sendNotificationDto = sendNotificationDto;
        this.countDownLatch = countDownLatch;
    }
}
