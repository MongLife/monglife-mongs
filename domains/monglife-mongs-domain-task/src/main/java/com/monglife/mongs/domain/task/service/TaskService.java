package com.monglife.mongs.domain.task.service;

import com.monglife.mongs.domain.task.dto.etc.GetTaskDto;
import com.monglife.mongs.domain.task.entity.TaskEntity;
import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import com.monglife.mongs.domain.task.exception.AlreadyExistsTaskException;
import com.monglife.mongs.domain.task.exception.NotExistsTaskCodeException;
import com.monglife.mongs.domain.task.exception.NotExistsTaskException;
import com.monglife.mongs.domain.task.repository.ComnCodeRepository;
import com.monglife.mongs.domain.task.repository.LockTaskRepository;
import com.monglife.mongs.domain.task.repository.TaskRepository;
import com.monglife.mongs.module.jpa.entity.ComnCodeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final ComnCodeRepository comnCodeRepository;

    private final TaskRepository taskRepository;

    private final LockTaskRepository lockTaskRepository;


    /**
     * Task 조회
     * @param taskId Task ID
     * @return Task 정보
     */
    @Transactional(readOnly = true)
    public GetTaskDto getTask(Long taskId) {
        return GetTaskDto.of(taskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new NotExistsTaskException(taskId)));
    }

    @Transactional(readOnly = true)
    public Boolean isExistsTask(String appCode, String taskOwnerId, String taskCode, TaskStatusCode taskStatusCode) {
        return taskRepository.findByAppCodeAndTaskOwnerIdAndTaskCodeComnCodeAndTaskStatusCode(appCode, taskOwnerId, taskCode, taskStatusCode).isPresent();
    }

    /**
     * Task 생성
     * @param appCode 앱 코드
     * @param taskOwnerId Task 생성자 ID
     * @param taskCode Task 종류 코드
     * @param expirationSeconds Task 만료 시간
     */
    @Transactional
    public void createTask(String appCode, String taskOwnerId, String taskCode, Long expirationSeconds) {

        ComnCodeEntity comnCodeEntity = comnCodeRepository.findById(taskCode)
                .orElseThrow(() -> new NotExistsTaskCodeException(taskCode));

        taskRepository.findByAppCodeAndTaskOwnerIdAndTaskCodeComnCode(appCode, taskOwnerId, taskCode)
                .ifPresent(taskEntity -> { throw new AlreadyExistsTaskException(taskEntity.getTaskId()); });

        TaskEntity taskEntity = TaskEntity.builder()
                .appCode(appCode)
                .taskOwnerId(taskOwnerId)
                .taskCode(comnCodeEntity)
                .taskStatusCode(TaskStatusCode.PROCESSING)
                .expirationSeconds(expirationSeconds)
                .isCycle(Boolean.FALSE)
                .cycleSeconds(expirationSeconds)
                .build();

        taskRepository.save(taskEntity);
    }

    /**
     * 반복 Task 생성
     * @param appCode 앱 코드
     * @param taskOwnerId Task 생성자 ID
     * @param taskCode Task 종류 코드
     * @param expirationSeconds Task 만료 시간
     * @param cycleSeconds 2번째 cycle 부터 적용될 Task 만료 시간
     */
    @Transactional
    public void createCycleTask(String appCode, String taskOwnerId, String taskCode, Long expirationSeconds, Long cycleSeconds) {

        ComnCodeEntity comnCodeEntity = comnCodeRepository.findById(taskCode)
                .orElseThrow(() -> new NotExistsTaskCodeException(taskCode));

        taskRepository.findByAppCodeAndTaskOwnerIdAndTaskCodeComnCode(appCode, taskOwnerId, taskCode)
                .ifPresent(taskEntity -> { throw new AlreadyExistsTaskException(taskEntity.getTaskId()); });

        TaskEntity taskEntity = TaskEntity.builder()
                .appCode(appCode)
                .taskOwnerId(taskOwnerId)
                .taskCode(comnCodeEntity)
                .taskStatusCode(TaskStatusCode.PROCESSING)
                .expirationSeconds(expirationSeconds)
                .isCycle(Boolean.TRUE)
                .cycleSeconds(cycleSeconds)
                .build();

        taskRepository.save(taskEntity);
    }

    /**
     * Task 반복 처리
     * @param taskId Task ID
     */
    @Transactional
    public void cycleTask(Long taskId) {

        TaskEntity taskEntity = lockTaskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new NotExistsTaskException(taskId));

        taskEntity.cycle();
    }

    /**
     * 모든 Task 삭제
     * @param appCode 앱 코드
     * @param taskOwnerId Task 생성자 ID
     */
    @Transactional
    public void deleteAllTasks(String appCode, String taskOwnerId) {

        lockTaskRepository.findByAppCodeAndTaskOwnerId(appCode, taskOwnerId).forEach(TaskEntity::delete);
    }

    /**
     * Task 삭제
     * @param appCode 앱 코드
     * @param taskOwnerId Task 생성자 ID
     * @param taskCode Task 종류 코드
     */
    @Transactional
    public void deleteTask(String appCode, String taskOwnerId, String taskCode) {

        lockTaskRepository.findByAppCodeAndTaskOwnerIdAndTaskCodeComnCode(appCode, taskOwnerId, taskCode)
                .ifPresent(TaskEntity::delete);
    }

    /**
     * Task 물리적 삭제
     * @param taskId Task ID
     */
    @Transactional
    public void hardDeleteTask(Long taskId) {
        lockTaskRepository.deleteById(taskId);
    }

    /**
     * Task 일시 중지
     * @param appCode 앱 코드
     * @param taskOwnerId Task 생성자 ID
     * @param taskCode Task 종류 코드
     */
    @Transactional
    public void pauseTask(String appCode, String taskOwnerId, String taskCode) {

        lockTaskRepository.findByAppCodeAndTaskOwnerIdAndTaskCodeComnCode(appCode, taskOwnerId, taskCode)
                .ifPresent(TaskEntity::pause);
    }

    /**
     * Task 재기동
     * @param appCode 앱 코드
     * @param taskOwnerId Task 생성자 ID
     * @param taskCode Task 종류 코드
     */
    @Transactional
    public void resumeTask(String appCode, String taskOwnerId, String taskCode) {

        lockTaskRepository.findByAppCodeAndTaskOwnerIdAndTaskCodeComnCode(appCode, taskOwnerId, taskCode)
                .ifPresent(TaskEntity::resume);
    }

    /**
     * Task 전체 일시 중지 (App 종료)
     * @param appCode 앱 코드
     */
    @Transactional
    public void appStopPauseAllTask(String appCode) {
        lockTaskRepository.findByAppCode(appCode).forEach(TaskEntity::appStopPause);
    }

    /**
     * Task 전체 재기동 (App 기동)
     * @param appCode 앱 코드
     */
    @Transactional
    public void appStopResumeAllTask(String appCode) {
        lockTaskRepository.findByAppCode(appCode).forEach(TaskEntity::appStopResume);
    }
}
