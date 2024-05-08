package com.mongs.play.module.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.play.core.error.module.KafkaErrorCode;
import com.mongs.play.core.exception.common.GenerateException;
import com.mongs.play.core.exception.common.InvalidException;
import com.mongs.play.module.kafka.event.BasicEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

    public enum KafkaTopic {
        MANAGEMENT_EXTERNAL,
        MANAGEMENT_INTERNAL,
        MANAGEMENT_WORKER,
        PLAYER_EXTERNAL,
        PLAYER_INTERNAL,
    }

    @Value("${application.kafka.base-topic.management-external}")
    private String managementExternalTopic;
    @Value("${application.kafka.base-topic.management-internal}")
    private String managementInternalTopic;
    @Value("${application.kafka.base-topic.management-worker}")
    private String managementWorkerTopic;
    @Value("${application.kafka.base-topic.player-external}")
    private String playerExternalTopic;
    @Value("${application.kafka.base-topic.player-internal}")
    private String playerInternalTopic;

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public <T extends BasicEvent> BasicEvent send(KafkaTopic kafkaTopic, String subTopic, T event) {

        String topic = "";

        switch (kafkaTopic) {
            case MANAGEMENT_EXTERNAL -> {
                topic = managementExternalTopic;
            }
            case MANAGEMENT_INTERNAL -> {
                topic = managementInternalTopic;
            }
            case MANAGEMENT_WORKER -> {
                topic = managementWorkerTopic;
            }
            case PLAYER_EXTERNAL -> {
                topic = playerExternalTopic;
            }
            case PLAYER_INTERNAL -> {
                topic = playerInternalTopic;
            }
            default -> {
                throw new InvalidException(KafkaErrorCode.NOT_FOUND_TOPIC);
            }
        }

        String sendTopic = String.format("%s.commit.%s", topic, subTopic);

        try {
            event.getHistory().put(sendTopic, objectMapper.writeValueAsBytes(event));
            kafkaTemplate.send(sendTopic, event);
            return event;
        } catch (JsonProcessingException e) {
            throw new GenerateException(KafkaErrorCode.GENERATE_HISTORY);
        }
    }
}
