package com.mongs.play.app.management.worker.consumer;

import com.mongs.play.module.kafka.event.managementWorker.*;
import com.mongs.play.module.kafka.service.KafkaService;
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

    @KafkaListener(topics = { KafkaService.StopTopic.DECREASE_STATUS_SCHEDULE })
    public void decreaseStatusSchedule(DecreaseStatusScheduleEvent payload) {
        Long mongId = payload.getMongId();
        taskService.stopTask(mongId, TaskCode.DECREASE_STATUS);
    }

    @KafkaListener(topics = { KafkaService.StopTopic.INCREASE_STATUS_SCHEDULE })
    public void increaseStatusSchedule(IncreaseStatusScheduleEvent payload) {
        Long mongId = payload.getMongId();
        taskService.stopTask(mongId, TaskCode.INCREASE_STATUS);
    }

    @KafkaListener(topics = { KafkaService.StopTopic.INCREASE_POOP_COUNT_SCHEDULE })
    public void increasePoopCountSchedule(IncreasePoopCountScheduleEvent payload) {
        Long mongId = payload.getMongId();
        taskService.stopTask(mongId, TaskCode.INCREASE_POOP_COUNT);
    }
}
