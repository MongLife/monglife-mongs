package com.monglife.mongs.app.manager.management.listener;

import com.monglife.mongs.app.manager.management.dto.response.MongObserveResponseDto;
import com.monglife.mongs.app.manager.management.enums.ManagementResponse;
import com.monglife.mongs.domain.mong.dto.event.MongObserveEvent;
import com.monglife.mongs.module.mqtt.service.MqttSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MongObserveEventListener {

    private final MqttSendService mqttSendService;

    /**
     * 몽 변경 이벤트 리스너
     * @param event 몽 변경 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void mongObserveEventListener(MongObserveEvent event) {

        String topic = "management/" + event.getMongId();

        MongObserveResponseDto mongObserveResponseDto = MongObserveResponseDto.builder()
                .payPoint(event.getPayPoint())
                .mongTypeCode(event.getMongTypeCode())
                .build();

        mqttSendService.sendMessage(topic, ManagementResponse.APP_MANAGER_MANAGEMENT_OBSERVE_MONG.toResponseDto(mongObserveResponseDto));
    }
}
