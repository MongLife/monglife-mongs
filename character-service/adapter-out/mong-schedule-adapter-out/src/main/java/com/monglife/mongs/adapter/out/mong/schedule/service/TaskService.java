package com.monglife.mongs.adapter.out.mong.schedule.service;

import com.monglife.mongs.adapter.out.mong.schedule.entity.TaskEntity;
import com.monglife.mongs.adapter.out.mong.schedule.entity.TaskScheduleEntity;
import com.monglife.mongs.adapter.out.mong.schedule.enums.TaskStateCode;
import com.monglife.mongs.adapter.out.mong.schedule.enums.TaskTypeCode;
import com.monglife.mongs.adapter.out.mong.schedule.repository.TaskRepository;
import com.monglife.mongs.adapter.out.mong.schedule.repository.TaskScheduleRepository;
import com.monglife.mongs.application.mong.port.enums.SchedulerType;
import com.monglife.mongs.application.mong.port.out.MongSchedulerPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class TaskService implements MongSchedulerPort {

    private static final String APP_PACKAGE_NAME = "com.monglife.mongs";

    private final TaskRepository taskRepository;

    private final TaskScheduleRepository taskScheduleRepository;

    private final ApplicationEventPublisher publisher;

    private final ScheduledExecutorService executor;

    public TaskService(
            @Autowired TaskRepository taskRepository,
            @Autowired TaskScheduleRepository taskScheduleRepository,
            @Autowired ApplicationEventPublisher publisher,
            @Qualifier("taskScheduledExecutorService") ScheduledExecutorService executor
    ) {
        this.taskRepository = taskRepository;
        this.taskScheduleRepository = taskScheduleRepository;
        this.publisher = publisher;
        this.executor = executor;
    }

    /**
     * 일회성 테스크 스케줄 등록
     * @param mongId 몽 ID
     * @param accountId 계정 ID
     * @param schedulerType 스케줄 타입 코드
     */
    @Override
    @Transactional
    public Optional<Long> createTaskPort(Long mongId, Long accountId, SchedulerType schedulerType) {

        TaskEntity taskEntity = taskRepository.findByAppPackageNameAndMongIdAndSchedulerTypeCode(APP_PACKAGE_NAME, mongId, schedulerType.getCode())
                .orElseGet(() -> new TaskEntity(APP_PACKAGE_NAME, mongId, accountId, schedulerType.getCode(), TaskTypeCode.NONE_FIX_TIME, schedulerType.getExpiration()));

        final TaskEntity finalTaskEntity = taskRepository.save(taskEntity);

        taskScheduleRepository.save(TaskScheduleEntity.of(taskEntity))
                .start(executor, () -> publisher.publishEvent(finalTaskEntity));

        return Optional.of(finalTaskEntity.getTaskId());
    }

    /**
     * 반복성 테스크 스케줄 등록
     * @param mongId 몽 ID
     * @param accountId 계정 ID
     * @param schedulerType 스케줄 타입 코드
     */
    @Override
    @Transactional
    public Optional<Long> createCycleTaskPort(Long mongId, Long accountId, SchedulerType schedulerType) {

        TaskEntity taskEntity = taskRepository.findByAppPackageNameAndMongIdAndSchedulerTypeCode(APP_PACKAGE_NAME, mongId, schedulerType.getCode())
                .orElseGet(() -> new TaskEntity(APP_PACKAGE_NAME, mongId, accountId, schedulerType.getCode(), TaskTypeCode.NONE_FIX_TIME_CYCLE, schedulerType.getExpiration()));

        final TaskEntity finalTaskEntity = taskRepository.save(taskEntity);

        taskScheduleRepository.save(TaskScheduleEntity.of(taskEntity))
                .start(executor, () -> publisher.publishEvent(finalTaskEntity));

        return Optional.of(finalTaskEntity.getTaskId());
    }

    /**
     * 고정 시간 반복성 테스크 스케줄 등록
     * @param mongId 몽 ID
     * @param accountId 계정 ID
     * @param schedulerType 스케줄 타입 코드
     * @param time 고정 시간
     */
    @Override
    @Transactional
    public Optional<Long> createFixedTimeCycleTaskPort(Long mongId, Long accountId, SchedulerType schedulerType, LocalTime time) {

        TaskEntity taskEntity = taskRepository.findByAppPackageNameAndMongIdAndSchedulerTypeCode(APP_PACKAGE_NAME, mongId, schedulerType.getCode())
                .orElseGet(() -> new TaskEntity(APP_PACKAGE_NAME, mongId, accountId, schedulerType.getCode(), TaskTypeCode.FIX_TIME_CYCLE, time));

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

        Optional<TaskEntity> taskEntityOptional = taskRepository.findByAppPackageNameAndMongIdAndSchedulerTypeCodeWithLock(APP_PACKAGE_NAME, mongId, schedulerType.getCode());

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

        // 테스크 삭제
        taskRepository.findAllByAppPackageNameAndMongIdWithLock(APP_PACKAGE_NAME, mongId).forEach(taskEntity -> {
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
            if (TaskStateCode.PROCESSING.equals(taskEntity.getStateCode())) {
                taskScheduleRepository.save(TaskScheduleEntity.of(taskEntity))
                        .start(executor, () -> publisher.publishEvent(taskEntity));
            }
        });

        return taskEntities;
    }
}
