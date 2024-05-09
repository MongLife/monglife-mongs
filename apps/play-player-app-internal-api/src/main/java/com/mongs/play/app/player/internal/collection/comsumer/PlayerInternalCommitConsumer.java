package com.mongs.play.app.player.internal.collection.comsumer;

import com.mongs.play.app.player.internal.collection.service.PlayerInternalCollectionService;
import com.mongs.play.core.error.module.KafkaErrorCode;
import com.mongs.play.core.exception.ErrorException;
import com.mongs.play.core.exception.common.KafkaTransactionException;
import com.mongs.play.module.kafka.event.commit.EvolutionMongEvent;
import com.mongs.play.module.kafka.event.rollback.EvolutionMongRollbackEvent;
import com.mongs.play.module.kafka.service.KafkaService;
import com.mongs.play.module.kafka.service.KafkaService.KafkaTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlayerInternalCommitConsumer {

    private final PlayerInternalCollectionService playerInternalCollectionService;
    private final KafkaService kafkaService;

    @KafkaListener(topics = "commit.evolutionMong" )
    public void registerMongCollection(EvolutionMongEvent event) {

        log.info("commit event: {}", event);
        Long accountId = event.getAccountId();
        String mongCode = event.getMongCode();

//        try {
//        RegisterMongCollectionVo registerMongCollectionVo =
//                playerInternalCollectionService.registerMongCollection(accountId, mongCode);


            throw new KafkaTransactionException(KafkaErrorCode.TEST);

//            kafkaService.sendCommit(KafkaTopic.REGISTER_MONG_COLLECTION, EvolutionMongEvent.builder()
//                    .id(event.getId())
//                    .createdAt(event.getCreatedAt())
//                    .accountId(accountId)
//                    .mongCode(mongCode)
//                    .build());

//        } catch (ErrorException e) {
//            kafkaService.sendRollback(KafkaTopic.EVOLUTION_MONG, EvolutionMongRollbackEvent.builder()
//                    .id(event.getId())
//                    .createdAt(event.getCreatedAt())
//                    .accountId(accountId)
//                    .mongCode(mongCode)
//                    .build());
//        }
    }
}
