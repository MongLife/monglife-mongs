package com.monglife.mongs.domain.task.service;

import com.monglife.mongs.domain.task.dto.etc.GetTaskDto;
import com.monglife.mongs.domain.task.entity.TaskEntity;
import com.monglife.mongs.domain.task.enums.TaskStateCode;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    @Transactional
    public void createFixTimeTask(String appCode, String taskOwnerId, String taskCode, LocalTime fixTime) {

        ComnCodeEntity comnCodeEntity = comnCodeRepository.findById(taskCode)
                .orElseThrow(() -> new NotExistsTaskCodeException(taskCode));

        taskRepository.findByAppCodeAndTaskOwnerIdAndTaskCodeComnCode(appCode, taskOwnerId, taskCode)
                .ifPresent(taskEntity -> { throw new AlreadyExistsTaskException(taskEntity.getTaskId()); });

        TaskEntity taskEntity = new TaskEntity(appCode, taskOwnerId, comnCodeEntity, TaskStateCode.FIX_TIME, fixTime);

        taskRepository.save(taskEntity);
    }

    @Transactional
    public void createFixTimeCycleTask(String appCode, String taskOwnerId, String taskCode, LocalTime fixTime) {

        ComnCodeEntity comnCodeEntity = comnCodeRepository.findById(taskCode)
                .orElseThrow(() -> new NotExistsTaskCodeException(taskCode));

        taskRepository.findByAppCodeAndTaskOwnerIdAndTaskCodeComnCode(appCode, taskOwnerId, taskCode)
                .ifPresent(taskEntity -> { throw new AlreadyExistsTaskException(taskEntity.getTaskId()); });

        TaskEntity taskEntity = new TaskEntity(appCode, taskOwnerId, comnCodeEntity, TaskStateCode.FIX_TIME_CYCLE, fixTime);

        taskRepository.save(taskEntity);
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

        TaskEntity taskEntity = new TaskEntity(appCode, taskOwnerId, comnCodeEntity, TaskStateCode.NONE_FIX_TIME, expirationSeconds);

        taskRepository.save(taskEntity);
    }

    /**
     * 반복 Task 생성
     * @param appCode 앱 코드
     * @param taskOwnerId Task 생성자 ID
     * @param taskCode Task 종류 코드
     * @param expirationSeconds Task 만료 시간
     */
    @Transactional
    public void createCycleTask(String appCode, String taskOwnerId, String taskCode, Long expirationSeconds) {

        ComnCodeEntity comnCodeEntity = comnCodeRepository.findById(taskCode)
                .orElseThrow(() -> new NotExistsTaskCodeException(taskCode));

        taskRepository.findByAppCodeAndTaskOwnerIdAndTaskCodeComnCode(appCode, taskOwnerId, taskCode)
                .ifPresent(taskEntity -> { throw new AlreadyExistsTaskException(taskEntity.getTaskId()); });

        TaskEntity taskEntity = new TaskEntity(appCode, taskOwnerId, comnCodeEntity, TaskStateCode.NONE_FIX_TIME_CYCLE, expirationSeconds);

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

        taskRepository.deleteAll(lockTaskRepository.findByAppCodeAndTaskOwnerId(appCode, taskOwnerId));
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
                .ifPresent(taskRepository::delete);
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
