package com.monglife.mongs.adapter.in.member.event.consumer;

import com.monglife.module.common.kafka.event.TransactionEvent;
import com.monglife.mongs.adapter.transaction.ExchangeStarPointEventDto;
import com.monglife.mongs.application.member.port.in.PlayerUseCase;
import com.monglife.mongs.application.member.port.in.command.IncreaseStarPointCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerRollbackConsumer {

    private final PlayerUseCase playerUseCase;

    /**
     * 스타 포인트 환전 트랜잭션 롤백 이벤트 리스너
     * @param event 스타 포인트 환전 정보
     */
    @KafkaListener(topics = "rollback.exchangeStarPoint")
    public void exchangeStarPointRollbackEvent(TransactionEvent<ExchangeStarPointEventDto> event) {

        if (event.getData() != null) {
            IncreaseStarPointCommand command = IncreaseStarPointCommand.builder()
                    .accountId(event.getData().getAccountId())
                    .starPoint(event.getData().getStarPoint())
                    .build();

            playerUseCase.increaseStarPointUseCase(command);
        }
    }
}
