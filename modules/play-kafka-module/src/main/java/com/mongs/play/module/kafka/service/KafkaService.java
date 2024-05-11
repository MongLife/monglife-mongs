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

    public <T extends BasicEvent> void sendRollback(String topic, T event) {
        kafkaTemplate.send("rollback." + topic, event);
    }

    @Getter
    @AllArgsConstructor
    public enum KafkaTopic {
        // management_external -> management_worker
        REGISTER_MONG("registerMong"),
        FIRST_EVOLUTION_MONG("evolutionMong.first"),
        EVOLUTION_MONG("evolutionMong"),
        LAST_EVOLUTION_MONG("evolutionMong.last"),
        SLEEP_SLEEPING_MONG("sleepingMong.sleep"),
        AWAKE_SLEEPING_MONG("sleepingMong.awake"),
        DELETE_MONG("deleteMong"),
        // management_worker -> management_internal
        EVOLUTION_READY_MONG_SCHEDULE("evolutionReadyMongSchedule"),
        DECREASE_WEIGHT_SCHEDULE("decreaseWeightSchedule"),
        DECREASE_STRENGTH_SCHEDULE("decreaseStrengthSchedule"),
        DECREASE_SATIETY_SCHEDULE("decreaseSatietySchedule"),
        DECREASE_HEALTHY_SCHEDULE("decreaseHealthySchedule"),
        DECREASE_SLEEP_SCHEDULE("decreaseSleepSchedule"),
        INCREASE_POOP_COUNT_SCHEDULE("increasePoolCountSchedule"),
        INCREASE_SLEEP_SCHEDULE("increaseSleepSchedule"),
        DEAD_MONG_SCHEDULE("deadMongSchedule"),
        // management_internal -> ?
        EVOLUTION_READY_MONG("evolutionReadyMong"),
        DECREASE_WEIGHT("decreaseWeight"),
        DECREASE_STRENGTH("decreaseStrength"),
        DECREASE_SATIETY("decreaseSatiety"),
        DECREASE_HEALTHY("decreaseHealthy"),
        DECREASE_SLEEP("decreaseSleep"),
        DECREASE_POOP_COUNT("decreasePoolCount"),
        INCREASE_WEIGHT("increaseWeight"),
        INCREASE_STRENGTH("increaseStrength"),
        INCREASE_SATIETY("increaseSatiety"),
        INCREASE_HEALTHY("increaseHealthy"),
        INCREASE_SLEEP("increaseSleep"),
        INCREASE_POOP_COUNT("increasePoolCount"),
        DEAD_MONG("deadMong"),
        // player_internal -> ?
        REGISTER_MONG_COLLECTION("registerMongCollection"),
        INCREASE_STAR_POINT("increaseStartPoint"),
        ;

        public final String topic;
    }
}
