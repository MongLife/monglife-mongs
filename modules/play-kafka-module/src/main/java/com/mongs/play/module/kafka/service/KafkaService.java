package com.mongs.play.module.kafka.service;

import com.mongs.play.module.kafka.event.BasicEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public <T extends BasicEvent> void sendCommit(KafkaTopic kafkaTopic, T event) {
        String sendTopic = String.format("commit.%s", kafkaTopic.topic);
        event.setTopic(sendTopic);
        kafkaTemplate.send(sendTopic, event);
    }

    public <T extends BasicEvent> void sendRollback(String sendTopic, T event) {
        event.setTopic(sendTopic);
        kafkaTemplate.send(sendTopic, event);
    }

    @Getter
    @AllArgsConstructor
    public enum KafkaTopic {

        EVOLUTION_MONG("evolutionMong"),
        REGISTER_MONG_COLLECTION("registerMongCollection"),
        INCREASE_STAR_POINT("increaseStartPoint"),
        ;

        public final String topic;
    }
}
