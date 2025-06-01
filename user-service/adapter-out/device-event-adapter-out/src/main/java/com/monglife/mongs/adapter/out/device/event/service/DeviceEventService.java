package com.monglife.mongs.adapter.out.device.event.service;

import com.monglife.module.common.kafka.service.KafkaService;
import com.monglife.mongs.adapter.transaction.ExchangeCurrentWalkingCountEventDto;
import com.monglife.mongs.application.device.port.out.DeviceEventPort;
import com.monglife.mongs.application.device.port.out.dto.ExchangeCurrentWalkingCountDto;
import com.monglife.mongs.core.kafka.event.enums.EventTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceEventService implements DeviceEventPort {

    private final KafkaService kafkaService;

    /**
     * 걸음 수 환전 분산 트랜잭션 이벤트 발생
     * @param stepEventDto 걸음 수 이벤트 Dto
     */
    @Override
    public void exchangeCurrentWalkingCountEventPort(ExchangeCurrentWalkingCountDto stepEventDto) {

        com.monglife.mongs.adapter.transaction.ExchangeCurrentWalkingCountEventDto exchangeCurrentWalkingCountEventDto = ExchangeCurrentWalkingCountEventDto.builder()
                .deviceId(stepEventDto.getDeviceId())
                .mongId(stepEventDto.getMongId())
                .walkingCount(stepEventDto.getWalkingCount())
                .payPoint(stepEventDto.getPayPoint())
                .build();

        kafkaService.generateEvent(EventTopic.COMMIT_EXCHANGE_CURRENT_WALKING_COUNT, exchangeCurrentWalkingCountEventDto);
    }
}
