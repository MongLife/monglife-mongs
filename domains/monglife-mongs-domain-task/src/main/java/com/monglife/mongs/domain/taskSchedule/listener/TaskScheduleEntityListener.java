package com.monglife.mongs.domain.taskSchedule.listener;

import com.monglife.mongs.domain.task.dto.event.ExecuteCycleTaskEvent;
import com.monglife.mongs.domain.task.dto.event.ExecuteTaskEvent;
import com.monglife.mongs.domain.task.exception.NotExistsTaskException;
import com.monglife.mongs.domain.task.service.TaskService;
import com.monglife.mongs.domain.taskSchedule.dto.event.RunTaskScheduleEvent;
import com.monglife.mongs.domain.taskSchedule.service.TaskScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskScheduleEntityListener {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final TaskService taskService;

    private final TaskScheduleService taskScheduleService;


    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void runTaskScheduleEventListener(RunTaskScheduleEvent event) {

        try {
            if (!event.getIsCycle()) {
                // 일회성 Task 엔티티 삭제
                taskService.deleteTask(event.getAppPackageName(), event.getTaskOwnerId(), event.getTaskCode());
                // 일회성 Task 완료 이벤트 발생
                applicationEventPublisher.publishEvent(ExecuteTaskEvent.builder()
                        .appPackageName(event.getAppPackageName())
                        .taskOwnerId(event.getTaskOwnerId())
                        .taskCode(event.getTaskCode())
                        .expiredAt(event.getExpiredAt())
                        .expirationSeconds(event.getExpirationSeconds())
                        .build());
            } else {
                // 반복 Task 는 @PostUpdate 에서 테스크 스케 줄러 실행 후
                taskService.cycleTask(event.getTaskId());
                // 반복 Task 완료 이벤트 발생
                applicationEventPublisher.publishEvent(ExecuteCycleTaskEvent.builder()
                        .appPackageName(event.getAppPackageName())
                        .taskOwnerId(event.getTaskOwnerId())
                        .taskCode(event.getTaskCode())
                        .expiredAt(event.getExpiredAt())
                        .expirationSeconds(event.getExpirationSeconds())
                        .build());
            }

            log.info("\n[TASK RUN] [{}] {}", event.getTaskOwnerId(), event.getTaskCode());
        } catch (NotExistsTaskException e) {
            log.warn("\n[TASK NOT EXISTS] [{}] {}", event.getTaskId(), e.getMessage());
        } catch (RuntimeException e) {
            taskScheduleService.stopTaskSchedule(event.getTaskId());
            log.error("\n[TASK EXECUTE ERROR] [{}] {}", event.getTaskId(), e.getMessage());
        }
    }
}
