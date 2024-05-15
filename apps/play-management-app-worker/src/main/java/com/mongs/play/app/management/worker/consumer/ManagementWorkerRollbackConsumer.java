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
public class ManagementWorkerRollbackConsumer {

    private final TaskService taskService;

    @KafkaListener(topics = { "rollback.zeroEvolutionSchedule" })
    public void zeroEvolutionSchedule(ZeroEvolutionScheduleEvent payload) {}

    @KafkaListener(topics = { "rollback.decreaseStatusSchedule" })
    public void decreaseStatusSchedule(DecreaseStatusScheduleEvent payload) {}

    @KafkaListener(topics = { "rollback.increaseStatusSchedule" })
    public void increaseStatusSchedule(IncreaseStatusScheduleEvent payload) {}

    @KafkaListener(topics = { "rollback.increasePoopCountSchedule" })
    public void increasePoopCountSchedule(IncreasePoopCountScheduleEvent payload) {}

    @KafkaListener(topics = { "rollback.deadSchedule" })
    public void deadSchedule(DeadScheduleEvent payload) {}
}
