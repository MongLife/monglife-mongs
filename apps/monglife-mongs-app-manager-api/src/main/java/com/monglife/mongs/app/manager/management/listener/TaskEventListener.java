package com.monglife.mongs.app.manager.management.listener;

import com.monglife.mongs.app.manager.global.config.TaskScheduleProperties;
import com.monglife.mongs.domain.mong.dto.etc.DecreaseMongStatusDto;
import com.monglife.mongs.domain.mong.dto.etc.IncreaseMongStatusDto;
import com.monglife.mongs.domain.mong.service.MongService;
import com.monglife.mongs.domain.task.dto.event.AppStopEvent;
import com.monglife.mongs.domain.task.dto.event.ExecuteTaskEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TaskEventListener {

    @Value("${application.app-code}")
    private String APP_CODE;

    private final TaskScheduleProperties taskScheduleProperties;

    private final MongService mongService;

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void executeTaskEventListener(ExecuteTaskEvent event) {

        // 앱 코드 확인
        if (!APP_CODE.equals(event.getAppCode())) return;

        String taskCode = event.getTaskCode();
        Long mongId = Long.parseLong(event.getTaskOwnerId());

        // Task 코드 확인
        if (taskCode.equals(taskScheduleProperties.eggEvolution.getCode())) {
            mongService.evolutionReadyMong(mongId);
        } else if (taskCode.equals(taskScheduleProperties.sleep.getCode())) {
            mongService.sleepMong(mongId);
        } else if (taskCode.equals(taskScheduleProperties.wakeup.getCode())) {
            mongService.wakeupMong(mongId);
        } else if (taskCode.equals(taskScheduleProperties.dead.getCode())) {
            mongService.deadMong(mongId);
        } else {

            long restExpirationSeconds = Math.max(1, event.getRestExpirationSeconds());
            long expirationSeconds = Math.max(1, event.getExpirationSeconds());

            double ratio = (double) restExpirationSeconds / (double) expirationSeconds;

            if (taskCode.equals(taskScheduleProperties.statusIncrease.getCode())) {
                mongService.increaseMongStatus(mongId, IncreaseMongStatusDto.builder()
                        .exp(taskScheduleProperties.statusIncrease.getExp() * ratio)
                        .weight(taskScheduleProperties.statusIncrease.getWeight() * ratio)
                        .strengthRatio(taskScheduleProperties.statusIncrease.getStrengthRatio() * ratio)
                        .satietyRatio(taskScheduleProperties.statusIncrease.getSatietyRatio() * ratio)
                        .healthyRatio(taskScheduleProperties.statusIncrease.getHealthyRatio() * ratio)
                        .fatigueRatio(taskScheduleProperties.statusIncrease.getFatigueRatio() * ratio)
                        .build());
            } else if (taskCode.equals(taskScheduleProperties.statusDecrease.getCode())) {
                mongService.decreaseMongStatus(mongId, DecreaseMongStatusDto.builder()
                        .exp(taskScheduleProperties.statusDecrease.getExp() * ratio)
                        .weight(taskScheduleProperties.statusDecrease.getWeight() * ratio)
                        .strengthRatio(taskScheduleProperties.statusDecrease.getStrengthRatio() * ratio)
                        .satietyRatio(taskScheduleProperties.statusDecrease.getSatietyRatio() * ratio)
                        .healthyRatio(taskScheduleProperties.statusDecrease.getHealthyRatio() * ratio)
                        .fatigueRatio(taskScheduleProperties.statusDecrease.getFatigueRatio() * ratio)
                        .build());
            } else if (taskCode.equals(taskScheduleProperties.poopIncrease.getCode())) {
                mongService.increasePoop(mongId, (int) (taskScheduleProperties.poopIncrease.getPoop() * ratio));
            }
        }
    }


    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void appStopEventListener(AppStopEvent event) {

        // 앱 코드 확인
        if (!APP_CODE.equals(event.getAppCode())) return;

        String taskCode = event.getTaskCode();
        Long mongId = Long.parseLong(event.getTaskOwnerId());

        long restExpirationSeconds = Math.max(1, event.getRestExpirationSeconds());
        long expirationSeconds = Math.max(1, event.getExpirationSeconds());

        double ratio = (double) restExpirationSeconds / (double) expirationSeconds;

        // Task 코드 확인
        if (taskCode.equals(taskScheduleProperties.statusIncrease.getCode())) {
            mongService.increaseMongStatus(mongId, IncreaseMongStatusDto.builder()
                    .exp(taskScheduleProperties.statusIncrease.getExp() * ratio)
                    .weight(taskScheduleProperties.statusIncrease.getWeight() * ratio)
                    .strengthRatio(taskScheduleProperties.statusIncrease.getStrengthRatio() * ratio)
                    .satietyRatio(taskScheduleProperties.statusIncrease.getSatietyRatio() * ratio)
                    .healthyRatio(taskScheduleProperties.statusIncrease.getHealthyRatio() * ratio)
                    .fatigueRatio(taskScheduleProperties.statusIncrease.getFatigueRatio() * ratio)
                    .build());
        } else if (taskCode.equals(taskScheduleProperties.statusDecrease.getCode())) {
            mongService.decreaseMongStatus(mongId, DecreaseMongStatusDto.builder()
                    .exp(taskScheduleProperties.statusDecrease.getExp() * ratio)
                    .weight(taskScheduleProperties.statusDecrease.getWeight() * ratio)
                    .strengthRatio(taskScheduleProperties.statusDecrease.getStrengthRatio() * ratio)
                    .satietyRatio(taskScheduleProperties.statusDecrease.getSatietyRatio() * ratio)
                    .healthyRatio(taskScheduleProperties.statusDecrease.getHealthyRatio() * ratio)
                    .fatigueRatio(taskScheduleProperties.statusDecrease.getFatigueRatio() * ratio)
                    .build());
        } else if (taskCode.equals(taskScheduleProperties.poopIncrease.getCode())) {
            mongService.increasePoop(mongId, (int) (taskScheduleProperties.poopIncrease.getPoop() * ratio));
        }
    }
}
