package com.mongs.play.app.management.worker.service;

import com.mongs.play.module.kafka.event.commit.DecreaseWeightCommitEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementWorkerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void test() {
        DecreaseWeightCommitEvent decreaseWeightCommitEventDto = DecreaseWeightCommitEvent.builder()
                .id("testId")
                .message("testMessage")
                .count(1)
                .createdAt(LocalDateTime.now())
                .build();

        String topic = "management-internal.decreaseWeight";

        kafkaTemplate.send(topic, decreaseWeightCommitEventDto);

        log.info("Send Test Event: {}", decreaseWeightCommitEventDto);
    }

}
