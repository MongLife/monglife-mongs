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

    public <T extends BasicEvent> void sendCommit(String topic, T event) {
        event.setTopic(topic);
        kafkaTemplate.send("commit." + topic, event);
    }

    public <T extends BasicEvent> void sendStop(String topic, T event) {
        event.setTopic(topic);
        kafkaTemplate.send("stop." + topic, event);
    }

    public <T extends BasicEvent> void sendRollback(String topic, T event) {
        kafkaTemplate.send("rollback." + topic, event);
    }

    @Getter
    @AllArgsConstructor
    public static class CommitTopic {
        // management_external -> management_worker
        public static final String REGISTER_MONG = "commit.registerMong";
        public static final String FIRST_EVOLUTION_MONG = "commit.evolutionMong.first";
        public static final String EVOLUTION_MONG = "commit.evolutionMong";
        public static final String LAST_EVOLUTION_MONG = "commit.evolutionMong.last";
        public static final String TRAINING_MONG = "commit.trainingMong";
        public static final String FEED_MONG = "commit.feedMong";
        public static final String SLEEP_SLEEPING_MONG = "commit.sleepingMong.sleep";
        public static final String AWAKE_SLEEPING_MONG = "commit.sleepingMong.awake";
        public static final String DELETE_MONG = "commit.deleteMong";

        // management_internal -> management_worker
        public static final String EVOLUTION_READY = "commit.evolutionReady";
        public static final String DECREASE_STATUS = "commit.decreaseStatus";
        public static final String INCREASE_STATUS = "commit.increaseStatus";
        public static final String INCREASE_POOP_COUNT = "commit.increasePoopCount";

        // management_worker -> management_internal
        public static final String ZERO_EVOLUTION_SCHEDULE = "commit.zeroEvolutionSchedule";
        public static final String DECREASE_STATUS_SCHEDULE = "commit.decreaseStatusSchedule";
        public static final String INCREASE_STATUS_SCHEDULE = "commit.increaseStatusSchedule";
        public static final String INCREASE_POOP_COUNT_SCHEDULE = "commit.increasePoopCountSchedule";
        public static final String DEAD_SCHEDULE = "commit.deadSchedule";
        // player_internal -> ?
        public static final String REGISTER_MAP_COLLECTION = "commit.registerMapCollection";
        public static final String REGISTER_MONG_COLLECTION = "commit.registerMongCollection";
        public static final String EXCHANGE_PAY_POINT = "commit.exchangePayPoint";
    }

    @Getter
    @AllArgsConstructor
    public static class RollbackTopic {
        // management_external -> management_worker
        public static final String REGISTER_MONG = "rollback.registerMong";
        public static final String FIRST_EVOLUTION_MONG = "rollback.evolutionMong.first";
        public static final String EVOLUTION_MONG = "rollback.evolutionMong";
        public static final String LAST_EVOLUTION_MONG = "rollback.evolutionMong.last";
        public static final String TRAINING_MONG = "rollback.trainingMong";
        public static final String FEED_MONG = "rollback.feedMong";
        public static final String SLEEP_SLEEPING_MONG = "rollback.sleepingMong.sleep";
        public static final String AWAKE_SLEEPING_MONG = "rollback.sleepingMong.awake";
        public static final String DELETE_MONG = "rollback.deleteMong";

        // management_internal -> management_worker
        public static final String EVOLUTION_READY = "rollback.evolutionReady";
        public static final String DECREASE_STATUS = "rollback.decreaseStatus";
        public static final String INCREASE_STATUS = "rollback.increaseStatus";
        public static final String INCREASE_POOP_COUNT = "rollback.increasePoopCount";

        // management_worker -> management_internal
        public static final String ZERO_EVOLUTION_SCHEDULE = "rollback.zeroEvolutionSchedule";
        public static final String DECREASE_STATUS_SCHEDULE = "rollback.decreaseStatusSchedule";
        public static final String INCREASE_STATUS_SCHEDULE = "rollback.increaseStatusSchedule";
        public static final String INCREASE_POOP_COUNT_SCHEDULE = "rollback.increasePoopCountSchedule";
        public static final String DEAD_SCHEDULE = "rollback.deadSchedule";
        // player_internal -> ?
        public static final String REGISTER_MAP_COLLECTION = "rollback.registerMapCollection";
        public static final String REGISTER_MONG_COLLECTION = "rollback.registerMongCollection";
        public static final String EXCHANGE_PAY_POINT = "rollback.exchangePayPoint";
    }

    @Getter
    @AllArgsConstructor
    public static class StopTopic {
        // management_external -> management_worker
        public static final String REGISTER_MONG = "stop.registerMong";
        public static final String FIRST_EVOLUTION_MONG = "stop.evolutionMong.first";
        public static final String EVOLUTION_MONG = "stop.evolutionMong";
        public static final String LAST_EVOLUTION_MONG = "stop.evolutionMong.last";
        public static final String TRAINING_MONG = "stop.trainingMong";
        public static final String FEED_MONG = "stop.feedMong";
        public static final String SLEEP_SLEEPING_MONG = "stop.sleepingMong.sleep";
        public static final String AWAKE_SLEEPING_MONG = "stop.sleepingMong.awake";
        public static final String DELETE_MONG = "stop.deleteMong";

        // management_internal -> management_worker
        public static final String EVOLUTION_READY = "stop.evolutionReady";
        public static final String DECREASE_STATUS = "stop.decreaseStatus";
        public static final String INCREASE_STATUS = "stop.increaseStatus";
        public static final String INCREASE_POOP_COUNT = "stop.increasePoopCount";

        // management_worker -> management_internal
        public static final String ZERO_EVOLUTION_SCHEDULE = "stop.zeroEvolutionSchedule";
        public static final String DECREASE_STATUS_SCHEDULE = "stop.decreaseStatusSchedule";
        public static final String INCREASE_STATUS_SCHEDULE = "stop.increaseStatusSchedule";
        public static final String INCREASE_POOP_COUNT_SCHEDULE = "stop.increasePoopCountSchedule";
        public static final String DEAD_SCHEDULE = "stop.deadSchedule";
        // player_internal -> ?
        public static final String REGISTER_MAP_COLLECTION = "stop.registerMapCollection";
        public static final String REGISTER_MONG_COLLECTION = "stop.registerMongCollection";
        public static final String EXCHANGE_PAY_POINT = "stop.exchangePayPoint";
    }
}
