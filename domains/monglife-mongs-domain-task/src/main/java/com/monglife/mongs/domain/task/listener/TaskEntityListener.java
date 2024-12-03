package com.monglife.mongs.domain.task.listener;

import com.monglife.mongs.domain.task.dto.etc.CreateTaskScheduleDto;
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
            taskScheduleService.startTaskSchedule(CreateTaskScheduleDto.of(taskEntity));
        }
    }

    @PostUpdate
    public void postUpdate(TaskEntity taskEntity) {

        if (TaskStatusCode.PAUSE.equals(taskEntity.getTaskStatusCode())) {
            // 테스크 스케줄러 삭제
            taskScheduleService.deleteTaskSchedule(taskEntity.getTaskId());
        } else if (TaskStatusCode.PROCESSING.equals(taskEntity.getTaskStatusCode())) {
            // 테스크 스케줄러 재생성
            taskScheduleService.startTaskSchedule(CreateTaskScheduleDto.of(taskEntity));
        }
    }

    @PreRemove
    public void postRemove(TaskEntity taskEntity) {
        taskScheduleService.deleteTaskSchedule(taskEntity.getTaskId());
    }
}
