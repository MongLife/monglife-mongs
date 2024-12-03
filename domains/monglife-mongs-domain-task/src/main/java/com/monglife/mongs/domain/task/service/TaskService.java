package com.monglife.mongs.domain.task.service;

import com.monglife.mongs.domain.task.entity.TaskEntity;
import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import com.monglife.mongs.domain.task.exception.NotExistsTaskCodeException;
import com.monglife.mongs.domain.task.repository.ComnCodeRepository;
import com.monglife.mongs.domain.task.repository.TaskRepository;
import com.monglife.mongs.module.jpa.entity.ComnCodeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final ComnCodeRepository comnCodeRepository;


    @Transactional
    public void createTask(String appCode, String taskOwnerId, String taskCode, Long expirationSeconds, TaskStatusCode taskStatusCode) {

        ComnCodeEntity comnCodeEntity = comnCodeRepository.findById(taskCode)
                .orElseThrow(() -> new NotExistsTaskCodeException(taskCode));

        taskRepository.findByAppCodeAndTaskOwnerIdAndTaskCodeComnCode(appCode, taskOwnerId, taskCode)
                .ifPresent(taskEntity -> taskRepository.deleteById(taskEntity.getTaskId()));

        TaskEntity taskEntity = TaskEntity.builder()
                .appCode(appCode)
                .taskOwnerId(taskOwnerId)
                .taskCode(comnCodeEntity)
                .taskStatusCode(taskStatusCode)
                .expirationSeconds(expirationSeconds)
                .expiredAt(LocalDateTime.now().plusSeconds(expirationSeconds))
                .isCycle(Boolean.FALSE)
                .cycleSeconds(0L)
                .build();

        taskRepository.save(taskEntity);
    }

    @Transactional
    public void createTask(String appCode, String taskOwnerId, String taskCode, Long expirationSeconds, Boolean isCycle, Long cycleSeconds, TaskStatusCode taskStatusCode) {

        ComnCodeEntity comnCodeEntity = comnCodeRepository.findById(taskCode)
                .orElseThrow(() -> new NotExistsTaskCodeException(taskCode));

        taskRepository.findByAppCodeAndTaskOwnerIdAndTaskCodeComnCode(appCode, taskOwnerId, taskCode)
                .ifPresent(taskEntity -> taskRepository.deleteById(taskEntity.getTaskId()));

        TaskEntity taskEntity = TaskEntity.builder()
                .appCode(appCode)
                .taskOwnerId(taskOwnerId)
                .taskCode(comnCodeEntity)
                .taskStatusCode(taskStatusCode)
                .expirationSeconds(expirationSeconds)
                .expiredAt(LocalDateTime.now().plusSeconds(expirationSeconds))
                .isCycle(isCycle)
                .cycleSeconds(cycleSeconds)
                .build();

        taskRepository.save(taskEntity);
    }

    @Transactional
    public void deleteAllTasks(String appCode, String taskOwnerId) {

        List<TaskEntity> taskEntities = taskRepository.findByAppCodeAndTaskOwnerId(appCode, taskOwnerId);

        taskRepository.deleteAll(taskEntities);
    }

    @Transactional
    public void deleteTask(String appCode, String taskOwnerId, String taskCode) {

        taskRepository.findByAppCodeAndTaskOwnerIdAndTaskCodeComnCode(appCode, taskOwnerId, taskCode)
                .ifPresent(taskRepository::delete);
    }

    @Transactional
    public void pauseTask(String appCode, String taskOwnerId, String taskCode) {

        taskRepository.findByAppCodeAndTaskOwnerIdAndTaskCodeComnCode(appCode, taskOwnerId, taskCode)
                .ifPresent(TaskEntity::pause);
    }

    @Transactional
    public void resumeTask(String appCode, String taskOwnerId, String taskCode) {

        taskRepository.findByAppCodeAndTaskOwnerIdAndTaskCodeComnCode(appCode, taskOwnerId, taskCode)
                .ifPresent(TaskEntity::resume);
    }

    @Transactional
    public void appStopPauseAllTask(String appCode) {
        taskRepository.findByAppCode(appCode).forEach(TaskEntity::appStopPause);
    }

    @Transactional
    public void appStopResumeAllTask(String appCode) {
        taskRepository.findByAppCode(appCode).forEach(TaskEntity::appStopResume);
    }
}
