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

    @KafkaListener(topics = { "management-internal.decreaseWeight" })
    public void increaseWeight(KafkaEventDto<TestReqDto> payload) {

        TestReqDto testReqDto = objectMapper.convertValue(payload.data(), TestReqDto.class);


    }

}
