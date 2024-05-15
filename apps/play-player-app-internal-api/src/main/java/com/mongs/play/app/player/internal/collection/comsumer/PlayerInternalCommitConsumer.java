package com.mongs.play.app.player.internal.collection.comsumer;

import com.mongs.play.app.player.internal.collection.service.PlayerInternalCollectionService;
import com.mongs.play.app.player.internal.collection.vo.RegisterMongCollectionVo;
import com.mongs.play.core.exception.common.CommonErrorException;
import com.mongs.play.module.kafka.event.managementExternal.EvolutionMongEvent;
import com.mongs.play.module.kafka.event.managementExternal.RegisterMongEvent;
import com.mongs.play.module.kafka.event.playerInternal.RegisterMongCollectionEvent;
import com.mongs.play.module.kafka.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlayerInternalCommitConsumer {

    private final KafkaService kafkaService;
    private final PlayerInternalCollectionService playerInternalCollectionService;

    @KafkaListener(topics = { "commit.registerMong" })
    public void registerMongCollection(RegisterMongEvent payload) {
        try {
            Long accountId = payload.getAccountId();
            String mongCode = payload.getMongCode();

            log.info("[registerMongCollection registerMong] accountId: {}, mongCode: {}", accountId, mongCode);

            playerInternalCollectionService.registerMongCollection(accountId, mongCode);

        } catch (CommonErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.REGISTER_MONG, payload);
        }
    }

    @KafkaListener(topics = { "commit.evolutionMong.first", "commit.evolutionMong" } )
    public void registerMongCollection(EvolutionMongEvent payload) {
        try {
            Long accountId = payload.getAccountId();
            String mongCode = payload.getMongCode();

            log.info("[registerMongCollection evolutionMong] accountId: {}, mongCode: {}", accountId, mongCode);

            playerInternalCollectionService.registerMongCollection(accountId, mongCode);

        } catch (CommonErrorException e) {
//            if (payload.getTopic().equals(KafkaService.KafkaTopic.FIRST_EVOLUTION_MONG.topic)) {
//                kafkaService.sendRollback(KafkaService.KafkaTopic.FIRST_EVOLUTION_MONG, payload);
//            }
//            if (payload.getTopic().equals(KafkaService.KafkaTopic.EVOLUTION_MONG.topic)) {
//                kafkaService.sendRollback(KafkaService.KafkaTopic.EVOLUTION_MONG, payload);
//            }
        }
    }

}
