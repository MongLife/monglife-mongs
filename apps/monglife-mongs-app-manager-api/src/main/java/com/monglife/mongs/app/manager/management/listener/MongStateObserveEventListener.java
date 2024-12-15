package com.monglife.mongs.app.manager.management.listener;

import com.monglife.mongs.app.manager.management.dto.response.MongStateObserveResponseDto;
import com.monglife.mongs.app.manager.management.enums.ManagementResponse;
import com.monglife.mongs.domain.mong.dto.event.MongObserveStateEvent;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.task.service.TaskService;
import com.monglife.mongs.module.mqtt.service.MqttSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MongStateObserveEventListener {

    @Value("${application.app-code}")
    private String APP_CODE;

    private final MqttSendService mqttSendService;

    private final TaskService taskService;


    /**
     * 몽 상태 변경 이벤트 리스너
     * @param event 몽 상태 변경 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void mongObserveStateEventListener(MongObserveStateEvent event) {

        String taskOwnerId = String.valueOf(event.getMongId());

        if (MongStateCode.DEAD.equals(event.getStateCode()) || MongStateCode.GRADUATE_READY.equals(event.getStateCode())) {
            taskService.deleteAllTasks(APP_CODE, taskOwnerId);
        }

        String topic = String.valueOf(event.getMongId());

        MongStateObserveResponseDto mongStateObserveResponseDto = MongStateObserveResponseDto.builder()
                .stateCode(event.getStateCode())
                .isSleep(event.getIsSleep())
                .build();

        mqttSendService.sendMessage(topic, ManagementResponse.MANAGER_MANAGEMENT_OBSERVE_MONG_STATE.toResponseDto(mongStateObserveResponseDto));
    }
}
