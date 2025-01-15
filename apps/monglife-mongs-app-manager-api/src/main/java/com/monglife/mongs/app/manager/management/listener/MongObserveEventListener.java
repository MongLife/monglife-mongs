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

    private static final String SERVICE_DOMAIN = "management";

    @Value("${application.app-package-name}")
    private String APP_PACKAGE_NAME;

    private final TaskScheduleProperties properties;

    private final MqttSendService mqttSendService;

    private final TaskService taskService;

    /**
     * 몽 변경 이벤트 리스너
     * @param event 몽 변경 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void mongObserveEventListener(MongObserveEvent event) {

        String topic = String.format("%s/%d", SERVICE_DOMAIN, event.getMongId());
        String taskOwnerId = String.valueOf(event.getMongId());

        // mong status
        if (MongStateCode.DEAD.equals(event.getStateCode()) || MongStateCode.GRADUATE_READY.equals(event.getStateCode())) {
            taskService.deleteAllTasks(APP_PACKAGE_NAME, taskOwnerId);
        }

        // mong state
        boolean isDeadStatus = event.getSatietyRatio() <= properties.dead.satietyRatio || event.getHealthyRatio() <= properties.dead.healthyRatio;

        if (isDeadStatus) {
            // 죽음 스케줄러 존재하는 경우
            if (taskService.isExistsTask(APP_PACKAGE_NAME, taskOwnerId, properties.dead.code, TaskStatusCode.PAUSE)) {
                // 기존 죽음 스케줄러 재가동
                taskService.resumeTask(APP_PACKAGE_NAME, taskOwnerId, properties.dead.code);
            }
            // 죽음 스케줄러 없는 경우
            else if (!taskService.isExistsTask(APP_PACKAGE_NAME, taskOwnerId, properties.dead.code, TaskStatusCode.PROCESSING) && !MongStateCode.DEAD.equals(event.getStateCode())) {
                // 새로운 죽음 스케줄러 생성
                taskService.createTask(APP_PACKAGE_NAME, taskOwnerId, properties.dead.code, properties.dead.expiration);
            }
        } else {
            // 죽음 스케줄러 존재하는 경우
            if (taskService.isExistsTask(APP_PACKAGE_NAME, taskOwnerId, properties.dead.code, TaskStatusCode.PROCESSING)) {
                // 일시 중지
                taskService.pauseTask(APP_PACKAGE_NAME, taskOwnerId, properties.dead.code);
            }
        }

        mqttSendService.sendMessage(topic, ManagementResponse.APP_MANAGER_MANAGEMENT_OBSERVE_MONG.toResponseDto(GetMongResponseDto.of(event)));
    }
}
