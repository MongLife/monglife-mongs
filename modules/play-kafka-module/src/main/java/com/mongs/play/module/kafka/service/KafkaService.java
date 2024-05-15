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
        event.setTopic(kafkaTopic.topic);
        kafkaTemplate.send("commit." + kafkaTopic.topic, event);
    }

    public <T extends BasicEvent> void sendStop(KafkaTopic kafkaTopic, T event) {
        event.setTopic(kafkaTopic.topic);
        kafkaTemplate.send("stop." + kafkaTopic.topic, event);
    }

    public <T extends BasicEvent> void sendRollback(KafkaTopic kafkaTopic, T event) {
        kafkaTemplate.send("rollback." + kafkaTopic.topic, event);
    }

    @Getter
    @AllArgsConstructor
    public enum KafkaTopic {
        // management_external -> management_worker
        REGISTER_MONG("registerMong"),
        FIRST_EVOLUTION_MONG("evolutionMong.first"),
        EVOLUTION_MONG("evolutionMong"),
        LAST_EVOLUTION_MONG("evolutionMong.last"),
        TRAINING_MONG("trainingMong"),
        FEED_MONG("feedMong"),
        SLEEP_SLEEPING_MONG("sleepingMong.sleep"),
        AWAKE_SLEEPING_MONG("sleepingMong.awake"),
        DELETE_MONG("deleteMong"),
        // management_internal -> management_worker
        EVOLUTION_READY("evolutionReady"),
        DECREASE_STATUS("decreaseStatus"),
        INCREASE_STATUS("increaseStatus"),
        INCREASE_POOP_COUNT("increasePoopCount"),
        // management_worker -> management_internal
        ZERO_EVOLUTION_SCHEDULE("zeroEvolutionSchedule"),
        DECREASE_STATUS_SCHEDULE("decreaseStatusSchedule"),
        INCREASE_STATUS_SCHEDULE("increaseStatusSchedule"),
        INCREASE_POOP_COUNT_SCHEDULE("increasePoopCountSchedule"),
        DEAD_SCHEDULE("deadSchedule"),
        // player_internal -> ?
        REGISTER_MAP_COLLECTION("registerMapCollection"),
        REGISTER_MONG_COLLECTION("registerMongCollection"),
        EXCHANGE_PAY_POINT("exchangePayPoint"),
        ;

        public final String topic;
    }
}
