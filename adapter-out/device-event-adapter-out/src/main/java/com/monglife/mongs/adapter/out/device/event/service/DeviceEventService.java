package com.monglife.mongs.adapter.out.device.event.service;

import com.monglife.module.common.kafka.service.KafkaService;
import com.monglife.mongs.adapter.out.device.event.dto.ExchangeCurrentWalkingCountEventDto;
import com.monglife.mongs.application.device.port.out.DeviceEventPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceEventService implements DeviceEventPort {

    private final KafkaService kafkaService;

    /**
     * 걸음 수 환전 분산 트랜잭션 이벤트 발생
     * @param mongId 몽 ID
     * @param walkingCount 환전할 걸음 수
     * @param payPoint 환전할 페이 포인트
     */
    @Override
    public void exchangeCurrentWalkingCountEventPort(Long mongId, Integer walkingCount, Integer payPoint) {

        String topic = "commit.exchangeCurrentWalkingCount";

        ExchangeCurrentWalkingCountEventDto exchangeCurrentWalkingCountEventDto = ExchangeCurrentWalkingCountEventDto.builder()
                .mongId(mongId)
                .walkingCount(walkingCount)
                .payPoint(payPoint)
                .build();

        kafkaService.generateEvent(topic, exchangeCurrentWalkingCountEventDto);
    }
}
