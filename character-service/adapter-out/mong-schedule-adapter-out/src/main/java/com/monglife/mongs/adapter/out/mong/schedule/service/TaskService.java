package com.monglife.mongs.adapter.out.mong.schedule.service;

import com.monglife.mongs.adapter.out.mong.schedule.entity.TaskEntity;
import com.monglife.mongs.adapter.out.mong.schedule.entity.TaskScheduleEntity;
import com.monglife.mongs.adapter.out.mong.schedule.enums.TaskStateCode;
import com.monglife.mongs.adapter.out.mong.schedule.repository.TaskRepository;
import com.monglife.mongs.adapter.out.mong.schedule.repository.TaskScheduleRepository;
import com.monglife.mongs.application.mong.port.enums.SchedulerType;
import com.monglife.mongs.application.mong.port.out.MongSchedulerPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;

@Service
@RequiredArgsConstructor
public class TaskService implements MongSchedulerPort {

    private static final String APP_PACKAGE_NAME = "com.wear.mongs";

    private final TaskRepository taskRepository;

    private final TaskScheduleRepository taskScheduleRepository;

    private final ApplicationEventPublisher publisher;

    private final ScheduledExecutorService executor;

    /**
     * 일회성 테스크 스케줄 등록
     * @param mongId 몽 ID
     * @param schedulerType 스케줄 타입 코드
     */
    @Override
    @Transactional
    public Optional<Long> createTaskPort(Long mongId, SchedulerType schedulerType) {

        String taskOwnerId = String.valueOf(mongId);

        TaskEntity taskEntity = taskRepository.findByAppPackageNameAndTaskOwnerIdAndSchedulerTypeCode(APP_PACKAGE_NAME, taskOwnerId, schedulerType.getCode())
                .orElseGet(() -> new TaskEntity(APP_PACKAGE_NAME, taskOwnerId, schedulerType.getCode(), TaskStateCode.NONE_FIX_TIME, schedulerType.getExpiration()));

        final TaskEntity finalTaskEntity = taskRepository.save(taskEntity);

        taskScheduleRepository.save(TaskScheduleEntity.of(taskEntity))
                .start(executor, () -> publisher.publishEvent(finalTaskEntity));

        return Optional.of(finalTaskEntity.getTaskId());
    }

    /**
     * 반복성 테스크 스케줄 등록
     * @param mongId 몽 ID
     * @param schedulerType 스케줄 타입 코드
     */
    @Override
    @Transactional
    public Optional<Long> createCycleTaskPort(Long mongId, SchedulerType schedulerType) {

        String taskOwnerId = String.valueOf(mongId);

        TaskEntity taskEntity = taskRepository.findByAppPackageNameAndTaskOwnerIdAndSchedulerTypeCode(APP_PACKAGE_NAME, taskOwnerId, schedulerType.getCode())
                .orElseGet(() -> new TaskEntity(APP_PACKAGE_NAME, taskOwnerId, schedulerType.getCode(), TaskStateCode.NONE_FIX_TIME_CYCLE, schedulerType.getExpiration()));

        final TaskEntity finalTaskEntity = taskRepository.save(taskEntity);

        taskScheduleRepository.save(TaskScheduleEntity.of(taskEntity))
                .start(executor, () -> publisher.publishEvent(finalTaskEntity));

        return Optional.of(finalTaskEntity.getTaskId());
    }

    /**
     * 고정 시간 반복성 테스크 스케줄 등록
     * @param mongId 몽 ID
     * @param schedulerType 스케줄 타입 코드
     * @param time 고정 시간
     */
    @Override
    @Transactional
    public Optional<Long> createFixedTimeCycleTaskPort(Long mongId, SchedulerType schedulerType, LocalTime time) {

        String taskOwnerId = String.valueOf(mongId);

        TaskEntity taskEntity = taskRepository.findByAppPackageNameAndTaskOwnerIdAndSchedulerTypeCode(APP_PACKAGE_NAME, taskOwnerId, schedulerType.getCode())
                .orElseGet(() -> new TaskEntity(APP_PACKAGE_NAME, taskOwnerId, schedulerType.getCode(), TaskStateCode.FIX_TIME_CYCLE, time));

        final TaskEntity finalTaskEntity = taskRepository.save(taskEntity);

        taskScheduleRepository.save(TaskScheduleEntity.of(taskEntity))
                .start(executor, () -> publisher.publishEvent(finalTaskEntity));

        return Optional.of(finalTaskEntity.getTaskId());
    }

    /**
     * 테스크 스케줄 삭제
     * @param mongId 몽 ID
     * @param schedulerType 스케줄 타입 코드
     */
    @Override
    @Transactional
    public void deleteTaskPort(Long mongId, SchedulerType schedulerType) {

        String taskOwnerId = String.valueOf(mongId);

        Optional<TaskEntity> taskEntityOptional = taskRepository.findByAppPackageNameAndTaskOwnerIdAndSchedulerTypeCodeWithLock(APP_PACKAGE_NAME, taskOwnerId, schedulerType.getCode());

        // 테스크 삭제
        taskEntityOptional.ifPresent(taskEntity -> {
            taskRepository.delete(taskEntity);
            taskScheduleRepository.findByTaskId(taskEntity.getTaskId()).ifPresent(TaskScheduleEntity::stop);
            taskScheduleRepository.deleteByTaskId(taskEntity.getTaskId());
        });
    }

    /**
     * 테스크 스케줄 전체 삭제
     * @param mongId 몽 ID
     */
    @Override
    @Transactional
    public void deleteAllTaskPort(Long mongId) {

        String taskOwnerId = String.valueOf(mongId);

        // 테스크 삭제
        taskRepository.findAllByAppPackageNameAndTaskOwnerIdWithLock(APP_PACKAGE_NAME, taskOwnerId).forEach(taskEntity -> {
            taskRepository.delete(taskEntity);
            taskScheduleRepository.findByTaskId(taskEntity.getTaskId()).ifPresent(TaskScheduleEntity::stop);
            taskScheduleRepository.deleteByTaskId(taskEntity.getTaskId());
        });
    }

    /**
     * 테스크 스케줄 전체 일시 중지 (App 종료)
     */
    @Transactional
    public List<TaskEntity> appStopPauseAllTask() {

        List<TaskEntity> taskEntities = taskRepository.findAllByAppPackageNameWithLock(APP_PACKAGE_NAME);

        taskEntities.forEach(taskEntity -> {
            // 테스크 일시 중지
            taskEntity.appStopPause();

            // 테스크 스케줄 삭제
            taskScheduleRepository.findByTaskId(taskEntity.getTaskId()).ifPresent(TaskScheduleEntity::stop);
            taskScheduleRepository.deleteByTaskId(taskEntity.getTaskId());
        });

        return taskEntities;
    }

    /**
     * 테스크 스케줄 전체 재기동 (App 기동)
     */
    @Transactional
    public List<TaskEntity> appStopResumeAllTask() {

        List<TaskEntity> taskEntities = taskRepository.findAllByAppPackageNameWithLock(APP_PACKAGE_NAME);

        taskEntities.forEach(taskEntity -> {
            // 테스크 재시작
            taskEntity.appStopResume();

            // 테스크 스케줄 시작
            taskScheduleRepository.save(TaskScheduleEntity.of(taskEntity))
                    .start(executor, () -> publisher.publishEvent(taskEntity));
        });

        return taskEntities;
    }
}
