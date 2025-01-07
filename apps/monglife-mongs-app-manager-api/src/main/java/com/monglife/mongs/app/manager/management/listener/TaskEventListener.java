package com.monglife.mongs.app.manager.management.listener;

import com.monglife.mongs.app.manager.global.config.TaskScheduleProperties;
import com.monglife.mongs.domain.mong.service.MongService;
import com.monglife.mongs.domain.task.dto.event.ExecuteTaskEvent;
import com.monglife.mongs.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TaskEventListener {

    @Value("${application.app-package-name}")
    private String APP_PACKAGE_NAME;

    private final TaskScheduleProperties taskScheduleProperties;

    private final MongService mongService;

    private final TaskService taskService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void executeTaskEventListener(ExecuteTaskEvent event) {

        // 앱 코드 확인
        if (!APP_PACKAGE_NAME.equals(event.getAppPackageName())) return;

        String taskCode = event.getTaskCode();
        Long mongId = Long.parseLong(event.getTaskOwnerId());

        long restExpirationSeconds = Math.max(0, Duration.between(LocalDateTime.now(), event.getExpiredAt()).toSeconds());
        long expirationSeconds = Math.max(1, event.getExpirationSeconds());

        double ratio = restExpirationSeconds == 0 ? 1 : (double) restExpirationSeconds / (double) expirationSeconds;

        // Task 코드 확인
        if (taskCode.equals(taskScheduleProperties.getEggEvolutionCode())) {
            mongService.evolutionReadyMong(mongId);
        } else if (taskCode.equals(taskScheduleProperties.getSleepCode())) {
            taskService.deleteTask(APP_PACKAGE_NAME, event.getTaskOwnerId(), taskScheduleProperties.getStatusDecreaseCode());
            taskService.deleteTask(APP_PACKAGE_NAME, event.getTaskOwnerId(), taskScheduleProperties.getPoopIncreaseCode());
            taskService.createCycleTask(APP_PACKAGE_NAME, event.getTaskOwnerId(), taskScheduleProperties.getStatusIncreaseCode(), taskScheduleProperties.getStatusIncreaseExpiration());
            mongService.sleepMong(mongId);
        } else if (taskCode.equals(taskScheduleProperties.getWakeupCode())) {
            taskService.createCycleTask(APP_PACKAGE_NAME, event.getTaskOwnerId(), taskScheduleProperties.getStatusDecreaseCode(), taskScheduleProperties.getStatusDecreaseExpiration());
            taskService.createCycleTask(APP_PACKAGE_NAME, event.getTaskOwnerId(), taskScheduleProperties.getPoopIncreaseCode(), taskScheduleProperties.getPoopIncreaseExpiration());
            taskService.deleteTask(APP_PACKAGE_NAME, event.getTaskOwnerId(), taskScheduleProperties.getStatusIncreaseCode());
            mongService.wakeupMong(mongId);
        } else if (taskCode.equals(taskScheduleProperties.getDeadCode())) {
            mongService.deadMong(mongId);
        } else if (taskCode.equals(taskScheduleProperties.getStatusIncreaseCode())) {
            // 지수 증가
            mongService.increaseMongStatus(mongId, taskScheduleProperties.getIncreaseMongStatusDto(ratio));
        } else if (taskCode.equals(taskScheduleProperties.getStatusDecreaseCode())) {
            // 지수 감소
            mongService.decreaseMongStatus(mongId, taskScheduleProperties.getDecreaseMongStatusDto(ratio));
        } else if (taskCode.equals(taskScheduleProperties.getPoopIncreaseCode()) && ratio >= 0.5) {
            // 배변 증가
            mongService.increasePoop(mongId, taskScheduleProperties.getIncreasePoopCount(ratio));
        }
    }
}
