package com.monglife.mongs.app.manager.management.listener;

import com.monglife.mongs.app.manager.global.config.TaskScheduleProperties;
import com.monglife.mongs.domain.mong.dto.etc.DecreaseMongStatusDto;
import com.monglife.mongs.domain.mong.dto.etc.IncreaseMongStatusDto;
import com.monglife.mongs.domain.mong.service.MongService;
import com.monglife.mongs.domain.task.dto.event.ExecuteTaskEvent;
import com.monglife.mongs.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
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
        if (taskCode.equals(taskScheduleProperties.eggEvolution.getCode())) {
            mongService.evolutionReadyMong(mongId);
        } else if (taskCode.equals(taskScheduleProperties.sleep.getCode())) {
            taskService.deleteTask(APP_PACKAGE_NAME, event.getTaskOwnerId(), taskScheduleProperties.statusDecrease.getCode());
            taskService.deleteTask(APP_PACKAGE_NAME, event.getTaskOwnerId(), taskScheduleProperties.poopIncrease.getCode());
            taskService.createCycleTask(APP_PACKAGE_NAME, event.getTaskOwnerId(), taskScheduleProperties.statusIncrease.getCode(), taskScheduleProperties.statusIncrease.getExpiration());
            mongService.sleepMong(mongId);
        } else if (taskCode.equals(taskScheduleProperties.wakeup.getCode())) {
            taskService.createCycleTask(APP_PACKAGE_NAME, event.getTaskOwnerId(), taskScheduleProperties.statusDecrease.getCode(), taskScheduleProperties.statusDecrease.getExpiration());
            taskService.createCycleTask(APP_PACKAGE_NAME, event.getTaskOwnerId(), taskScheduleProperties.poopIncrease.getCode(), taskScheduleProperties.poopIncrease.getExpiration());
            taskService.deleteTask(APP_PACKAGE_NAME, event.getTaskOwnerId(), taskScheduleProperties.statusIncrease.getCode());
            mongService.wakeupMong(mongId);
        } else if (taskCode.equals(taskScheduleProperties.dead.getCode())) {
            mongService.deadMong(mongId);
        } else if (taskCode.equals(taskScheduleProperties.statusIncrease.getCode())) {
            // 지수 증가
            mongService.increaseMongStatus(mongId, IncreaseMongStatusDto.builder()
                    .exp(taskScheduleProperties.statusIncrease.getExp() * ratio)            // TODO: 삭제
                    .weight(taskScheduleProperties.statusIncrease.getWeight() * ratio)      // TODO: 삭제
                    .strengthRatio(taskScheduleProperties.statusIncrease.getStrengthRatio() * ratio)
                    .satietyRatio(taskScheduleProperties.statusIncrease.getSatietyRatio() * ratio)
                    .healthyRatio(taskScheduleProperties.statusIncrease.getHealthyRatio() * ratio)
                    .fatigueRatio(taskScheduleProperties.statusIncrease.getFatigueRatio() * ratio)
                    .poop(taskScheduleProperties.statusIncrease.getPoop())
                    .build());
        } else if (taskCode.equals(taskScheduleProperties.statusDecrease.getCode())) {
            // 지수 감소
            mongService.decreaseMongStatus(mongId, DecreaseMongStatusDto.builder()
                    .exp(taskScheduleProperties.statusDecrease.getExp() * ratio)
                    .weight(taskScheduleProperties.statusDecrease.getWeight() * ratio)
                    .strengthRatio(taskScheduleProperties.statusDecrease.getStrengthRatio() * ratio)
                    .satietyRatio(taskScheduleProperties.statusDecrease.getSatietyRatio() * ratio)
                    .healthyRatio(taskScheduleProperties.statusDecrease.getHealthyRatio() * ratio)
                    .fatigueRatio(taskScheduleProperties.statusDecrease.getFatigueRatio() * ratio)
                    .poop(taskScheduleProperties.statusDecrease.getPoop())
                    .build());
        } else if (taskCode.equals(taskScheduleProperties.poopIncrease.getCode()) && ratio >= 0.5) {
            // 배변 증가
            mongService.increasePoop(mongId, taskScheduleProperties.poopIncrease.getPoop());
        }
    }
}
