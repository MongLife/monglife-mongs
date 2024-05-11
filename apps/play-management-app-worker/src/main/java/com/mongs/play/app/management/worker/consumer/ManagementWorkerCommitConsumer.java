package com.mongs.play.app.management.worker.consumer;

import com.mongs.play.app.management.worker.service.ManagementWorkerService;
import com.mongs.play.module.kafka.event.managementExternal.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagementWorkerCommitConsumer {

    private final ManagementWorkerService managementWorkerService;

    @KafkaListener(topics = { "commit.registerMong" })
    public void zeroEvolutionMongSchedule(RegisterMongEvent payload) {
        Long mongId = payload.getMongId();
        managementWorkerService.evolutionReadyMong(mongId);
        log.info("[evolution ready mong schedule] mongId: {}", mongId);
    }

    @KafkaListener(topics = { "commit.evolutionMong.first" })
    public void firstEvolutionMongSchedule(EvolutionMongEvent payload) {
        Long mongId = payload.getMongId();
        String pastMongCode = payload.getPastMongCode();
        log.info("[first evolution mong schedule] mongId: {}, pastMongCode: {}", mongId, pastMongCode);
    }

    @KafkaListener(topics = { "commit.evolutionMong" })
    public void evolutionMongSchedule(EvolutionMongEvent payload) {
        Long mongId = payload.getMongId();
        String pastMongCode = payload.getPastMongCode();
        log.info("[evolution mong schedule] mongId: {}, pastMongCode: {}", mongId, pastMongCode);
    }

    @KafkaListener(topics = { "commit.evolutionMong.last" })
    public void lastEvolutionMongSchedule(EvolutionMongEvent payload) {
        Long mongId = payload.getMongId();
        String pastMongCode = payload.getPastMongCode();
        log.info("[last evolution mong schedule] mongId: {}, pastMongCode: {}", mongId, pastMongCode);
    }

    @KafkaListener(topics = { "commit.sleepingMong.sleep" })
    public void SleepSleepingMongSchedule(SleepingMongEvent payload) {
        Long mongId = payload.getMongId();
        log.info("[sleep sleeping mong schedule] mongId: {}", mongId);
    }

    @KafkaListener(topics = { "commit.sleepingMong.awake" })
    public void awakeSleepingMongSchedule(SleepingMongEvent payload) {
        Long mongId = payload.getMongId();
        log.info("[awake sleeping mong schedule] mongId: {}", mongId);
    }

    @KafkaListener(topics = { "commit.deleteMong" })
    public void deleteMongSchedule(DeleteMongEvent payload) {
        Long mongId = payload.getMongId();
        log.info("[delete mong schedule] mongId: {}", mongId);
    }
}
