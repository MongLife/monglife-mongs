package com.monglife.mongs.app.manager.management.listener;

import com.monglife.mongs.app.manager.global.config.TaskScheduleProperties;
import com.monglife.mongs.app.manager.management.dto.response.MongObserveResponseDto;
import com.monglife.mongs.app.manager.management.dto.response.MongStateObserveResponseDto;
import com.monglife.mongs.app.manager.management.dto.response.MongStatusObserveResponseDto;
import com.monglife.mongs.app.manager.management.enums.ManagerResponse;
import com.monglife.mongs.domain.mong.dto.event.MongObserveEvent;
import com.monglife.mongs.domain.mong.dto.event.MongObserveMetaEvent;
import com.monglife.mongs.domain.mong.dto.event.MongObserveStateEvent;
import com.monglife.mongs.domain.mong.dto.event.MongObserveStatusEvent;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import com.monglife.mongs.domain.mong.service.MongService;
import com.monglife.mongs.domain.task.dto.etc.GetTaskDto;
import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import com.monglife.mongs.domain.task.exception.AlreadyExistsTaskException;
import com.monglife.mongs.domain.task.exception.NotExistsTaskCodeException;
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

    @Value("${application.app-code}")
    private String APP_CODE;

    private final TaskScheduleProperties taskScheduleProperties;

    private final MqttSendService mqttSendService;

    private final TaskService taskService;


    /**
     * 몽 변경 이벤트 리스너
     * @param event 몽 변경 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void mongObserveEventListener(MongObserveEvent event) {

        String topic = String.valueOf(event.getMongId());

        MongObserveResponseDto mongObserveResponseDto = MongObserveResponseDto.builder()
                .payPoint(event.getPayPoint())
                .mongTypeCode(event.getMongTypeCode())
                .build();

        mqttSendService.sendMessage(topic, ManagerResponse.MANAGER_MANAGEMENT_OBSERVE_MONG.toResponseDto(mongObserveResponseDto));
    }

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
            if (taskService.isExistsTask(APP_CODE, taskOwnerId, taskScheduleProperties.dead.getCode(), TaskStatusCode.PAUSE)) {
                taskService.resumeTask(APP_CODE, taskOwnerId, taskScheduleProperties.dead.getCode());
            } else if (!MongStateCode.DEAD.equals(event.getStateCode())) {
                taskService.createTask(APP_CODE, taskOwnerId, taskScheduleProperties.dead.getCode(), taskScheduleProperties.dead.getExpiration());
            }
        } else {
            if (taskService.isExistsTask(APP_CODE, taskOwnerId, taskScheduleProperties.dead.getCode(), TaskStatusCode.PROCESSING)) {
                taskService.pauseTask(APP_CODE, taskOwnerId, taskScheduleProperties.dead.getCode());
            }
        }

        String topic = String.valueOf(event.getMongId());

        MongStatusObserveResponseDto mongStatusObserveResponseDto = MongStatusObserveResponseDto.builder()
                .statusCode(event.getStatusCode())
                .weight(event.getWeight())
                .expRatio(event.getExpRatio())
                .strengthRatio(event.getStrengthRatio())
                .satietyRatio(event.getSatietyRatio())
                .healthyRatio(event.getHealthyRatio())
                .fatigueRatio(event.getFatigueRatio())
                .poopCount(event.getPoopCount())
                .build();

        mqttSendService.sendMessage(topic, ManagerResponse.MANAGER_MANAGEMENT_OBSERVE_MONG_STATUS.toResponseDto(mongStatusObserveResponseDto));
    }

    /**
     * 몽 상태 변경 이벤트 리스너
     * @param event 몽 상태 변경 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void mongObserveStateEventListener(MongObserveStateEvent event) {

        if (MongStateCode.DEAD.equals(event.getStateCode())) {
            String taskOwnerId = String.valueOf(event.getMongId());
            taskService.deleteAllTasks(APP_CODE, taskOwnerId);
        }

        String topic = String.valueOf(event.getMongId());

        MongStateObserveResponseDto mongStateObserveResponseDto = MongStateObserveResponseDto.builder()
                .stateCode(event.getStateCode())
                .isSleep(event.getIsSleep())
                .build();

        mqttSendService.sendMessage(topic, ManagerResponse.MANAGER_MANAGEMENT_OBSERVE_MONG_STATE.toResponseDto(mongStateObserveResponseDto));
    }
}
