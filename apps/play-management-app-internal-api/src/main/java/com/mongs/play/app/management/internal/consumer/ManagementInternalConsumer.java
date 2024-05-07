package com.mongs.play.app.management.internal.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.play.module.kafka.dto.KafkaEventDto;
import com.mongs.play.module.kafka.dto.req.TestReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagementInternalConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = { "management-internal.test" })
    public void increaseWeight(KafkaEventDto<TestReqDto> kafkaEventDto) {

        TestReqDto testReqDto = objectMapper.convertValue(kafkaEventDto.getData(), TestReqDto.class);

        log.info("kafkaEventDto: {}", kafkaEventDto);
//        log.info("data: {}", kafkaEventDto.data().message());
//        log.info("data: {}", testReqDto.message());
//        log.info("message: {}", kafkaEventDto.data().message());
        // TestReqDto testReqDto = kafkaEventDto.data();

        // log.info("message: {}, count: {}", testReqDto.message(), testReqDto.count());
//        log.info("data: {}", kafkaEventDto.data().getMessage());
//        log.info(kafkaEventDto.data().message());
    }

}
