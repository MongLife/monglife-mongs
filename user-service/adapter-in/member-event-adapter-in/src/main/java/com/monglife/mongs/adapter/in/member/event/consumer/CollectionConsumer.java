package com.monglife.mongs.adapter.in.member.event.consumer;

import com.monglife.module.common.kafka.event.TransactionEvent;
import com.monglife.mongs.adapter.transaction.CreateCollectionMongEventDto;
import com.monglife.mongs.application.member.port.in.CollectionUseCase;
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
     * 컬렉션 몽 등록 트랜잭션 이벤트 리스너
     * @param event 컬렉션 몽 등록 정보
     */
    @KafkaListener(topics = EventTopic.COMMIT_CREATE_COLLECTION_MONG)
    public void createCollectionMongEvent(TransactionEvent<CreateCollectionMongEventDto> event) {

        if (event.getData() != null) {
            CreateCollectionMongCommand command = CreateCollectionMongCommand.builder()
                    .accountId(event.getData().getAccountId())
                    .mongTypeCode(event.getData().getMongTypeCode())
                    .build();

            collectionUseCase.createCollectionMongUseCase(command);
        }
    }
}
