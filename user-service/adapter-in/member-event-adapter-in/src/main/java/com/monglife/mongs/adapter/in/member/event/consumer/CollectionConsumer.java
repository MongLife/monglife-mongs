package com.monglife.mongs.adapter.in.member.event.consumer;

import com.monglife.module.common.kafka.event.TransactionEvent;
import com.monglife.mongs.adapter.transaction.CreateMongEventDto;
import com.monglife.mongs.adapter.transaction.EvolutionMongEventDto;
import com.monglife.mongs.adapter.transaction.RandomDrawMapEventDto;
import com.monglife.mongs.application.member.port.in.CollectionUseCase;
import com.monglife.mongs.application.member.port.in.command.CreateCollectionMapCommand;
import com.monglife.mongs.application.member.port.in.command.CreateCollectionMongCommand;
import com.monglife.mongs.core.kafka.event.enums.EventTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CollectionConsumer {

    private final CollectionUseCase collectionUseCase;

    /**
     * 몽 등록 트랜잭션 이벤트 리스너
     * @param event 몽 등록 정보
     */
    @KafkaListener(topics = "${spring.config.activate.on-profile}." + EventTopic.COMMIT_CREATE_MONG)
    public void createMongEvent(TransactionEvent<CreateMongEventDto> event) {

        if (event.getData() != null) {
            CreateCollectionMongCommand command = CreateCollectionMongCommand.builder()
                    .accountId(event.getData().getAccountId())
                    .mongCode(event.getData().getMongCode())
                    .build();

            collectionUseCase.createCollectionMongUseCase(command);
        }
    }

    /**
     * 몽 진화 이벤트 트랜잭션 리스너
     * @param event 몽 진화 캐릭터 정보
     */
    @KafkaListener(topics = "${spring.config.activate.on-profile}." + EventTopic.COMMIT_EVOLUTION_MONG)
    public void evolutionMongEvent(TransactionEvent<EvolutionMongEventDto> event) {

        if (event.getData() != null) {
            CreateCollectionMongCommand command = CreateCollectionMongCommand.builder()
                    .accountId(event.getData().getAccountId())
                    .mongCode(event.getData().getMongCode())
                    .build();

            collectionUseCase.createCollectionMongUseCase(command);
        }
    }

    /**
     * 랜덤 뽑기 맵 당첨 이벤트 리스너
     * @param event 뽑은 맵 정보
     */
    @KafkaListener(topics = "${spring.config.activate.on-profile}." + EventTopic.COMMIT_RANDOM_DRAW_MAP)
    public void randomDrawMapEvent(TransactionEvent<RandomDrawMapEventDto> event) {

        if (event.getData() != null) {
            CreateCollectionMapCommand command = CreateCollectionMapCommand.builder()
                    .accountId(event.getData().getAccountId())
                    .mapCode(event.getData().getMapCode())
                    .build();

            collectionUseCase.createCollectionMapUseCase(command);
        }
    }
}
