package com.monglife.mongs.app.manager.management.listener;

import com.monglife.mongs.app.manager.global.config.TaskScheduleProperties;
import com.monglife.mongs.domain.mong.dto.etc.DecreaseMongStatusDto;
import com.monglife.mongs.domain.mong.dto.etc.IncreaseMongStatusDto;
import com.monglife.mongs.domain.mong.service.MongService;
import com.monglife.mongs.domain.task.dto.event.TaskRunEvent;
import com.monglife.mongs.domain.task.dto.event.TaskStopEvent;
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
    public void taskRunEventListener(TaskRunEvent event) {

        // 앱 코드 확인
        if (!APP_CODE.equals(event.getAppCode())) {
            return;
        }

        String taskCode = event.getTaskCode();
        Long mongId = Long.parseLong(event.getTaskOwnerId());

        // 테스크 코드 확인
        if (taskCode.equals(taskScheduleProperties.eggEvolution.getCode())) {
            mongService.evolutionReadyMong(mongId);
        } else if (taskCode.equals(taskScheduleProperties.sleep.getCode())) {
            mongService.sleepMong(mongId);
        } else if (taskCode.equals(taskScheduleProperties.wakeup.getCode())) {
            mongService.wakeupMong(mongId);
        } else if (taskCode.equals(taskScheduleProperties.statusIncrease.getCode())) {
            mongService.increaseMongStatus(mongId, IncreaseMongStatusDto.builder()
                    .exp(taskScheduleProperties.statusIncrease.getExp())
                    .weight(taskScheduleProperties.statusIncrease.getWeight())
                    .strengthRatio(taskScheduleProperties.statusIncrease.getStrengthRatio())
                    .satietyRatio(taskScheduleProperties.statusIncrease.getSatietyRatio())
                    .healthyRatio(taskScheduleProperties.statusIncrease.getHealthyRatio())
                    .fatigueRatio(taskScheduleProperties.statusIncrease.getFatigueRatio())
                    .build());
        } else if (taskCode.equals(taskScheduleProperties.statusDecrease.getCode())) {
            mongService.decreaseMongStatus(mongId, DecreaseMongStatusDto.builder()
                    .exp(taskScheduleProperties.statusIncrease.getExp())
                    .weight(taskScheduleProperties.statusIncrease.getWeight())
                    .strengthRatio(taskScheduleProperties.statusIncrease.getStrengthRatio())
                    .satietyRatio(taskScheduleProperties.statusIncrease.getSatietyRatio())
                    .healthyRatio(taskScheduleProperties.statusIncrease.getHealthyRatio())
                    .fatigueRatio(taskScheduleProperties.statusIncrease.getFatigueRatio())
                    .build());
        } else if (taskCode.equals(taskScheduleProperties.poopIncrease.getCode())) {
            mongService.increasePoop(mongId, taskScheduleProperties.poopIncrease.getPoop());
        }
    }

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void taskStopEventListener(TaskStopEvent event) {

        // 앱 코드 확인
        if (!APP_CODE.equals(event.getAppCode())) {
            return;
        }

        String taskCode = event.getTaskCode();
        Long mongId = Long.parseLong(event.getTaskOwnerId());

        Double ratio = event.getRestExpirationSeconds().doubleValue() / event.getExpirationSeconds().doubleValue() * 100D;

        // 테스크 코드 확인
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
                    .exp(taskScheduleProperties.statusIncrease.getExp() * ratio)
                    .weight(taskScheduleProperties.statusIncrease.getWeight() * ratio)
                    .strengthRatio(taskScheduleProperties.statusIncrease.getStrengthRatio() * ratio)
                    .satietyRatio(taskScheduleProperties.statusIncrease.getSatietyRatio() * ratio)
                    .healthyRatio(taskScheduleProperties.statusIncrease.getHealthyRatio() * ratio)
                    .fatigueRatio(taskScheduleProperties.statusIncrease.getFatigueRatio() * ratio)
                    .build());
        } else if (taskCode.equals(taskScheduleProperties.poopIncrease.getCode())) {
            mongService.increasePoop(mongId, (int) (taskScheduleProperties.poopIncrease.getPoop() * ratio));
        }
    }
}
