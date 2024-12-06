package com.monglife.mongs.domain.task.listener;

import com.monglife.mongs.domain.task.dto.etc.StartTaskScheduleDto;
import com.monglife.mongs.domain.task.dto.event.StopTaskScheduleEvent;
import com.monglife.mongs.domain.task.entity.TaskEntity;
import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import com.monglife.mongs.domain.task.repository.TaskRepository;
import com.monglife.mongs.domain.task.service.TaskScheduleService;
import com.monglife.mongs.domain.task.service.TaskService;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskEntityListener {

    private final TaskScheduleService taskScheduleService;


    @PostPersist
    public void postPersist(TaskEntity taskEntity) {
        if (TaskStatusCode.PROCESSING.equals(taskEntity.getTaskStatusCode())) {
            taskScheduleService.startTaskSchedule(StartTaskScheduleDto.of(taskEntity));
        }
    }

    /**
     * PAUSE : Task Scheduler 중지 -> 결과 반영
     * PROCESSING : Task Scheduler 시작
     * APP_STOP_PROCESSING : Task Scheduler 중지 -> 결과 반영
     * @param taskEntity 변경된 Task Entity
     */
    @PostUpdate
    public void postUpdate(TaskEntity taskEntity) {
        switch (taskEntity.getTaskStatusCode()) {
            case PROCESSING ->
                    taskScheduleService.startTaskSchedule(StartTaskScheduleDto.of(taskEntity));
            case PAUSE, APP_STOP_PROCESSING, DELETE ->
                    taskScheduleService.stopTaskSchedule(taskEntity.getTaskId());
        }
    }
}
