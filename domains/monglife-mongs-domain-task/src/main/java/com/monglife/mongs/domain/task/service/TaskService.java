package com.monglife.mongs.domain.task.service;

import com.monglife.mongs.domain.task.entity.TaskEntity;
import com.monglife.mongs.domain.task.enums.TaskStateCode;
import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import com.monglife.mongs.domain.task.exception.NotExistsTaskCodeException;
import com.monglife.mongs.domain.task.exception.NotExistsTaskException;
import com.monglife.mongs.domain.task.repository.ComnCodeRepository;
import com.monglife.mongs.domain.task.repository.LockTaskRepository;
import com.monglife.mongs.domain.task.repository.TaskRepository;
import com.monglife.mongs.domain.task.vo.TaskVo;
import com.monglife.mongs.module.jpa.entity.ComnCodeEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final ComnCodeRepository comnCodeRepository;

    private final TaskRepository taskRepository;

    private final LockTaskRepository lockTaskRepository;

    /**
     * Task 존재 여부 조회
     * @param appPackageName 앱 패키지 명
     * @param taskOwnerId Task 소유자 ID
     * @param taskCode Task 종류 코드
     * @param taskStatusCode Task 상태 코드
     * @return Task 존재 여부
     */
    @Transactional(readOnly = true)
    public Boolean isExistsTask(String appPackageName, String taskOwnerId, String taskCode, TaskStatusCode taskStatusCode) {
        return taskRepository.findByAppPackageNameAndTaskOwnerIdAndComnCodeAndTaskStatusCode(appPackageName, taskOwnerId, taskCode, taskStatusCode).isPresent();
    }

    /**
     * 고정 시간 일회성 Task 생성
     * @param appPackageName 앱 패키지 명
     * @param taskOwnerId Task 소유자 ID
     * @param taskCode Task 종류 코드
     * @param fixTime Task 실행 시간
     */
    @Transactional
    public void createFixTimeTask(String appPackageName, String taskOwnerId, String taskCode, LocalTime fixTime) {

        ComnCodeEntity comnCodeEntity = comnCodeRepository.findById(taskCode)
                .orElseThrow(() -> new NotExistsTaskCodeException(taskCode));

        TaskEntity taskEntity = taskRepository.findByAppPackageNameAndTaskOwnerIdAndComnCode(appPackageName, taskOwnerId, taskCode)
                .orElseGet(() -> new TaskEntity(appPackageName, taskOwnerId, comnCodeEntity, TaskStateCode.FIX_TIME, fixTime));

        taskRepository.save(taskEntity);
    }

    /**
     * 고정 시간 반복 Task 생성
     * @param appPackageName 앱 패키지 명
     * @param taskOwnerId Task 소유자 ID
     * @param taskCode Task 종류 코드
     * @param fixTime Task 실행 시간
     */
    @Transactional
    public void createFixTimeCycleTask(String appPackageName, String taskOwnerId, String taskCode, LocalTime fixTime) {

        ComnCodeEntity comnCodeEntity = comnCodeRepository.findById(taskCode)
                .orElseThrow(() -> new NotExistsTaskCodeException(taskCode));

        TaskEntity taskEntity = taskRepository.findByAppPackageNameAndTaskOwnerIdAndComnCode(appPackageName, taskOwnerId, taskCode)
                .orElseGet(() -> new TaskEntity(appPackageName, taskOwnerId, comnCodeEntity, TaskStateCode.FIX_TIME_CYCLE, fixTime));

        taskRepository.save(taskEntity);
    }

    /**
     * Task 생성
     * @param appPackageName 앱 코드
     * @param taskOwnerId Task 생성자 ID
     * @param taskCode Task 종류 코드
     * @param expirationSeconds Task 만료 시간
     */
    @Transactional
    public void createTask(String appPackageName, String taskOwnerId, String taskCode, Long expirationSeconds) {

        ComnCodeEntity comnCodeEntity = comnCodeRepository.findById(taskCode)
                .orElseThrow(() -> new NotExistsTaskCodeException(taskCode));

        TaskEntity taskEntity = taskRepository.findByAppPackageNameAndTaskOwnerIdAndComnCode(appPackageName, taskOwnerId, taskCode)
                .orElseGet(() -> new TaskEntity(appPackageName, taskOwnerId, comnCodeEntity, TaskStateCode.NONE_FIX_TIME, expirationSeconds));

        taskRepository.save(taskEntity);
    }

    /**
     * 반복 Task 생성
     * @param appPackageName 앱 코드
     * @param taskOwnerId Task 생성자 ID
     * @param taskCode Task 종류 코드
     * @param expirationSeconds Task 만료 시간
     */
    @Transactional
    public void createCycleTask(String appPackageName, String taskOwnerId, String taskCode, Long expirationSeconds) {

        ComnCodeEntity comnCodeEntity = comnCodeRepository.findById(taskCode)
                .orElseThrow(() -> new NotExistsTaskCodeException(taskCode));

        TaskEntity taskEntity = taskRepository.findByAppPackageNameAndTaskOwnerIdAndComnCode(appPackageName, taskOwnerId, taskCode)
                .orElseGet(() -> new TaskEntity(appPackageName, taskOwnerId, comnCodeEntity, TaskStateCode.NONE_FIX_TIME_CYCLE, expirationSeconds));

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
     * @param appPackageName 앱 코드
     * @param taskOwnerId Task 생성자 ID
     */
    @Transactional
    public void deleteAllTasks(String appPackageName, String taskOwnerId) {

        lockTaskRepository.deleteAll(lockTaskRepository.findByAppPackageNameAndTaskOwnerId(appPackageName, taskOwnerId));
    }

    /**
     * Task 삭제
     * @param appPackageName 앱 코드
     * @param taskOwnerId Task 생성자 ID
     * @param taskCode Task 종류 코드
     */
    @Transactional
    public void deleteTask(String appPackageName, String taskOwnerId, String taskCode) {

        lockTaskRepository.deleteAll(lockTaskRepository.findByAppPackageNameAndTaskOwnerIdAndComnCode(appPackageName, taskOwnerId, taskCode));
    }

    /**
     * Task 일시 중지
     * @param appPackageName 앱 코드
     * @param taskOwnerId Task 생성자 ID
     * @param taskCode Task 종류 코드
     */
    @Transactional
    public void pauseTask(String appPackageName, String taskOwnerId, String taskCode) {

        lockTaskRepository.findByAppPackageNameAndTaskOwnerIdAndComnCode(appPackageName, taskOwnerId, taskCode)
                .forEach(TaskEntity::pause);
    }

    /**
     * Task 재기동
     * @param appPackageName 앱 코드
     * @param taskOwnerId Task 생성자 ID
     * @param taskCode Task 종류 코드
     */
    @Transactional
    public void resumeTask(String appPackageName, String taskOwnerId, String taskCode) {

        lockTaskRepository.findByAppPackageNameAndTaskOwnerIdAndComnCode(appPackageName, taskOwnerId, taskCode)
                .forEach(TaskEntity::resume);
    }

    /**
     * Task 전체 일시 중지 (App 종료)
     * @param appPackageName 앱 코드
     */
    @Transactional
    public List<TaskVo> appStopPauseAllTask(String appPackageName) {

        List<TaskVo> taskVoList = new ArrayList<>();

        lockTaskRepository.findByAppPackageName(appPackageName).forEach(taskEntity -> {
            taskEntity.appStopPause();
            taskVoList.add(TaskVo.of(taskEntity));
        });

        return taskVoList;
    }

    /**
     * Task 전체 재기동 (App 기동)
     * @param appPackageName 앱 코드
     */
    @Transactional
    public List<TaskVo> appStopResumeAllTask(String appPackageName) {

        List<TaskVo> taskVoList = new ArrayList<>();

        lockTaskRepository.findByAppPackageName(appPackageName).forEach(taskEntity -> {
            taskEntity.appStopResume();
            taskVoList.add(TaskVo.of(taskEntity));
        });

        return taskVoList;
    }
}
