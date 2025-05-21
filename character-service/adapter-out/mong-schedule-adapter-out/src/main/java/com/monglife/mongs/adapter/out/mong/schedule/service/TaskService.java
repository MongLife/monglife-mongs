package com.monglife.mongs.adapter.out.mong.schedule.service;

import com.monglife.mongs.adapter.out.mong.schedule.entity.TaskEntity;
import com.monglife.mongs.adapter.out.mong.schedule.enums.TaskStateCode;
import com.monglife.mongs.adapter.out.mong.schedule.repository.TaskRepository;
import com.monglife.mongs.application.mong.port.enums.MongSchedulerTypeCode;
import com.monglife.mongs.application.mong.port.out.MongSchedulerPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService implements MongSchedulerPort {

    private static final String APP_PACKAGE_NAME = "com.wear.mongs";

    private final TaskRepository taskRepository;

    /**
     * 일회성 몽 스케줄 등록
     * @param mongId 몽 ID
     * @param mongSchedulerTypeCode 몽 스케줄 타입 코드
     */
    @Override
    @Transactional
    public Optional<Long> createTaskPort(Long mongId, MongSchedulerTypeCode mongSchedulerTypeCode) {

        String taskOwnerId = String.valueOf(mongId);

        TaskEntity taskEntity = taskRepository.findByAppPackageNameAndTaskOwnerIdAndSchedulerTypeCode(APP_PACKAGE_NAME, taskOwnerId, mongSchedulerTypeCode)
                .orElseGet(() -> new TaskEntity(APP_PACKAGE_NAME, taskOwnerId, mongSchedulerTypeCode, TaskStateCode.NONE_FIX_TIME, mongSchedulerTypeCode.getExpiration()));

        taskEntity = taskRepository.save(taskEntity);

        return Optional.of(taskEntity.getTaskId());
    }

    /**
     * 반복성 몽 스케줄 등록
     * @param mongId 몽 ID
     * @param mongSchedulerTypeCode 몽 스케줄 타입 코드
     */
    @Override
    @Transactional
    public Optional<Long> createCycleTaskPort(Long mongId, MongSchedulerTypeCode mongSchedulerTypeCode) {

        String taskOwnerId = String.valueOf(mongId);

        TaskEntity taskEntity = taskRepository.findByAppPackageNameAndTaskOwnerIdAndSchedulerTypeCode(APP_PACKAGE_NAME, taskOwnerId, mongSchedulerTypeCode)
                .orElseGet(() -> new TaskEntity(APP_PACKAGE_NAME, taskOwnerId, mongSchedulerTypeCode, TaskStateCode.NONE_FIX_TIME_CYCLE, mongSchedulerTypeCode.getExpiration()));

        taskEntity = taskRepository.save(taskEntity);

        return Optional.of(taskEntity.getTaskId());
    }

    /**
     * 고정 시간 반복성 몽 스케줄 등록
     * @param mongId 몽 ID
     * @param mongSchedulerTypeCode 몽 스케줄 타입 코드
     * @param time 고정 시간
     */
    @Override
    @Transactional
    public Optional<Long> createFixedTimeCycleTaskPort(Long mongId, MongSchedulerTypeCode mongSchedulerTypeCode, LocalTime time) {

        String taskOwnerId = String.valueOf(mongId);

        TaskEntity taskEntity = taskRepository.findByAppPackageNameAndTaskOwnerIdAndSchedulerTypeCode(APP_PACKAGE_NAME, taskOwnerId, mongSchedulerTypeCode)
                .orElseGet(() -> new TaskEntity(APP_PACKAGE_NAME, taskOwnerId, mongSchedulerTypeCode, TaskStateCode.FIX_TIME_CYCLE, time));

        taskEntity = taskRepository.save(taskEntity);

        return Optional.of(taskEntity.getTaskId());
    }

    /**
     * Task 일시 중지
     * @param mongId 몽 ID
     * @param mongSchedulerTypeCode 몽 스케줄 타입 코드
     */
    @Override
    @Transactional
    public Optional<Long> pauseTask(Long mongId, MongSchedulerTypeCode mongSchedulerTypeCode) {

        String taskOwnerId = String.valueOf(mongId);

        Optional<TaskEntity> taskEntityOptional = taskRepository.findByAppPackageNameAndTaskOwnerIdAndSchedulerTypeCodeWithLock(APP_PACKAGE_NAME, taskOwnerId, mongSchedulerTypeCode);

        if (taskEntityOptional.isPresent()) {
            taskEntityOptional.get().pause();
            return Optional.of(taskEntityOptional.get().getTaskId());
        }

        return Optional.empty();
    }

    /**
     * Task 재기동
     * @param mongId 몽 ID
     * @param mongSchedulerTypeCode 몽 스케줄 타입 코드
     */
    @Override
    @Transactional
    public Optional<Long> resumeTask(Long mongId, MongSchedulerTypeCode mongSchedulerTypeCode) {

        String taskOwnerId = String.valueOf(mongId);

        Optional<TaskEntity> taskEntityOptional = taskRepository.findByAppPackageNameAndTaskOwnerIdAndSchedulerTypeCodeWithLock(APP_PACKAGE_NAME, taskOwnerId, mongSchedulerTypeCode);

        if (taskEntityOptional.isPresent()) {
            taskEntityOptional.get().resume();
            return Optional.of(taskEntityOptional.get().getTaskId());
        }

        return Optional.empty();
    }

    /**
     * 몽 스케줄 삭제
     * @param mongId 몽 ID
     * @param mongSchedulerTypeCode 몽 스케줄 타입 코드
     */
    @Override
    @Transactional
    public void deleteTaskPort(Long mongId, MongSchedulerTypeCode mongSchedulerTypeCode) {

        String taskOwnerId = String.valueOf(mongId);

        Optional<TaskEntity> taskEntityOptional = taskRepository.findByAppPackageNameAndTaskOwnerIdAndSchedulerTypeCodeWithLock(APP_PACKAGE_NAME, taskOwnerId, mongSchedulerTypeCode);

        taskEntityOptional.ifPresent(taskRepository::delete);
    }

    /**
     * 몽 스케줄 전체 삭제
     * @param mongId 몽 ID
     */
    @Override
    @Transactional
    public void deleteAllTaskPort(Long mongId) {

        String taskOwnerId = String.valueOf(mongId);

        taskRepository.deleteAll(taskRepository.findAllByAppPackageNameAndTaskOwnerIdWithLock(APP_PACKAGE_NAME, taskOwnerId));
    }

    /**
     * Task 전체 일시 중지 (App 종료)
     */
    @Transactional
    public List<TaskEntity> appStopPauseAllTask() {

        List<TaskEntity> taskEntities = taskRepository.findAllByAppPackageNameWithLock(APP_PACKAGE_NAME);

        taskEntities.forEach(TaskEntity::appStopPause);

        return taskEntities;
    }

    /**
     * Task 전체 재기동 (App 기동)
     */
    @Transactional
    public List<TaskEntity> appStopResumeAllTask() {

        List<TaskEntity> taskEntities = taskRepository.findAllByAppPackageNameWithLock(APP_PACKAGE_NAME);

        taskEntities.forEach(TaskEntity::appStopResume);

        return taskEntities;
    }
}
