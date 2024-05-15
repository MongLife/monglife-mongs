package com.mongs.play.app.management.internal.consumer;

import com.mongs.play.module.kafka.event.managementInternal.*;
import com.mongs.play.module.kafka.event.managementWorker.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagementInternalRollbackConsumer {

    @KafkaListener(topics = { "rollback.evolutionReady" })
    public void zeroEvolutionMong(ZeroEvolutionScheduleEvent payload) {}

    @KafkaListener(topics = { "rollback.decreaseStatus" })
    public void decreaseStatus(DecreaseStatusEvent payload) {}

    @KafkaListener(topics = { "rollback.increaseStatus" })
    public void decreaseSatiety(IncreaseStatusEvent payload) {}

    @KafkaListener(topics = { "rollback.increasePoopCount" })
    public void increasePoopCount(IncreasePoopCountEvent payload) {}
}
