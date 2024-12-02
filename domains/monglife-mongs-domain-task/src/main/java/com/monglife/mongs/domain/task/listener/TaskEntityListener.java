package com.monglife.mongs.domain.task.listener;

import com.monglife.mongs.domain.task.dto.etc.CreateTaskScheduleDto;
import com.monglife.mongs.domain.task.entity.TaskEntity;
import com.monglife.mongs.domain.task.service.TaskScheduleService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskEntityListener {

    private final TaskScheduleService taskScheduleService;

    @PostPersist
    public void postPersist(TaskEntity taskEntity) {

        log.info("postPersist: {}", taskEntity);

        taskScheduleService.createTaskSchedule(CreateTaskScheduleDto.of(taskEntity));
    }

    @PreUpdate
    public void preUpdate(TaskEntity taskEntity) {
        taskEntity.setPreviousTaskEntity(taskEntity.clone());
    }

    @PostUpdate
    public void postUpdate(TaskEntity taskEntity) {

        log.info("pre: {}, next: {}", taskEntity.getPreviousTaskEntity(), taskEntity);

//        if (TaskStatusCode.PAUSE.equals(taskEntity.getTaskStatusCode())) {
//            // 테스크 스케줄러 삭제
//            taskScheduleService.deleteTaskSchedule(taskEntity.getTaskId());
//        } else if (TaskStatusCode.PROCESSING.equals(taskEntity.getTaskStatusCode())) {
//            // 테스크 스케줄러 재생성
//            taskScheduleService.createTaskSchedule(CreateTaskScheduleDto.of(taskEntity));
//        }
    }

    @PostRemove
    public void postRemove(TaskEntity taskEntity) {
        taskScheduleService.deleteTaskSchedule(taskEntity.getTaskId());
    }
}
