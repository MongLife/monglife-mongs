package com.mongs.play.domain.task.service;

import com.mongs.play.core.error.domain.TaskErrorCode;
import com.mongs.play.core.exception.common.AlreadyExistException;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.task.entity.TaskEvent;
import com.mongs.play.domain.task.enums.TaskCode;
import com.mongs.play.domain.task.enums.TaskStatus;
import com.mongs.play.domain.task.enums.TaskUtil;
import com.mongs.play.domain.task.repository.TaskEventRepository;
import com.mongs.play.domain.task.entity.Task;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final ScheduledExecutorService scheduledExecutorService;
    private final ApplicationEventPublisher publisher;

    private final Map<String, Task> activeTaskMap = new ConcurrentHashMap<>();
    private final TaskEventRepository taskEventRepository;

    private Task getTask(String taskId) throws NotFoundException {
        Task task = activeTaskMap.get(taskId);
        if (task == null) {
            throw new NotFoundException(TaskErrorCode.NOT_FOUND_TASK);
        }
        return task;
    }

    @Transactional(transactionManager = "taskTransactionManager")
    public void startTask(Long mongId, TaskCode taskCode) {
        try {
            Optional<TaskEvent> taskEvent =
                    taskEventRepository.findByMongIdAndTaskCodeAndTaskStatus(mongId, taskCode, TaskStatus.WAIT);

            if (taskEvent.map(event -> activeTaskMap.get(event.getTaskId()) != null).orElse(false)) {
                throw new AlreadyExistException(TaskErrorCode.ALREADY_EXIST_TASK);
            }

            Long expiration = TaskUtil.getExpiration(taskCode);
            TaskEvent newTaskEvent = taskEventRepository.save(TaskEvent.of(mongId, taskCode, expiration));
            Task task = Task.builder()
                    .taskId(newTaskEvent.getTaskId())
                    .taskCode(newTaskEvent.getTaskCode())
                    .mongId(newTaskEvent.getMongId())
                    .expiration(newTaskEvent.getExpiration())
                    .expiredAt(newTaskEvent.getExpiredAt())
                    .createdAt(newTaskEvent.getCreatedAt())
                    .publisher(publisher)
                    .executor(scheduledExecutorService)
                    .build();
            activeTaskMap.put(newTaskEvent.getTaskId(), task);
            task.start();
        } catch (AlreadyExistException e) {
//            log.warn("[startTask] already exist task, mongId: {}, taskCode: {}", mongId, taskCode);
        }
    }

    @Transactional(transactionManager = "taskTransactionManager")
    public void pauseTask(Long mongId, TaskCode taskCode) {
        try {
            TaskEvent taskEvent = taskEventRepository.findByMongIdAndTaskCodeAndTaskStatusWithLock(mongId, taskCode, TaskStatus.WAIT)
                    .orElseThrow(() -> new NotFoundException(TaskErrorCode.NOT_FOUND_TASK_EVENT));

            Task task = getTask(taskEvent.getTaskId());
            long newExpiration = Duration.between(LocalDateTime.now(), taskEvent.getExpiredAt()).getSeconds();
            taskEvent = taskEventRepository.save(taskEvent.toBuilder()
                    .taskStatus(TaskStatus.PAUSE)
                    .expiration(newExpiration == 0 ? TaskUtil.getExpiration(taskEvent.getTaskCode()) : newExpiration)
                    .expiredAt(null)
                    .build());
            activeTaskMap.remove(taskEvent.getTaskId());
            task.pause();
        } catch (NotFoundException e) {
//            log.warn("[pauseTask] not found task, mongId: {}, taskCode: {}", mongId, taskCode);
        }
    }

    @Transactional(transactionManager = "taskTransactionManager")
    public void pauseSystemTask() {
        List<TaskEvent> taskEventList = taskEventRepository.findByTaskStatusWithLock(TaskStatus.WAIT);

        taskEventList.forEach(taskEvent -> {
            try {
                Task task = getTask(taskEvent.getTaskId());
                long newExpiration = Duration.between(LocalDateTime.now(), taskEvent.getExpiredAt()).getSeconds();
                taskEvent = taskEventRepository.save(taskEvent.toBuilder()
                        .taskStatus(TaskStatus.PAUSE_SYSTEM)
                        .expiration(newExpiration <= 0 ? TaskUtil.getExpiration(taskEvent.getTaskCode()) : newExpiration)
                        .expiredAt(null)
                        .build());
                activeTaskMap.remove(taskEvent.getTaskId());
                task.pause();

                log.info("[pauseSystem] {} : {} : {} : {}s", taskEvent.getTaskId(), taskEvent.getMongId(), taskEvent.getTaskCode(), taskEvent.getExpiration());
            } catch (NotFoundException e) {
//                log.warn("[pauseSystemTask] not found task, mongId: {}, taskCode: {}", taskEvent.getMongId(), taskEvent.getTaskCode());
            }
        });
    }

    @Transactional(transactionManager = "taskTransactionManager")
    public void resumeTask(Long mongId, TaskCode taskCode) {
        try {
            TaskEvent taskEvent = taskEventRepository.findByMongIdAndTaskCodeAndTaskStatus(mongId, taskCode, TaskStatus.PAUSE)
                    .orElseThrow(() -> new NotFoundException(TaskErrorCode.NOT_FOUND_TASK_EVENT));

            taskEvent = taskEventRepository.save(taskEvent.toBuilder()
                    .taskStatus(TaskStatus.WAIT)
                    .expiredAt(LocalDateTime.now().plusSeconds(taskEvent.getExpiration()))
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
            activeTaskMap.put(taskEvent.getTaskId(), task);
            task.start();
        } catch (NotFoundException e) {
//            log.warn("[resumeTask] not found task, mongId: {}, taskCode: {}", mongId, taskCode);
        }
    }

    @Transactional(transactionManager = "taskTransactionManager")
    public void resumeSystemTask() {
        List<TaskEvent> taskEventList = taskEventRepository.findByTaskStatusWithLock(TaskStatus.PAUSE_SYSTEM);

        taskEventList.forEach(taskEvent -> {
            taskEvent = taskEventRepository.save(taskEvent.toBuilder()
                    .taskStatus(TaskStatus.WAIT)
                    .expiredAt(LocalDateTime.now().plusSeconds(taskEvent.getExpiration()))
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

            activeTaskMap.put(taskEvent.getTaskId(), task);
            task.start();

            log.info("[resumeSystem] {} : {} : {} : {}s : {}", taskEvent.getTaskId(), taskEvent.getMongId(), taskEvent.getTaskCode(), taskEvent.getExpiration(), taskEvent.getExpiredAt());
        });
    }

    @Transactional(transactionManager = "taskTransactionManager")
    public void stopTask(Long mongId, TaskCode taskCode) {
        try {
            TaskEvent taskEvent = taskEventRepository.findByMongIdAndTaskCodeAndTaskStatusWithLock(mongId, taskCode, TaskStatus.WAIT)
                    .orElseThrow(() -> new NotFoundException(TaskErrorCode.NOT_FOUND_TASK_EVENT));

            Task task = getTask(taskEvent.getTaskId());
            taskEventRepository.deleteById(taskEvent.getTaskId());
            activeTaskMap.remove(taskEvent.getTaskId());
            task.stop();
        } catch (NotFoundException e) {
//            log.warn("[stopTask] not found task, mongId: {}, taskCode: {}", mongId, taskCode);
        }
    }

    @Transactional(transactionManager = "taskTransactionManager")
    public void forceStopAllTask(Long mongId) {
        List<TaskEvent> taskEventList = taskEventRepository.findByMongIdWithLock(mongId);

        taskEventList.forEach(taskEvent -> {
            try {
                Task task = getTask(taskEvent.getTaskId());
                taskEventRepository.deleteById(taskEvent.getTaskId());
                activeTaskMap.remove(taskEvent.getTaskId());
                task.forceStop();
            } catch (NotFoundException e) {
//                log.warn("[forceStopAllTask] not found task, mongId: {}, taskCode: {}", taskEvent.getMongId(), taskEvent.getTaskCode());
            }
        });
    }

    @Transactional(transactionManager = "taskTransactionManager")
    public void doneTask(String taskId) {
        try {
            taskEventRepository.deleteById(taskId);
            activeTaskMap.remove(taskId);
        } catch (NotFoundException e) {
//            log.warn("[doneTask] not found task, taskId: {}", taskId);
        }
    }
}
