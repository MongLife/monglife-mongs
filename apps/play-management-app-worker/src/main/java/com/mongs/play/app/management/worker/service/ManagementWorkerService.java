package com.mongs.play.app.management.worker.service;

import com.mongs.play.module.kafka.service.KafkaService.KafkaTopic;
import com.mongs.play.module.kafka.event.commit.DecreaseWeightCommitPayload;
import com.mongs.play.module.kafka.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementWorkerService {

    private final KafkaService kafkaService;

    public void decreaseWeight(Long mongId, Double subWeight) {
        DecreaseWeightCommitPayload decreaseWeightCommitPayload = DecreaseWeightCommitPayload.builder()
                .mongId(mongId)
                .subWeight(subWeight)
                .build();

        decreaseWeightCommitPayload = (DecreaseWeightCommitPayload) kafkaService.send(KafkaTopic.MANAGEMENT_INTERNAL, "decreaseWeight", decreaseWeightCommitPayload);

        log.info("Send Event: {}", decreaseWeightCommitPayload);
    }
}
