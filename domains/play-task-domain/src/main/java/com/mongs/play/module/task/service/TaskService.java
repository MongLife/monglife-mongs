package com.mongs.play.module.task.service;

import com.mongs.play.core.error.domain.TaskErrorCode;
import com.mongs.play.core.exception.common.AlreadyExistException;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.module.task.entity.TaskEvent;
import com.mongs.play.module.task.enums.TaskCode;
import com.mongs.play.module.task.enums.TaskStatus;
import com.mongs.play.module.task.enums.TaskUtil;
import com.mongs.play.module.task.repository.TaskEventRepository;
import com.mongs.play.module.task.entity.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final ScheduledExecutorService scheduledExecutorService;
    private final ApplicationEventPublisher publisher;

    private final Map<String, Task> activeTaskMap = new ConcurrentHashMap<>();
    private final TaskEventRepository taskEventRepository;

    private List<TaskEvent> getTaskEventByTaskStatus(TaskStatus taskStatus) {
        return taskEventRepository.findByTaskStatus(taskStatus);
    }

    private TaskEvent getTaskEventByTaskId(String taskId) {
        return taskEventRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException(TaskErrorCode.NOT_FOUND_TASK_EVENT));
    }

    private List<TaskEvent> getTaskEventIsNotDone(Long mongId) {
        List<TaskEvent> taskEventList = new ArrayList<>();
        Arrays.stream(TaskStatus.values())
                .filter(taskStatus -> taskStatus != TaskStatus.DONE)
                .forEach(taskStatus -> taskEventList.addAll(taskEventRepository.findByMongIdAndTaskStatus(mongId, taskStatus)));
        return taskEventList;
    }

    private TaskEvent getTaskEvent(Long mongId, TaskCode taskCode, TaskStatus taskStatus) throws NotFoundException {
        return taskEventRepository.findByMongIdAndTaskCodeAndTaskStatus(mongId, taskCode, taskStatus)
                .orElseThrow(() -> new NotFoundException(TaskErrorCode.NOT_FOUND_TASK_EVENT));
    }

    private Boolean validationAddTask(Long mongId, TaskCode taskCode) {

        AtomicBoolean isAddPossible = new AtomicBoolean(true);

        Arrays.stream(TaskStatus.values())
                .filter(taskStatus -> taskStatus != TaskStatus.DONE)
                .forEach(taskStatus -> {
                    Optional<TaskEvent> taskEvent =
                            taskEventRepository.findByMongIdAndTaskCodeAndTaskStatus(mongId, taskCode, taskStatus);
                    boolean isExist = taskEvent.map(event -> activeTaskMap.get(event.getTaskId()) == null).orElse(true);
                    isAddPossible.set(isAddPossible.get() && isExist);
                });

        return isAddPossible.get();
    }

    private Task getTask(String taskId) throws NotFoundException {
        Task task = activeTaskMap.get(taskId);
        if (task == null) {
            throw new NotFoundException(TaskErrorCode.NOT_FOUND_TASK);
        }
        return task;
    }

    public void startTask(Long mongId, TaskCode taskCode) {
        try {
            // 동일한 테스크가 존재하면 만들 수 없음
            if (!validationAddTask(mongId, taskCode)) {
                throw new AlreadyExistException(TaskErrorCode.ALREADY_EXIST_TASK);
            }

            Long expiration = TaskUtil.getExpiration(taskCode);

            // 테스크 이벤트 정보 저장 (WAIT)
            TaskEvent taskEvent = taskEventRepository.save(TaskEvent.of(mongId, taskCode, expiration));
            // 테스크 시작
            Task task = Task.builder()
                    .taskId(taskEvent.getTaskId())
                    .taskCode(taskEvent.getTaskCode())
                    .mongId(taskEvent.getMongId())
                    .expiration(taskEvent.getExpiration())
                    .expiredAt(taskEvent.getExpiredAt())
                    .createdAt(taskEvent.getCreatedAt())
                    .publisher(publisher)
                    .executor(scheduledExecutorService)
                    .build();
            task.start();
            // 테스크 생성 후 저장
            activeTaskMap.put(taskEvent.getTaskId(), task);

        } catch (AlreadyExistException e) {
            log.info("[startTask] already exist task, mongId: {}, taskCode: {}", mongId, taskCode);
        }
    }

    public void pauseTask(Long mongId, TaskCode taskCode) {

        try {
            TaskEvent taskEvent = getTaskEvent(mongId, taskCode, TaskStatus.WAIT);

            Task task = getTask(taskEvent.getTaskId());

            // 테스크 이벤트 정보 (PAUSE) 로 변경
            Long newExpiration = Duration.between(LocalDateTime.now(), taskEvent.getExpiredAt()).getSeconds();
            taskEvent = taskEventRepository.save(taskEvent.toBuilder()
                    .taskStatus(TaskStatus.PAUSE)
                    .expiration(newExpiration)
                    .expiredAt(null)
                    .build());
            // 테스크 일시 중지
            task.pause();
            // 테스크 삭제
            activeTaskMap.remove(taskEvent.getTaskId());

        } catch (NotFoundException e) {
            log.info("[pauseTask] not found task, mongId: {}, taskCode: {}", mongId, taskCode);
        }
    }

    public void pauseSystemTask() {

        List<TaskEvent> taskEventList = getTaskEventByTaskStatus(TaskStatus.WAIT);

        taskEventList.forEach(taskEvent -> {
            try {
                Task task = getTask(taskEvent.getTaskId());

                Long newExpiration = Duration.between(LocalDateTime.now(), taskEvent.getExpiredAt()).getSeconds();
                taskEvent = taskEventRepository.save(taskEvent.toBuilder()
                        .taskStatus(TaskStatus.PAUSE_SYSTEM)
                        .expiration(newExpiration)
                        .expiredAt(null)
                        .ttl(0L)
                        .build());
                task.pause();
                activeTaskMap.remove(taskEvent.getTaskId());
            } catch (NotFoundException e) {
                log.info("[pauseSystemTask] not found task, mongId: {}, taskCode: {}", taskEvent.getMongId(), taskEvent.getTaskCode());
            }
        });
    }

    public void resumeTask(Long mongId, TaskCode taskCode) {

        try {
            TaskEvent taskEvent = getTaskEvent(mongId, taskCode, TaskStatus.PAUSE);

            // 테스크 이벤트 정보 (WAIT) 로 변경
            taskEvent = taskEventRepository.save(taskEvent.toBuilder()
                    .taskStatus(TaskStatus.WAIT)
                    .build());

            // 테스크 시작
            Task task = Task.builder()
                    .taskId(taskEvent.getTaskId())
                    .taskCode(taskEvent.getTaskCode())
                    .mongId(taskEvent.getMongId())
                    .expiration(taskEvent.getExpiration())
                    .expiredAt(taskEvent.getExpiredAt())
                    .createdAt(taskEvent.getCreatedAt())
                    .publisher(publisher)
                    .executor(scheduledExecutorService)
                    .build();
            task.start();
            // 테스크 생성 후 저장
            activeTaskMap.put(taskEvent.getTaskId(), task);

        } catch (NotFoundException e) {
            log.info("[resumeTask] not found task, mongId: {}, taskCode: {}", mongId, taskCode);
        }
    }

    public void resumeSystemTask() {

        List<TaskEvent> taskEventList = getTaskEventByTaskStatus(TaskStatus.PAUSE_SYSTEM);

        taskEventList.forEach(taskEvent -> {
            taskEvent = taskEventRepository.save(taskEvent.toBuilder()
                    .taskStatus(TaskStatus.WAIT)
                    .ttl(taskEvent.getExpiration() + 10)
                    .build());

            Task task = Task.builder()
                    .taskId(taskEvent.getTaskId())
                    .taskCode(taskEvent.getTaskCode())
                    .mongId(taskEvent.getMongId())
                    .expiration(taskEvent.getExpiration())
                    .expiredAt(taskEvent.getExpiredAt())
                    .createdAt(taskEvent.getCreatedAt())
                    .publisher(publisher)
                    .executor(scheduledExecutorService)
                    .build();
            task.start();

            activeTaskMap.put(taskEvent.getTaskId(), task);
        });
    }

    public void stopTask(Long mongId, TaskCode taskCode) {

        try {
            TaskEvent taskEvent = getTaskEvent(mongId, taskCode, TaskStatus.WAIT);

            Task task = getTask(taskEvent.getTaskId());

            // 테스크 이벤트 정보 (DONE) 로 변경
            taskEvent = taskEventRepository.save(taskEvent.toBuilder()
                    .expiration(0L)
                    .expiredAt(null)
                    .taskStatus(TaskStatus.DONE)
                    .build());

            // 테스크 중지
            task.stop();
            // 테스크 삭제
            activeTaskMap.remove(taskEvent.getTaskId());

        } catch (NotFoundException e) {
            log.info("[stopTask] not found task, mongId: {}, taskCode: {}", mongId, taskCode);
        }
    }

    public void forceStopAllTask(Long mongId) {

        List<TaskEvent> taskEventList = getTaskEventIsNotDone(mongId);

        taskEventList.forEach(taskEvent -> {
            try {
                Task task = getTask(taskEvent.getTaskId());

                // 테스크 이벤트 정보 (DONE) 로 변경
                taskEvent = taskEventRepository.save(taskEvent.toBuilder()
                        .expiration(0L)
                        .expiredAt(null)
                        .taskStatus(TaskStatus.DONE)
                        .build());
                // 테스크 강제 중지
                task.forceStop();
                // 테스크 삭제
                activeTaskMap.remove(taskEvent.getTaskId());
            } catch (NotFoundException e) {
                log.info("[forceStopAllTask] not found task, mongId: {}, taskCode: {}", taskEvent.getMongId(), taskEvent.getTaskCode());
            }
        });
    }

    public void doneTask(String taskId) throws NotFoundException {

        try {
            TaskEvent taskEvent = getTaskEventByTaskId(taskId);

            // 테스크 이벤트 정보 (DONE) 로 변경
            taskEvent = taskEventRepository.save(taskEvent.toBuilder()
                    .expiration(0L)
                    .expiredAt(null)
                    .taskStatus(TaskStatus.DONE)
                    .build());

            // 테스크 삭제
            activeTaskMap.remove(taskEvent.getTaskId());

        } catch (NotFoundException e) {
            log.info("[doneTask] not found task, taskId: {}", taskId);
        }
    }
}
