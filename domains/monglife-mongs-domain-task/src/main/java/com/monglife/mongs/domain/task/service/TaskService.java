package com.monglife.mongs.domain.task.service;

import com.monglife.mongs.domain.task.entity.ComnCodeEntity;
import com.monglife.mongs.domain.task.entity.TaskEntity;
import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import com.monglife.mongs.domain.task.exception.NotExistsTaskCodeException;
import com.monglife.mongs.domain.task.exception.NotExistsTaskException;
import com.monglife.mongs.domain.task.repository.ComnCodeRepository;
import com.monglife.mongs.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final ComnCodeRepository comnCodeRepository;


    @Transactional
    public void createTask(String appCode, String taskOwnerId, String taskCode, Long expirationSeconds) {

        log.info("{}, {}, {}, {}", appCode, taskOwnerId, taskCode, expirationSeconds);

        ComnCodeEntity comnCodeEntity = comnCodeRepository.findById(taskCode)
                .orElseThrow(() -> new NotExistsTaskCodeException(taskCode));

        TaskEntity taskEntity = TaskEntity.builder()
                .appCode(appCode)
                .taskOwnerId(taskOwnerId)
                .taskCode(comnCodeEntity)
                .taskStatusCode(TaskStatusCode.PROCESSING)
                .expirationSeconds(expirationSeconds)
                .expiredAt(LocalDateTime.now().plusSeconds(expirationSeconds))
                .isCycle(Boolean.FALSE)
                .cycleSeconds(0L)
                .build();

        taskRepository.save(taskEntity);
    }

    @Transactional
    public void createTask(String appCode, String taskOwnerId, String taskCode, Boolean isCycle, Long cycleSeconds) {

        ComnCodeEntity comnCodeEntity = comnCodeRepository.findById(taskCode)
                .orElseThrow(() -> new NotExistsTaskCodeException(taskCode));

        TaskEntity taskEntity = TaskEntity.builder()
                .appCode(appCode)
                .taskOwnerId(taskOwnerId)
                .taskCode(comnCodeEntity)
                .taskStatusCode(TaskStatusCode.PROCESSING)
                .expirationSeconds(cycleSeconds)
                .expiredAt(LocalDateTime.now().plusSeconds(cycleSeconds))
                .isCycle(isCycle)
                .cycleSeconds(cycleSeconds)
                .build();

        taskRepository.save(taskEntity);
    }

    @Transactional
    public void createTask(String appCode, String taskOwnerId, String taskCode, Long expirationSeconds, Boolean isCycle, Long cycleSeconds) {

        ComnCodeEntity comnCodeEntity = comnCodeRepository.findById(taskCode)
                .orElseThrow(() -> new NotExistsTaskCodeException(taskCode));

        TaskEntity taskEntity = TaskEntity.builder()
                .appCode(appCode)
                .taskOwnerId(taskOwnerId)
                .taskCode(comnCodeEntity)
                .taskStatusCode(TaskStatusCode.PROCESSING)
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

        TaskEntity taskEntity = taskRepository.findByAppCodeAndTaskOwnerIdAndTaskCodeComnCode(appCode, taskOwnerId, taskCode)
                .orElseThrow(() -> new NotExistsTaskException(appCode, taskOwnerId, taskCode));

        taskRepository.delete(taskEntity);
    }

    @Transactional
    public void pauseTask(String appCode, String taskOwnerId, String taskCode) {

        TaskEntity taskEntity = taskRepository.findByAppCodeAndTaskOwnerIdAndTaskCodeComnCode(appCode, taskOwnerId, taskCode)
                .orElseThrow(() -> new NotExistsTaskException(appCode, taskOwnerId, taskCode));

        taskEntity.setTaskStatusCode(TaskStatusCode.PAUSE);
    }

    @Transactional
    public void resumeTask(String appCode, String taskOwnerId, String taskCode) {

        TaskEntity taskEntity = taskRepository.findByAppCodeAndTaskOwnerIdAndTaskCodeComnCode(appCode, taskOwnerId, taskCode)
                .orElseThrow(() -> new NotExistsTaskException(appCode, taskOwnerId, taskCode));

        taskEntity.setTaskStatusCode(TaskStatusCode.PROCESSING);
    }

    @Transactional
    public void appStopPauseTask(String appCode, String taskOwnerId, String taskCode) {

        TaskEntity taskEntity = taskRepository.findByAppCodeAndTaskOwnerIdAndTaskCodeComnCode(appCode, taskOwnerId, taskCode)
                .orElseThrow(() -> new NotExistsTaskException(appCode, taskOwnerId, taskCode));

        taskEntity.setTaskStatusCode(TaskStatusCode.APP_STOP_PAUSE);
    }

    @Transactional
    public void appStopResumeTask(String appCode, String taskOwnerId, String taskCode) {

        TaskEntity taskEntity = taskRepository.findByAppCodeAndTaskOwnerIdAndTaskCodeComnCode(appCode, taskOwnerId, taskCode)
                .orElseThrow(() -> new NotExistsTaskException(appCode, taskOwnerId, taskCode));

        taskEntity.setTaskStatusCode(TaskStatusCode.APP_STOP_PROCESSING);
    }
}
