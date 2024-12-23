package com.monglife.mongs.domain.task.listener;

import com.monglife.mongs.domain.task.dto.event.ExecuteTaskEvent;
import com.monglife.mongs.domain.task.entity.TaskEntity;
import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import com.monglife.mongs.domain.taskSchedule.dto.etc.StartTaskScheduleDto;
import com.monglife.mongs.domain.taskSchedule.dto.etc.StopTaskScheduleDto;
import com.monglife.mongs.domain.taskSchedule.service.TaskScheduleService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskEntityListener {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final TaskScheduleService taskScheduleService;


    /**
     * PAUSE : Task Scheduler 중지 -> 결과 반영
     * PROCESSING : Task Scheduler 시작
     * APP_STOP_PROCESSING : Task Scheduler 중지 -> 결과 반영
     * @param taskEntity 등록된 Task Entity
     */
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
            case PAUSE, APP_STOP_PROCESSING ->
                    taskScheduleService.stopTaskSchedule(StopTaskScheduleDto.of(taskEntity));
        }
    }

    /**
     * @param taskEntity 삭제된 Task Entity
     */
    @PostRemove
    public void preRemove(TaskEntity taskEntity) {

        taskScheduleService.stopTaskSchedule(StopTaskScheduleDto.of(taskEntity));

        applicationEventPublisher.publishEvent(ExecuteTaskEvent.builder()
                .appPackageName(taskEntity.getAppPackageName())
                .taskOwnerId(taskEntity.getTaskOwnerId())
                .taskCode(taskEntity.getComn().getCode())
                .restExpirationSeconds(taskEntity.getRestExpirationSeconds())
                .expirationSeconds(taskEntity.getExpirationSeconds())
                .build());
    }
}
