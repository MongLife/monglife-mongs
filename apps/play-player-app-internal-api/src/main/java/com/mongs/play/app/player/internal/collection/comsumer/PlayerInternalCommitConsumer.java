package com.mongs.play.app.player.internal.collection.comsumer;

import com.mongs.play.app.player.internal.collection.service.PlayerInternalCollectionService;
import com.mongs.play.module.kafka.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlayerInternalCommitConsumer {

    private final PlayerInternalCollectionService playerInternalCollectionService;
    private final KafkaService kafkaService;

//    @KafkaListener(topics = "commit.evolutionMong" )
//    public void registerMongCollection(ZeroEvolutionMongScheduleEvent event) {
//
//        Long accountId = event.getAccountId();
//        String mongCode = event.getMongCode();
//
//        RegisterMongCollectionVo registerMongCollectionVo =
//                playerInternalCollectionService.registerMongCollection(accountId, mongCode);
//
//        kafkaService.sendCommit(KafkaService.KafkaTopic.REGISTER_MONG_COLLECTION, RegisterMongCollectionEvent.builder()
//                .accountId(registerMongCollectionVo.accountId())
//                .mongCode(registerMongCollectionVo.code())
//                .createdAt(registerMongCollectionVo.createdAt())
//                .build());
//    }
}
