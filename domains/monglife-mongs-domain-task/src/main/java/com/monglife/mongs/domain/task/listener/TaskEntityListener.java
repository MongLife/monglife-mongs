package com.monglife.mongs.domain.task.listener;

import com.monglife.mongs.domain.task.dto.etc.GetTaskDto;
import com.monglife.mongs.domain.task.dto.etc.StartTaskScheduleDto;
import com.monglife.mongs.domain.task.dto.etc.StopTaskScheduleDto;
import com.monglife.mongs.domain.task.entity.TaskEntity;
import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import com.monglife.mongs.domain.task.service.TaskScheduleService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreRemove;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            case PAUSE, APP_STOP_PROCESSING ->
                    taskScheduleService.stopTaskSchedule(StopTaskScheduleDto.of(taskEntity));
        }
    }

    @PreRemove
    public void preRemove(TaskEntity taskEntity) {

        taskScheduleService.stopTaskSchedule(StopTaskScheduleDto.of(taskEntity));

        log.info("[DEL] {} -> {}", taskEntity.getTaskOwnerId(), taskEntity.getTaskCode());
    }
}
