package com.monglife.mongs.app.manager.management.listener;

import com.monglife.mongs.app.manager.management.dto.response.MongObserveResponseDto;
import com.monglife.mongs.app.manager.management.dto.response.MongStateObserveResponseDto;
import com.monglife.mongs.app.manager.management.dto.response.MongStatusObserveResponseDto;
import com.monglife.mongs.app.manager.management.enums.ManagerResponse;
import com.monglife.mongs.domain.mong.dto.event.MongObserveEvent;
import com.monglife.mongs.domain.mong.dto.event.MongObserveStateEvent;
import com.monglife.mongs.domain.mong.dto.event.MongObserveStatusEvent;
import com.monglife.mongs.domain.mong.service.MongService;
import com.monglife.mongs.module.mqtt.service.MqttSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MongObserveEventListener {

    private final MqttSendService mqttSendService;

    private final MongService mongService;


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

        mqttSendService.sendMessage(topic, ManagerResponse.MANAGER_MANAGEMENT_MONG_OBSERVE_MONG.toResponseDto(mongObserveResponseDto));
    }

    /**
     * 몽 지수 변경 이벤트 리스너
     * @param event 몽 지수 변경 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void mongObserveStatusEventListener(MongObserveStatusEvent event) {

        if (event.getExpRatio() >= 100) mongService.evolutionReadyMong(event.getMongId());

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

        String topic = String.valueOf(event.getMongId());

        MongStateObserveResponseDto mongStateObserveResponseDto = MongStateObserveResponseDto.builder()
                .stateCode(event.getStateCode())
                .isSleep(event.getIsSleep())
                .build();

        mqttSendService.sendMessage(topic, ManagerResponse.MANAGER_MANAGEMENT_OBSERVE_MONG_STATE.toResponseDto(mongStateObserveResponseDto));
    }
}
