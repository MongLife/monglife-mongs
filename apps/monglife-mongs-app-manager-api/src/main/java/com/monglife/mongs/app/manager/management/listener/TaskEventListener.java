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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TaskEventListener {

    @Value("${application.app-package-name}")
    private String APP_PACKAGE_NAME;

    private final TaskScheduleProperties taskScheduleProperties;

    private final MongService mongService;

    private final TaskService taskService;


    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    @Transactional
    public void executeTaskEventListener(ExecuteTaskEvent event) {

        // 앱 코드 확인
        if (!APP_PACKAGE_NAME.equals(event.getAppPackageName())) return;

        String taskCode = event.getTaskCode();
        Long mongId = Long.parseLong(event.getTaskOwnerId());

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
        } else {

            long restExpirationSeconds = Math.max(1, event.getRestExpirationSeconds());
            long expirationSeconds = Math.max(1, event.getExpirationSeconds());

            double ratio = (double) restExpirationSeconds / (double) expirationSeconds;

            if (taskCode.equals(taskScheduleProperties.statusIncrease.getCode())) {
//                int addPoop = ratio >= 0.5 ? taskScheduleProperties.statusIncrease.getPoop() : 0;
                mongService.increaseMongStatus(mongId, IncreaseMongStatusDto.builder()
//                        .exp(taskScheduleProperties.statusIncrease.getExp() * ratio)
                        .exp(0D)
//                        .weight(taskScheduleProperties.statusIncrease.getWeight() * ratio)
                        .weight(0D)
                        .strengthRatio(taskScheduleProperties.statusIncrease.getStrengthRatio() * ratio)
                        .satietyRatio(taskScheduleProperties.statusIncrease.getSatietyRatio() * ratio)
                        .healthyRatio(taskScheduleProperties.statusIncrease.getHealthyRatio() * ratio)
                        .fatigueRatio(taskScheduleProperties.statusIncrease.getFatigueRatio() * ratio)
//                        .poop(addPoop)
                        .poop(0)
                        .build());
            } else if (taskCode.equals(taskScheduleProperties.statusDecrease.getCode())) {
//                int addPoop = ratio >= 0.5 ? taskScheduleProperties.statusDecrease.getPoop() : 0;
                mongService.decreaseMongStatus(mongId, DecreaseMongStatusDto.builder()
//                        .exp(taskScheduleProperties.statusDecrease.getExp() * ratio)
                        .exp(0D)
                        .weight(taskScheduleProperties.statusDecrease.getWeight() * ratio)
                        .strengthRatio(taskScheduleProperties.statusDecrease.getStrengthRatio() * ratio)
                        .satietyRatio(taskScheduleProperties.statusDecrease.getSatietyRatio() * ratio)
                        .healthyRatio(taskScheduleProperties.statusDecrease.getHealthyRatio() * ratio)
                        .fatigueRatio(taskScheduleProperties.statusDecrease.getFatigueRatio() * ratio)
//                        .poop(addPoop)
                        .poop(0)
                        .build());
            } else if (taskCode.equals(taskScheduleProperties.poopIncrease.getCode())) {
                if (ratio >= 0.5) mongService.increasePoop(mongId, taskScheduleProperties.poopIncrease.getPoop());
            }
        }
    }
}
