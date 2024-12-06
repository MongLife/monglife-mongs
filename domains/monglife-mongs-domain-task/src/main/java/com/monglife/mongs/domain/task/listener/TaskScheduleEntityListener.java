package com.monglife.mongs.domain.task.listener;

import com.monglife.mongs.domain.task.dto.etc.GetTaskDto;
import com.monglife.mongs.domain.task.dto.event.ExecuteTaskEvent;
import com.monglife.mongs.domain.task.dto.event.RunTaskScheduleEvent;
import com.monglife.mongs.domain.task.dto.event.StopTaskScheduleEvent;
import com.monglife.mongs.domain.task.exception.NotExistsTaskException;
import com.monglife.mongs.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskScheduleEntityListener {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final TaskService taskService;


    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void runTaskScheduleEventListener(RunTaskScheduleEvent event) {

        try {
            GetTaskDto getTaskDto = taskService.getTask(event.getTaskId());

            if (!getTaskDto.getIsCycle()) taskService.deleteTask(getTaskDto.getAppCode(), getTaskDto.getTaskOwnerId(), getTaskDto.getTaskCode());
            else taskService.cycleTask(getTaskDto.getTaskId());

            applicationEventPublisher.publishEvent(ExecuteTaskEvent.builder()
                    .appCode(getTaskDto.getAppCode())
                    .taskOwnerId(getTaskDto.getTaskOwnerId())
                    .taskCode(getTaskDto.getTaskCode())
                    .restExpirationSeconds(getTaskDto.getRestExpirationSeconds())
                    .expirationSeconds(getTaskDto.getExpirationSeconds())
                    .build());

            log.info("[RUN] {} -> {}", getTaskDto.getTaskOwnerId(), getTaskDto.getTaskCode());

        } catch (NotExistsTaskException e) {
            log.info("[ALREADY DEL] {} -> {}", event.getTaskId(), e.getMessage());
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void stopTaskScheduleEventListener(StopTaskScheduleEvent event) {

        GetTaskDto getTaskDto = taskService.getTask(event.getTaskId());

        switch (getTaskDto.getTaskStatusCode()) {
            case DELETE -> {
                taskService.hardDeleteTask(getTaskDto.getTaskId());
                log.info("[DEL] {} -> {}", getTaskDto.getTaskOwnerId(), getTaskDto.getTaskCode());
            }
            case PAUSE, APP_STOP_PROCESSING -> {
                applicationEventPublisher.publishEvent(ExecuteTaskEvent.builder()
                    .appCode(getTaskDto.getAppCode())
                    .taskOwnerId(getTaskDto.getTaskOwnerId())
                    .taskCode(getTaskDto.getTaskCode())
                    .restExpirationSeconds(getTaskDto.getRestExpirationSeconds())
                    .expirationSeconds(getTaskDto.getExpirationSeconds())
                    .build());

                log.info("[STOP] {} -> {}", getTaskDto.getTaskOwnerId(), getTaskDto.getTaskCode());
            }
        }
    }
}
