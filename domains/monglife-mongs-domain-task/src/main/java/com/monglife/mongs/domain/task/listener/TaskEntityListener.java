package com.monglife.mongs.domain.task.listener;

import com.monglife.mongs.domain.task.dto.event.DeleteCycleTaskEvent;
import com.monglife.mongs.domain.task.dto.event.DeleteTaskEvent;
import com.monglife.mongs.domain.task.entity.TaskEntity;
import com.monglife.mongs.domain.taskSchedule.dto.event.StartTaskScheduleEvent;
import com.monglife.mongs.domain.taskSchedule.dto.event.StopTaskScheduleEvent;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskEntityListener {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * PAUSE : Task Scheduler 중지 -> 결과 반영
     * PROCESSING : Task Scheduler 시작
     * APP_STOP_PROCESSING : Task Scheduler 중지 -> 결과 반영
     * @param taskEntity 등록된 Task Entity
     */
    @PostPersist
    public void postPersist(TaskEntity taskEntity) {
        applicationEventPublisher.publishEvent(StartTaskScheduleEvent.of(taskEntity));
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
                applicationEventPublisher.publishEvent(StartTaskScheduleEvent.of(taskEntity));
            case PAUSE, APP_STOP_PROCESSING ->
                applicationEventPublisher.publishEvent(StopTaskScheduleEvent.of(taskEntity));
        }
    }

    /**
     * @param taskEntity 삭제된 Task Entity
     */
    @PostRemove
    public void postRemove(TaskEntity taskEntity) {

        applicationEventPublisher.publishEvent(StopTaskScheduleEvent.of(taskEntity));

        if (!taskEntity.isCycle()) {
            // 일회성 Task 삭제 이벤트
            applicationEventPublisher.publishEvent(DeleteTaskEvent.builder()
                    .appPackageName(taskEntity.getAppPackageName())
                    .taskOwnerId(taskEntity.getTaskOwnerId())
                    .taskCode(taskEntity.getComn().getCode())
                    .expiredAt(taskEntity.getExpiredAt())
                    .expirationSeconds(taskEntity.getExpirationSeconds())
                    .build());
        } else {
            // 반복 Task 삭제 이벤트
            applicationEventPublisher.publishEvent(DeleteCycleTaskEvent.builder()
                    .appPackageName(taskEntity.getAppPackageName())
                    .taskOwnerId(taskEntity.getTaskOwnerId())
                    .taskCode(taskEntity.getComn().getCode())
                    .expiredAt(taskEntity.getExpiredAt())
                    .expirationSeconds(taskEntity.getExpirationSeconds())
                    .build());

        }
    }
}
