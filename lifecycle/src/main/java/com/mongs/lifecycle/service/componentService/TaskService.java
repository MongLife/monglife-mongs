package com.mongs.lifecycle.service.componentService;

import com.mongs.core.enums.lifecycle.TaskStatusCode;
import com.mongs.core.enums.lifecycle.TaskCode;
import com.mongs.lifecycle.entity.TaskEvent;
import com.mongs.lifecycle.exception.EventTaskException;
import com.mongs.lifecycle.exception.LifecycleErrorCode;
import com.mongs.lifecycle.repository.TaskEventRepository;
import com.mongs.lifecycle.task.*;
import com.mongs.lifecycle.utils.TaskUtil;
import com.mongs.lifecycle.vo.TaskEventVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskUtil taskUtil;
    private final TaskEventRepository taskEventRepository;

    // Task 전달용
    private final TaskActiveService taskActiveService;
    private final ScheduledExecutorService scheduledExecutorService;

    private final Map<String, BasicTask> taskMap = new ConcurrentHashMap<>();

    private BasicTask getTask(TaskEvent taskEvent, TaskCode taskCode) {
        return switch (taskCode) {
            case EGG -> EggTask.of(this, taskActiveService, scheduledExecutorService, TaskEventVo.of(taskEvent));
            case WEIGHT_DOWN -> WeightDownTask.of(this, taskActiveService, scheduledExecutorService, TaskEventVo.of(taskEvent));
            case STRENGTH_DOWN -> StrengthDownTask.of(this, taskActiveService, scheduledExecutorService, TaskEventVo.of(taskEvent));
            case SATIETY_DOWN -> SatietyDownTask.of(this, taskActiveService, scheduledExecutorService, TaskEventVo.of(taskEvent));
            case HEALTHY_DOWN -> HealthyDownTask.of(this, taskActiveService, scheduledExecutorService, TaskEventVo.of(taskEvent));
            case SLEEP_DOWN -> SleepDownTask.of(this, taskActiveService, scheduledExecutorService, TaskEventVo.of(taskEvent));
            case SLEEP_UP -> SleepUpTask.of(this, taskActiveService, scheduledExecutorService, TaskEventVo.of(taskEvent));
            case PAY_POINT_UP -> PayPointUpTask.of(this, taskActiveService, scheduledExecutorService, TaskEventVo.of(taskEvent));
            case POOP -> GeneratePoopTask.of(this, taskActiveService, scheduledExecutorService, TaskEventVo.of(taskEvent));
            case DEAD_SATIETY, DEAD_HEALTHY -> DeadTask.of(this, taskActiveService, scheduledExecutorService, TaskEventVo.of(taskEvent));
        };
    }

    /* Task 변경 */
    @Transactional
    public void startTask(Long mongId, TaskCode taskCode) {
        try {
            if (taskEventRepository.findByMongIdAndTaskCodeAndStatusCode(mongId, taskCode, TaskStatusCode.WAIT).isPresent()) {
                throw new EventTaskException(LifecycleErrorCode.EXIST_TASK);
            }

            // TaskEvent 저장
            TaskEvent taskEvent = TaskEvent.of(mongId, taskCode, LocalDateTime.now(), taskUtil.getExpiration(taskCode));
            taskEventRepository.save(taskEvent);

            BasicTask task = getTask(taskEvent, taskCode);

            if (task == null) {
                throw new EventTaskException(LifecycleErrorCode.GENERATE_TASK_FAIL);
            }

            log.info("[{}] {} Task 시작 ({})", taskEvent.getMongId(), taskEvent.getTaskCode(), taskEvent.getTaskId());

            taskMap.put(taskEvent.getTaskId(), task);
            task.start();
        } catch (EventTaskException e) {
            // log.info("[{}] {} Task 실행 실패 : {}", mongId, taskCode, e.errorCode.getMessage());
        }
    }
    @Transactional
    public void restartTask(Long mongId, TaskCode taskCode) {
        try {
            TaskEvent taskEvent = taskEventRepository.findByMongIdAndTaskCodeAndStatusCode(mongId, taskCode, TaskStatusCode.PAUSE)
                    .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_TASK));

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime newExpiredAt = now.plusSeconds(taskEvent.getExpiration());
            taskEvent = taskEventRepository.save(taskEvent.toBuilder()
                    .expiredAt(newExpiredAt)
                    .statusCode(TaskStatusCode.WAIT)
                    .build());

            BasicTask task = getTask(taskEvent, taskEvent.getTaskCode());

            if (task == null) {
                throw new EventTaskException(LifecycleErrorCode.GENERATE_TASK_FAIL);
            }

            taskMap.put(taskEvent.getTaskId(), task);
            task.start();

            log.info("[{}] {} Task 재시작 ({}), 남은 시간 : {} 초, 재시작 시간 : {}, 만료 시간 : {}",
                    taskEvent.getMongId(), taskEvent.getTaskCode(), taskEvent.getTaskId(), taskEvent.getExpiration(), now, newExpiredAt);

        } catch (EventTaskException e) {
            // log.info("[doneTask] 진행중이지 않은 Task 변경 [{}]", taskId);
        }
    }
    @Transactional
    public void restartAllTask() {
        try {
            // WAIT 복구
            taskEventRepository.findAllByStatusCode(TaskStatusCode.WAIT)
                    .forEach(taskEvent -> {
                        taskEventRepository.findByTaskIdAndStatusCode(taskEvent.getTaskId(), TaskStatusCode.WAIT)
                                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_TASK));

                        LocalDateTime now = LocalDateTime.now();
                        LocalDateTime newExpiredAt = now.plusSeconds(taskEvent.getExpiration());
                        taskEvent = taskEventRepository.save(taskEvent.toBuilder()
                                .expiredAt(newExpiredAt)
                                .statusCode(TaskStatusCode.WAIT)
                                .build());

                        BasicTask task = getTask(taskEvent, taskEvent.getTaskCode());

                        if (task == null) {
                            throw new EventTaskException(LifecycleErrorCode.GENERATE_TASK_FAIL);
                        }

                        taskMap.put(taskEvent.getTaskId(), task);
                        task.start();

                        log.info("[{}] {} Task 복구 ({}), 남은 시간 : {} 초, 복구 시간 : {}, 만료 시간 : {}",
                                taskEvent.getMongId(), taskEvent.getTaskCode(), taskEvent.getTaskId(), taskEvent.getExpiration(), now, newExpiredAt);

                    });

            // SHUTDOWN_PAUSE 재시작
            taskEventRepository.findAllByStatusCode(TaskStatusCode.SHUTDOWN_PAUSE)
                    .forEach(taskEvent -> {
                        taskEventRepository.findByTaskIdAndStatusCode(taskEvent.getTaskId(), TaskStatusCode.SHUTDOWN_PAUSE)
                                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_TASK));

                        LocalDateTime now = LocalDateTime.now();
                        LocalDateTime newExpiredAt = now.plusSeconds(taskEvent.getExpiration());
                        taskEvent = taskEventRepository.save(taskEvent.toBuilder()
                                .expiredAt(newExpiredAt)
                                .statusCode(TaskStatusCode.WAIT)
                                .build());

                        BasicTask task = getTask(taskEvent, taskEvent.getTaskCode());

                        if (task == null) {
                            throw new EventTaskException(LifecycleErrorCode.GENERATE_TASK_FAIL);
                        }

                        taskMap.put(taskEvent.getTaskId(), task);
                        task.start();

                        log.info("[{}] {} Task 재시작 ({}), 남은 시간 : {} 초, 재시작 시간 : {}, 만료 시간 : {}",
                                taskEvent.getMongId(), taskEvent.getTaskCode(), taskEvent.getTaskId(), taskEvent.getExpiration(), now, newExpiredAt);

                    });
        } catch (EventTaskException e) {
            // log.info("[doneTask] 진행중이지 않은 Task 변경 [{}]", taskId);
        }
    }
    @Transactional
    public void pauseTask(Long mongId, TaskCode taskCode) {
        try {
            // Task 조회
            TaskEvent taskEvent = taskEventRepository.findByMongIdAndTaskCodeAndStatusCode(mongId, taskCode, TaskStatusCode.WAIT)
                    .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_TASK));

            // 일시 중지
            BasicTask task = taskMap.get(taskEvent.getTaskId());
            task.pause(TaskStatusCode.PAUSE);

        } catch (EventTaskException e) {
            // log.info("[doneTask] 진행중이지 않은 Task 변경 [{}]", taskId);
        }
    }
    @Transactional
    public void pauseAllTask() {
        try {
            taskEventRepository.findAllByStatusCode(TaskStatusCode.WAIT)
                    .forEach(taskEvent -> {
                        taskEventRepository.findByTaskIdAndStatusCode(taskEvent.getTaskId(), TaskStatusCode.WAIT)
                                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_TASK));

                        // 일시 중지
                        BasicTask task = taskMap.get(taskEvent.getTaskId());
                        task.pause(TaskStatusCode.SHUTDOWN_PAUSE);
                    });

        } catch (EventTaskException e) {
            // log.info("[doneTask] 진행중이지 않은 Task 변경 [{}]", taskId);
        }
    }
    @Transactional
    public void stopTask(Long mongId, TaskCode taskCode) {
        try {
            // 이벤트 조회
            TaskEvent taskEvent = taskEventRepository.findByMongIdAndTaskCodeAndStatusCode(mongId, taskCode, TaskStatusCode.WAIT)
                    .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_TASK));
            // StatusCode 변경
            BasicTask task = taskMap.get(taskEvent.getTaskId());
            task.stop();

            log.info("[{}] {} Task 삭제 ({})", mongId, taskCode, taskEvent.getTaskId());

        } catch (EventTaskException e) {
            // log.info("[{}] {} Task 진행중이지 않음", mongId, taskCode);
        }
    }
    @Transactional
    public void stopAllTask(Long mongId) {
        try {
            taskEventRepository.findAllByMongIdAndStatusCode(mongId, TaskStatusCode.WAIT)
                    .forEach(taskEvent -> this.stopTask(mongId, taskEvent.getTaskCode()));
        } catch (RuntimeException e) {
            // log.error("[stopAllTask] [{}] 모든 Task 중지 실패", mongId);
        }
    }



    /* TaskStatusCode 변경 */
    @Transactional
    public void doneTask(String taskId) {
        try {
            TaskEvent taskEvent = taskEventRepository.findByTaskIdAndStatusCode(taskId, TaskStatusCode.PROCESS)
                    .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_TASK));

            taskEventRepository.save(taskEvent.toBuilder().statusCode(TaskStatusCode.DONE).build());
            taskMap.remove(taskEvent.getTaskId());
        } catch (EventTaskException e) {
            // log.info("[doneTask] 진행중이지 않은 Task 변경 [{}]", taskId);
        }
    }
    @Transactional
    public void pauseTask(String taskId, TaskStatusCode taskStatusCode) {
        try {
            TaskEvent taskEvent = taskEventRepository.findByTaskIdAndStatusCode(taskId, TaskStatusCode.WAIT)
                    .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_TASK));

            // Expiration 업데이트
            Long newExpiration = Duration.between(LocalDateTime.now(), taskEvent.getExpiredAt()).getSeconds();
            taskEventRepository.save(taskEvent.toBuilder()
                    .expiration(newExpiration)
                    .statusCode(taskStatusCode).build());
            taskMap.remove(taskEvent.getTaskId());

            log.info("[{}] {} Task 일시중지 ({}), 남은 시간 : {} 초", taskEvent.getMongId(), taskEvent.getTaskCode(), taskEvent.getTaskId(), newExpiration);

        } catch (EventTaskException e) {
            // log.info("[doneTask] 진행중이지 않은 Task 변경 [{}]", taskId);
        }
    }
    @Transactional
    public void processTask(String taskId) {
        try {
            TaskEvent taskEvent = taskEventRepository.findByTaskIdAndStatusCode(taskId, TaskStatusCode.WAIT)
                    .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_TASK));

            taskEventRepository.save(taskEvent.toBuilder().statusCode(TaskStatusCode.PROCESS).build());
        } catch (EventTaskException e) {
            // log.info("[processTask] 진행중이지 않은 Task 변경 [{}]", taskId);
        }
    }
    public Boolean checkTaskActive(Long mongId, TaskCode taskCode) {
        return taskEventRepository.findByMongIdAndTaskCodeAndStatusCodeIn(
                mongId, taskCode, List.of(TaskStatusCode.WAIT, TaskStatusCode.PROCESS, TaskStatusCode.PAUSE)).isPresent();
    }
}
