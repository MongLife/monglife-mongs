package com.monglife.mongs.adapter.in.device.event.consumer;

import com.monglife.module.common.kafka.event.TransactionEvent;
import com.monglife.module.common.logging.annotation.EntryLoggingPoint;
import com.monglife.mongs.adapter.transaction.ExchangeCurrentWalkingCountRollbackEventDto;
import com.monglife.mongs.application.device.port.in.StepUseCase;
import com.monglife.mongs.application.device.port.in.command.RestoreExchangedWalkingCountCommand;
import com.monglife.mongs.core.kafka.event.enums.EventTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class StepRollbackConsumer {

    private final StepUseCase stepUseCase;

    /**
     * 걸음 수 환전 트랜잭션 롤백 이벤트 리스너
     *
     * 서버는 걸음 수를 보관하지 않으므로 되돌릴 상태가 없다. 잔액을 들고 있는 기기에게
     * 복구를 알리는 것이 서버가 할 수 있는 전부다.
     *
     * 그 알림은 MQTT 라 기기가 연결돼 있지 않으면 유실된다. 수동 보상이 가능하도록
     * 여기서 실패 내용을 남긴다.
     */
    @EntryLoggingPoint
    @KafkaListener(topics = "${spring.config.activate.on-profile}." + EventTopic.ROLLBACK_EXCHANGE_CURRENT_WALKING_COUNT)
    public void exchangeCurrentWalkingCountRollbackEvent(TransactionEvent<ExchangeCurrentWalkingCountRollbackEventDto> event) {

        if (event.getData() == null) {
            return;
        }

        ExchangeCurrentWalkingCountRollbackEventDto data = event.getData();

        log.error("걸음 수 환전 실패 - 기기에 복구를 알린다. deviceId={}, mongId={}, walkingCount={}, payPoint={}",
                data.getDeviceId(), data.getMongId(), data.getWalkingCount(), data.getPayPoint());

        // 이벤트의 transactionId 를 기기 쪽 중복 방지 키로 쓴다. Kafka 는 at-least-once 라
        // 같은 롤백이 재전달될 수 있는데, 여기서 매번 새 키를 만들면 기기가 중복을 걸러 낼 수 없다.
        String eventId = event.getTransactionId() != null ? event.getTransactionId() : UUID.randomUUID().toString();

        stepUseCase.restoreExchangedWalkingCountUseCase(RestoreExchangedWalkingCountCommand.builder()
                .deviceId(data.getDeviceId())
                .walkingCount(data.getWalkingCount())
                .eventId(eventId)
                .build());
    }
}
