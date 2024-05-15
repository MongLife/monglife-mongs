package com.mongs.play.app.player.internal.collection.comsumer;

import com.mongs.play.app.player.internal.collection.service.PlayerInternalCollectionService;
import com.mongs.play.module.kafka.event.playerInternal.RegisterMapCollectionEvent;
import com.mongs.play.module.kafka.event.playerInternal.RegisterMongCollectionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlayerInternalRollbackConsumer {

    private final PlayerInternalCollectionService playerInternalCollectionService;

    @KafkaListener(topics = "rollback.registerMapCollection")
    public void registerMapCollection(RegisterMapCollectionEvent event) {

        Long accountId = event.getAccountId();
        String mapCode = event.getMapCode();

        playerInternalCollectionService.removeMapCollection(accountId, mapCode);
    }

    @KafkaListener(topics = "rollback.registerMongCollection")
    public void registerMongCollection(RegisterMongCollectionEvent event) {

        Long accountId = event.getAccountId();
        String mongCode = event.getMongCode();

        playerInternalCollectionService.removeMongCollection(accountId, mongCode);
    }
}
