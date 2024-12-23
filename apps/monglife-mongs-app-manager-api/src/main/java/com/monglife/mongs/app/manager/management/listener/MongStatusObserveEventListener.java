package com.monglife.mongs.app.manager.management.listener;

import com.monglife.mongs.app.manager.global.config.TaskScheduleProperties;
import com.monglife.mongs.app.manager.management.dto.response.MongStatusObserveResponseDto;
import com.monglife.mongs.app.manager.management.enums.ManagementResponse;
import com.monglife.mongs.domain.mong.dto.event.MongObserveStatusEvent;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import com.monglife.mongs.domain.task.service.TaskService;
import com.monglife.mongs.module.mqtt.service.MqttSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MongStatusObserveEventListener {

    @Value("${application.app-package-name}")
    private String APP_PACKAGE_NAME;

    private final TaskScheduleProperties taskScheduleProperties;

    private final MqttSendService mqttSendService;

    private final TaskService taskService;


    /**
     * 몽 지수 변경 이벤트 리스너
     * @param event 몽 지수 변경 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void mongObserveStatusEventListener(MongObserveStatusEvent event) {

        String taskOwnerId = String.valueOf(event.getMongId());

        boolean isDeadStatus = event.getSatietyRatio() <= taskScheduleProperties.dead.getSatietyRatio() || event.getHealthyRatio() <= taskScheduleProperties.dead.getHealthyRatio();

        if (isDeadStatus) {
            if (taskService.isExistsTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.dead.getCode(), TaskStatusCode.PAUSE)) {
                taskService.resumeTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.dead.getCode());
            } else if (!taskService.isExistsTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.dead.getCode(), TaskStatusCode.PROCESSING) && !MongStateCode.DEAD.equals(event.getStateCode())) {
                taskService.createTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.dead.getCode(), taskScheduleProperties.dead.getExpiration());
            }
        } else {
            if (taskService.isExistsTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.dead.getCode(), TaskStatusCode.PROCESSING)) {
                taskService.pauseTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.dead.getCode());
            }
        }

        String topic = String.valueOf(event.getMongId());

        MongStatusObserveResponseDto mongStatusObserveResponseDto = MongStatusObserveResponseDto.builder()
                .mongId(event.getMongId())
                .statusCode(event.getStatusCode())
                .weight(event.getWeight())
                .expRatio(event.getExpRatio())
                .strengthRatio(event.getStrengthRatio())
                .satietyRatio(event.getSatietyRatio())
                .healthyRatio(event.getHealthyRatio())
                .fatigueRatio(event.getFatigueRatio())
                .poopCount(event.getPoopCount())
                .build();

        mqttSendService.sendMessage(topic, ManagementResponse.MANAGER_MANAGEMENT_OBSERVE_MONG_STATUS.toResponseDto(mongStatusObserveResponseDto));
    }
}
