package com.mongs.play.app.management.worker.service;

import com.mongs.play.module.kafka.dto.KafkaEventDto;
import com.mongs.play.module.kafka.dto.req.TestReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementWorkerService {

    private final KafkaTemplate<String, KafkaEventDto<?>> kafkaTemplate;

    public void test() {
        KafkaEventDto<TestReqDto> kafkaEventDto = KafkaEventDto.<TestReqDto>builder()
                .id("testId")
                .data(TestReqDto.builder()
                        .message("testMessage")
                        .count(1)
                        .createdAt(LocalDateTime.now())
                        .build())
                .build();

        String topic = "management-internal.test";

        kafkaTemplate.send(topic, kafkaEventDto);

        log.info("Send Test Event: {}", kafkaEventDto);
    }

}
