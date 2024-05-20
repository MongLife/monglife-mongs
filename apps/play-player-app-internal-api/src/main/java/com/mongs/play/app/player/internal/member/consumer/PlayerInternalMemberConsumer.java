package com.mongs.play.app.player.internal.member.consumer;

import com.mongs.play.app.player.internal.member.service.PlayerInternalMemberService;
import com.mongs.play.core.exception.common.CommonErrorException;
import com.mongs.play.module.kafka.event.playerInternal.RegisterMapCollectionEvent;
import com.mongs.play.module.kafka.event.playerInternal.RegisterMongCollectionEvent;
import com.mongs.play.module.kafka.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlayerInternalMemberConsumer {

    private final KafkaService kafkaService;
    private final PlayerInternalMemberService playerInternalMemberService;

    @KafkaListener(topics = { "commit.registerMapCollection" })
    public void increaseStarPoint(RegisterMapCollectionEvent payload) {
        try {
            Long accountId = payload.getAccountId();

            var vo = playerInternalMemberService.increaseStarPointMapCollection(accountId);

        } catch (CommonErrorException e) {
            kafkaService.sendRollback(KafkaService.CommitTopic.REGISTER_MAP_COLLECTION, payload);
        }
    }

    @KafkaListener(topics = { "commit.registerMongCollection" })
    public void increaseStarPoint(RegisterMongCollectionEvent payload) {
        try {
            Long accountId = payload.getAccountId();

            var vo = playerInternalMemberService.increaseStarPointMongCollection(accountId);

        } catch (CommonErrorException e) {
            kafkaService.sendRollback(KafkaService.CommitTopic.REGISTER_MONG_COLLECTION, payload);
        }
    }
}
