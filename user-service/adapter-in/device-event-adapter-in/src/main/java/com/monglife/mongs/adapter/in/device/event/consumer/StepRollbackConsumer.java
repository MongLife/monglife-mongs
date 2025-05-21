package com.monglife.mongs.adapter.in.device.event.consumer;

import com.monglife.module.common.kafka.event.TransactionEvent;
import com.monglife.mongs.adapter.transaction.ExchangeCurrentWalkingCountEventDto;
import com.monglife.mongs.application.device.port.in.StepUseCase;
import com.monglife.mongs.application.device.port.in.command.IncreaseCurrentWalkingCountCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StepRollbackConsumer {

    private final StepUseCase stepUseCase;

    /**
     * 걸음 수 환전 트랜잭션 롤백 이벤트 리스너
     * @param event 걸음 수 환전 정보
     */
    @KafkaListener(topics = "rollback.exchangeCurrentWalkingCount")
    public void exchangeCurrentWalkingCountRollbackEvent(TransactionEvent<ExchangeCurrentWalkingCountEventDto> event) {

        if (event.getData() != null) {
            IncreaseCurrentWalkingCountCommand command = IncreaseCurrentWalkingCountCommand.builder()
                    .deviceId(event.getData().getDeviceId())
                    .walkingCount(event.getData().getWalkingCount())
                    .build();

            stepUseCase.increaseCurrentWalkingCountUseCase(command);
        }
    }
}
