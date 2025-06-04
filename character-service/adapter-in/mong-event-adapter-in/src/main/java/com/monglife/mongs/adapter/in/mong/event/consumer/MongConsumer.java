package com.monglife.mongs.adapter.in.mong.event.consumer;

import com.monglife.core.exception.ErrorException;
import com.monglife.module.common.kafka.event.TransactionEvent;
import com.monglife.module.common.kafka.service.KafkaService;
import com.monglife.mongs.adapter.transaction.*;
import com.monglife.mongs.application.mong.port.in.ManagementUseCase;
import com.monglife.mongs.application.mong.port.in.command.*;
import com.monglife.mongs.core.kafka.event.enums.EventTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongConsumer {

    private final ManagementUseCase managementUseCase;

    private final KafkaService kafkaService;

    /**
     * 걸음 수 환전 이벤트 리스너
     */
    @KafkaListener(topics = "${spring.config.activate.on-profile}." + EventTopic.COMMIT_EXCHANGE_CURRENT_WALKING_COUNT)
    public void exchangeCurrentWalkingCountEvent(TransactionEvent<ExchangeCurrentWalkingCountEventDto> event) {

        if (event.getData() != null) {
            try {
                IncreaseMongPayPointCommand command = IncreaseMongPayPointCommand.builder()
                        .accountId(event.getData().getAccountId())
                        .mongId(event.getData().getMongId())
                        .payPoint(event.getData().getPayPoint())
                        .build();

                managementUseCase.increaseMongPayPointUseCase(command);

            } catch (ErrorException exception) {
                kafkaService.generateEventWithProfile(EventTopic.ROLLBACK_EXCHANGE_CURRENT_WALKING_COUNT, ExchangeCurrentWalkingCountRollbackEventDto.builder()
                        .deviceId(event.getData().getDeviceId())
                        .mongId(event.getData().getMongId())
                        .walkingCount(event.getData().getWalkingCount())
                        .payPoint(event.getData().getPayPoint())
                        .build());
            }
        }
    }

    /**
     * 스타 포인트 환전 이벤트 리스너
     */
    @KafkaListener(topics = "${spring.config.activate.on-profile}." + EventTopic.COMMIT_EXCHANGE_STAR_POINT)
    public void exchangeStarPointEvent(TransactionEvent<ExchangeStarPointEventDto> event) {

        if (event.getData() != null) {
            try {
                IncreaseMongPayPointCommand command = IncreaseMongPayPointCommand.builder()
                        .accountId(event.getData().getAccountId())
                        .mongId(event.getData().getMongId())
                        .payPoint(event.getData().getPayPoint())
                        .build();

                managementUseCase.increaseMongPayPointUseCase(command);

            } catch (ErrorException exception) {
                kafkaService.generateEventWithProfile(EventTopic.ROLLBACK_EXCHANGE_STAR_POINT, ExchangeStarPointRollbackEventDto.builder()
                        .accountId(event.getData().getAccountId())
                        .mongId(event.getData().getMongId())
                        .starPoint(event.getData().getStarPoint())
                        .payPoint(event.getData().getPayPoint())
                        .build());
            }
        }
    }

    /**
     * 알 진화 이벤트 리스너
     */
    @KafkaListener(topics = "${spring.config.activate.on-profile}." + EventTopic.COMMIT_EGG_EVOLUTION)
    public void eggEvolutionEvent(TransactionEvent<EggEvolutionEventDto> event) {

        if (event.getData() != null) {
            EvolutionReadyMongCommand command = EvolutionReadyMongCommand.builder()
                    .accountId(event.getData().getAccountId())
                    .mongId(event.getData().getMongId())
                    .build();

            managementUseCase.evolutionReadyMongUseCase(command);
        }
    }

    /**
     * 지수 증가 이벤트 리스너
     */
    @KafkaListener(topics = "${spring.config.activate.on-profile}." + EventTopic.COMMIT_INCREASE_STATUS)
    public void increaseStatusEvent(TransactionEvent<IncreaseStatusEventDto> event) {

        if (event.getData() != null) {
            IncreaseMongStatusCommand command = IncreaseMongStatusCommand.builder()
                    .accountId(event.getData().getAccountId())
                    .mongId(event.getData().getMongId())
                    .build();

            managementUseCase.increaseMongStatusUseCase(command);
        }
    }

    /**
     * 지수 감소 이벤트 리스너
     */
    @KafkaListener(topics = "${spring.config.activate.on-profile}." + EventTopic.COMMIT_DECREASE_STATUS)
    public void decreaseStatusEvent(TransactionEvent<DecreaseStatusEventDto> event) {

        if (event.getData() != null) {
            DecreaseMongStatusCommand command = DecreaseMongStatusCommand.builder()
                    .accountId(event.getData().getAccountId())
                    .mongId(event.getData().getMongId())
                    .build();

            managementUseCase.decreaseMongStatusUseCase(command);
        }
    }

    /**
     * 배변 수 증가 이벤트 리스너
     */
    @KafkaListener(topics = "${spring.config.activate.on-profile}." + EventTopic.COMMIT_INCREASE_POOP)
    public void increasePoopEvent(TransactionEvent<IncreasePoopEventDto> event) {

        if (event.getData() != null) {
            IncreaseMongPoopCountCommand command = IncreaseMongPoopCountCommand.builder()
                    .accountId(event.getData().getAccountId())
                    .mongId(event.getData().getMongId())
                    .build();

            managementUseCase.increaseMongPoopCountUseCase(command);
        }
    }

    /**
     * 죽음 이벤트 리스너
     */
    @KafkaListener(topics = "${spring.config.activate.on-profile}." + EventTopic.COMMIT_DEAD)
    public void deadEvent(TransactionEvent<DeadEventDto> event) {

        if (event.getData() != null) {
            DeadMongCommand command = DeadMongCommand.builder()
                    .accountId(event.getData().getAccountId())
                    .mongId(event.getData().getMongId())
                    .build();

            managementUseCase.deadMongUseCase(command);
        }
    }

    /**
     * 수면 이벤트 리스너
     */
    @KafkaListener(topics = "${spring.config.activate.on-profile}." + EventTopic.COMMIT_SLEEP)
    public void sleepEvent(TransactionEvent<SleepEventDto> event) {

        if (event.getData() != null) {
            SleepMongCommand command = SleepMongCommand.builder()
                    .accountId(event.getData().getAccountId())
                    .mongId(event.getData().getMongId())
                    .build();

            managementUseCase.sleepMongUseCase(command);
        }
    }

    /**
     * 기상 이벤트 리스너
     */
    @KafkaListener(topics = "${spring.config.activate.on-profile}." + EventTopic.COMMIT_WAKEUP)
    public void wakeupEvent(TransactionEvent<WakeupEventDto> event) {

        if (event.getData() != null) {
            WakeupMongCommand command = WakeupMongCommand.builder()
                    .accountId(event.getData().getAccountId())
                    .mongId(event.getData().getMongId())
                    .build();

            managementUseCase.wakeUpMongUseCase(command);
        }
    }
}
