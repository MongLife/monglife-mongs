package com.monglife.mongs.domain.global.schedule;

import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import com.monglife.mongs.domain.task.repository.TaskRepository;
import com.monglife.mongs.domain.taskSchedule.repository.TaskScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetryTaskSchedule {

    private final TaskRepository taskRepository;

    private final TaskScheduleRepository taskScheduleRepository;

    /* 스케줄러 목록 로깅
    @Scheduled(fixedDelay = 1000)
    @Transactional(readOnly = true)
    public void scheduleTask() {
        StringBuffer sb = new StringBuffer();
        taskScheduleRepository.findAll().forEach(taskScheduleEntity ->
                sb.append("\n").append(taskScheduleEntity.getTaskOwnerId()).append(" : ").append(taskScheduleEntity.getTaskCode()).append("[").append(taskScheduleEntity.getExpiredAt()).append("]"));

        if (!sb.toString().trim().isEmpty()) log.info(sb.toString());
    }
     */

    /**
     * 10 분 간격으로 누락 Task 찾아서 재등록
     */
    @Scheduled(initialDelay = 5000, fixedRate = 5 * 60 * 1000)
    @Transactional
    public void retryTasks() {
        try {
            Set<Long> taskIds = taskScheduleRepository.findTaskIdAll();

            taskRepository.findByTaskIdNotInAndTaskStatusCode(taskIds, TaskStatusCode.PROCESSING)
                    .forEach(taskEntity -> {
                        taskEntity.retry();

                        log.info("[{}] TASK RETRY : {} - {}", taskEntity.getTaskId(), taskEntity.getTaskOwnerId(), taskEntity.getComn().getCode());
                    });

        } catch (RuntimeException e) {
            log.error("[RETRY FAIL] {}", e.getMessage());
        }
    }
}
