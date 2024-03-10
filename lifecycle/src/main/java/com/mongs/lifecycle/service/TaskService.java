package com.mongs.lifecycle.service;

import com.mongs.lifecycle.code.TaskStatusCode;
import com.mongs.lifecycle.code.TaskCode;
import com.mongs.lifecycle.entity.TaskEvent;
import com.mongs.lifecycle.exception.EventTaskException;
import com.mongs.lifecycle.exception.LifecycleErrorCode;
import com.mongs.lifecycle.repository.MongRepository;
import com.mongs.lifecycle.repository.TaskEventRepository;
import com.mongs.lifecycle.task.*;
import com.mongs.lifecycle.utils.TaskUtil;
import com.mongs.lifecycle.vo.TaskEventVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskUtil taskUtil;
    private final TaskActiveService taskActiveService;
    private final TaskEventRepository taskEventRepository;
    private final MongRepository mongRepository;

    private final Map<String, BasicTask> taskMap = new ConcurrentHashMap<>();

    /* Task 변경 */
    @Transactional
    public void startTask(Long mongId, TaskCode taskCode) {
        try {
            if (taskEventRepository.findByMongIdAndTaskCodeAndStatusCode(mongId, taskCode, TaskStatusCode.WAIT).isPresent()) {
                throw new EventTaskException(LifecycleErrorCode.EXIST_TASK);
            }

            if (mongRepository.findByIdAndIsActiveTrue(mongId).isEmpty()) {
                throw new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG);
            }

            // TaskEvent 저장
            TaskEvent taskEvent = TaskEvent.of(mongId, taskCode, taskUtil.getExpiration(taskCode));
            taskEventRepository.save(taskEvent);

            BasicTask task = switch (taskCode) {
                case WEIGHT_DOWN -> WeightDownTask.of(this, taskActiveService, TaskEventVo.of(taskEvent));
                case STRENGTH_DOWN -> StrengthDownTask.of(this, taskActiveService, TaskEventVo.of(taskEvent));
                case SATIETY_DOWN -> SatietyDownTask.of(this, taskActiveService, TaskEventVo.of(taskEvent));
                case HEALTHY_DOWN -> HealthyDownTask.of(this, taskActiveService, TaskEventVo.of(taskEvent));
                case SLEEP_DOWN -> SleepDownTask.of(this, taskActiveService, TaskEventVo.of(taskEvent));
                case SLEEP_UP -> SleepUpTask.of(this, taskActiveService, TaskEventVo.of(taskEvent));
                case PAY_POINT_UP -> PayPointUpTask.of(this, taskActiveService, TaskEventVo.of(taskEvent));
                case POOP -> GeneratePoopTask.of(this, taskActiveService, TaskEventVo.of(taskEvent));
                case DEAD_SATIETY, DEAD_HEALTHY, DEAD -> DeadTask.of(this, taskActiveService, TaskEventVo.of(taskEvent));
            };

            if (task == null) {
                throw new EventTaskException(LifecycleErrorCode.GENERATE_TASK_ERROR);
            }

            taskMap.put(taskEvent.getTaskId(), task);
            task.start();
        } catch (EventTaskException e) {
            // log.info("[{}] {} Task 실행 실패 : {}", mongId, taskCode, e.errorCode.getMessage());
        }
    }

    @Transactional
    public void stopTask(Long mongId, TaskCode taskCode) {
        try {
            // 이벤트 조회
            TaskEvent taskEvent = taskEventRepository.findByMongIdAndTaskCodeAndStatusCode(mongId, taskCode, TaskStatusCode.WAIT)
                    .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_TASK));
            // StatusCode 변경
            this.doneTask(taskEvent.getTaskId());
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
            taskEventRepository.findByMongIdAndStatusCode(mongId, TaskStatusCode.WAIT)
                    .forEach(taskEvent -> {
                        // StatusCode 변경
                        this.doneTask(taskEvent.getTaskId());
                        BasicTask task = taskMap.get(taskEvent.getTaskId());
                        task.stop();
                        log.info("[{}] {} Task 삭제 ({})", mongId, taskEvent.getTaskCode(), taskEvent.getTaskId());
                    });
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
        return taskEventRepository.findByMongIdAndTaskCodeAndStatusCodeNot(mongId, taskCode, TaskStatusCode.DONE).isPresent();
    }
}
