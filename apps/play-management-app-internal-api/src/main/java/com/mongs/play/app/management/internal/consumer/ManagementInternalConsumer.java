package com.mongs.play.app.management.internal.consumer;

import com.mongs.play.module.kafka.dto.KafkaEventDto;
import com.mongs.play.module.kafka.dto.req.TestReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagementInternalConsumer {

    @KafkaListener(topics = { "test" }, groupId = "dev_mongs")
    public void increaseWeight(@Payload KafkaEventDto<TestReqDto> kafkaEventDto) {

        // TestReqDto testReqDto = kafkaEventDto.data();

        // log.info("message: {}, count: {}", testReqDto.message(), testReqDto.count());
//        log.info("data: {}", kafkaEventDto.data().getMessage());
        log.info(kafkaEventDto.data().message());
    }

}
