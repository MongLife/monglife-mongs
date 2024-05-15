package com.mongs.play.app.management.worker.consumer;

import com.mongs.play.module.kafka.event.managementWorker.*;
import com.mongs.play.module.task.enums.TaskCode;
import com.mongs.play.module.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagementWorkerStopConsumer {

    private final TaskService taskService;

    @KafkaListener(topics = { "stop.zeroEvolutionSchedule" })
    public void zeroEvolutionSchedule(ZeroEvolutionScheduleEvent payload) {}

    @KafkaListener(topics = { "stop.decreaseStatusSchedule" })
    public void decreaseStatusSchedule(DecreaseStatusScheduleEvent payload) {
        Long mongId = payload.getMongId();
        taskService.stopTask(mongId, TaskCode.DECREASE_STATUS);
    }

    @KafkaListener(topics = { "stop.increaseStatusSchedule" })
    public void increaseStatusSchedule(IncreaseStatusScheduleEvent payload) {
        Long mongId = payload.getMongId();
        taskService.stopTask(mongId, TaskCode.INCREASE_STATUS);
    }

    @KafkaListener(topics = { "stop.increasePoopCountSchedule" })
    public void increasePoopCountSchedule(IncreasePoopCountScheduleEvent payload) {
        Long mongId = payload.getMongId();
        taskService.stopTask(mongId, TaskCode.INCREASE_POOP_COUNT);
    }

    @KafkaListener(topics = { "stop.deadSchedule" })
    public void deadSchedule(DeadScheduleEvent payload) {}
}
