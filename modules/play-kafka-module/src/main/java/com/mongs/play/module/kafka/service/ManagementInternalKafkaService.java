package com.mongs.play.module.kafka.service;

import com.mongs.play.module.kafka.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementInternalKafkaService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void evolutionReady(Long mongId) {
        kafkaTemplate.send(Topic.EVOLUTION_READY_SCHEDULE, EvolutionReadyEvent.builder()
                        .topic(Topic.EVOLUTION_READY_SCHEDULE)
                        .mongId(mongId)
                .build());
    }

    public void decreaseStatus(Long mongId, Double subWeight, Double subStrength, Double subSatiety, Double subHealthy, Double subSleep) {
        String topic = Topic.DECREASE_STATUS_SCHEDULE;

        kafkaTemplate.send(topic, DecreaseStatusEvent.builder()
                .topic(topic)
                .mongId(mongId)
                .subWeight(subWeight)
                .subStrength(subStrength)
                .subSatiety(subSatiety)
                .subHealthy(subHealthy)
                .subSleep(subSleep)
                .build());
    }

    public void increaseStatus(Long mongId, Double addWeight, Double addStrength, Double addSatiety, Double addHealthy, Double addSleep) {
        String topic = Topic.INCREASE_STATUS_SCHEDULE;

        kafkaTemplate.send(topic, IncreaseStatusEvent.builder()
                .topic(topic)
                .mongId(mongId)
                .addWeight(addWeight)
                .addStrength(addStrength)
                .addSatiety(addSatiety)
                .addHealthy(addHealthy)
                .addSleep(addSleep)
                .build());
    }

    public void increasePoopCount(Long mongId, Integer addPoopCount) {
        String topic = Topic.INCREASE_POOP_COUNT_SCHEDULE;

        kafkaTemplate.send(topic, IncreasePoopCountEvent.builder()
                .topic(topic)
                .mongId(mongId)
                .addPoopCount(addPoopCount)
                .build());
    }

    public void dead(Long mongId) {
        String topic = Topic.DEAD_SCHEDULE;

        kafkaTemplate.send(topic, DeadEvent.builder()
                .topic(topic)
                .mongId(mongId)
                .build());
    }
}
