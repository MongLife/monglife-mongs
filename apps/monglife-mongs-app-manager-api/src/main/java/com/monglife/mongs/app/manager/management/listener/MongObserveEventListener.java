package com.monglife.mongs.app.manager.management.listener;

import com.monglife.mongs.app.manager.global.config.TaskScheduleProperties;
import com.monglife.mongs.app.manager.management.dto.response.GetMongResponseDto;
import com.monglife.mongs.app.manager.management.enums.ManagementResponse;
import com.monglife.mongs.domain.mong.dto.event.MongObserveEvent;
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
public class MongObserveEventListener {

    @Value("${application.app-package-name}")
    private String APP_PACKAGE_NAME;

    private final TaskScheduleProperties taskScheduleProperties;

    private final MqttSendService mqttSendService;

    private final TaskService taskService;

    /**
     * 몽 변경 이벤트 리스너
     * @param event 몽 변경 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void mongObserveEventListener(MongObserveEvent event) {

        String topic = "management/" + event.getMongId();
        String taskOwnerId = String.valueOf(event.getMongId());

        // STATUS
        if (MongStateCode.DEAD.equals(event.getStateCode()) || MongStateCode.GRADUATE_READY.equals(event.getStateCode())) {
            taskService.deleteAllTasks(APP_PACKAGE_NAME, taskOwnerId);
        }

        // STATE
        boolean isDeadStatus = event.getSatietyRatio() <= taskScheduleProperties.getDeadSatietyRatio() || event.getHealthyRatio() <= taskScheduleProperties.getDeadHealthyRatio();

        if (isDeadStatus) {
            if (taskService.isExistsTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.getDeadCode(), TaskStatusCode.PAUSE)) {
                taskService.resumeTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.getDeadCode());
            } else if (!taskService.isExistsTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.getDeadCode(), TaskStatusCode.PROCESSING) && !MongStateCode.DEAD.equals(event.getStateCode())) {
                taskService.createTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.getDeadCode(), taskScheduleProperties.getDeadExpiration());
            }
        } else {
            if (taskService.isExistsTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.getDeadCode(), TaskStatusCode.PROCESSING)) {
                taskService.pauseTask(APP_PACKAGE_NAME, taskOwnerId, taskScheduleProperties.getDeadCode());
            }
        }

        mqttSendService.sendMessage(topic, ManagementResponse.APP_MANAGER_MANAGEMENT_OBSERVE_MONG.toResponseDto(GetMongResponseDto.of(event)));
    }
}
