package com.monglife.mongs.adapter.out.mong.schedule.listener;

import com.monglife.mongs.adapter.out.mong.schedule.entity.TaskEntity;
import com.monglife.mongs.adapter.out.mong.schedule.service.TaskScheduleService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskEntityListener {

    private final TaskScheduleService taskScheduleService;

    /**
     * PAUSE : Task Scheduler 중지 -> 결과 반영
     * PROCESSING : Task Scheduler 시작
     * APP_STOP_PROCESSING : Task Scheduler 중지 -> 결과 반영
     * @param taskEntity 등록된 Task Entity
     */
    @PostPersist
    public void postPersist(TaskEntity taskEntity) {
        taskScheduleService.startTaskSchedule(taskEntity);
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
            case PROCESSING -> taskScheduleService.startTaskSchedule(taskEntity);
            case PAUSE, APP_STOP_PROCESSING -> taskScheduleService.stopTaskSchedule(taskEntity.getTaskId());
        }
    }

    /**
     * @param taskEntity 삭제된 Task Entity
     */
    @PostRemove
    public void postRemove(TaskEntity taskEntity) {
        taskScheduleService.stopTaskSchedule(taskEntity.getTaskId());
    }
}
