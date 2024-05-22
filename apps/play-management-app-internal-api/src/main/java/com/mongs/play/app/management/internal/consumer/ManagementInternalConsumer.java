package com.mongs.play.app.management.internal.consumer;

import com.mongs.play.app.management.internal.service.ManagementInternalService;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.module.feign.service.ManagementWorkerFeignService;
import com.mongs.play.module.kafka.event.*;
import com.mongs.play.module.kafka.service.Topic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagementInternalConsumer {

    private final ManagementInternalService managementInternalService;
    private final ManagementWorkerFeignService managementWorkerFeignService;

    @KafkaListener(topics = { Topic.EVOLUTION_READY_SCHEDULE })
    public void evolutionReady(@RequestBody EvolutionReadyEvent evolutionReadyEvent) {
        Long mongId = evolutionReadyEvent.getMongId();

        try {
            managementInternalService.evolutionReady(mongId);
        } catch (NotFoundException e) {
            managementWorkerFeignService.deleteSchedule(mongId);
        }
    }

    @KafkaListener(topics = { Topic.DECREASE_STATUS_SCHEDULE })
    public void decreaseStatus(@RequestBody DecreaseStatusEvent decreaseStatusEvent) {
        Long mongId = decreaseStatusEvent.getMongId();
        Double subWeight = decreaseStatusEvent.getSubWeight();
        Double subStrength = decreaseStatusEvent.getSubStrength();
        Double subSatiety = decreaseStatusEvent.getSubSatiety();
        Double subHealthy = decreaseStatusEvent.getSubHealthy();
        Double subSleep = decreaseStatusEvent.getSubSleep();

        try {
            managementInternalService.decreaseStatus(mongId, subWeight, subStrength, subSatiety, subHealthy, subSleep);
        } catch (NotFoundException e) {
            managementWorkerFeignService.deleteSchedule(mongId);
        }
    }

    @KafkaListener(topics = { Topic.INCREASE_STATUS_SCHEDULE })
    public void increaseStatus(@RequestBody IncreaseStatusEvent increaseStatusEvent) {
        Long mongId = increaseStatusEvent.getMongId();
        Double addWeight = increaseStatusEvent.getAddWeight();
        Double addStrength = increaseStatusEvent.getAddStrength();
        Double addSatiety = increaseStatusEvent.getAddSatiety();
        Double addHealthy = increaseStatusEvent.getAddHealthy();
        Double addSleep = increaseStatusEvent.getAddSleep();

        try {
            managementInternalService.increaseStatus(mongId, addWeight, addStrength, addSatiety, addHealthy, addSleep);
        } catch (NotFoundException e) {
            managementWorkerFeignService.deleteSchedule(mongId);
        }
    }

    @KafkaListener(topics = { Topic.INCREASE_POOP_COUNT_SCHEDULE })
    public void increasePoopCount(@RequestBody IncreasePoopCountEvent increasePoopCountEvent) {
        Long mongId = increasePoopCountEvent.getMongId();
        Integer addPoopCount = increasePoopCountEvent.getAddPoopCount();

        try {
            managementInternalService.increasePoopCount(mongId, addPoopCount);
        } catch (NotFoundException e) {
            managementWorkerFeignService.deleteSchedule(mongId);
        }
    }

    @KafkaListener(topics = { Topic.DEAD_SCHEDULE })
    public void dead(@RequestBody DeadEvent deadEvent) {
        Long mongId = deadEvent.getMongId();

        try {
            managementInternalService.dead(mongId);
            managementWorkerFeignService.deleteSchedule(mongId);
        } catch (NotFoundException e) {
            managementWorkerFeignService.deleteSchedule(mongId);
        }
    }
}
