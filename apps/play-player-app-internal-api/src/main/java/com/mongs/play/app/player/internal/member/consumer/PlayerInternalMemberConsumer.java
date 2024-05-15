package com.mongs.play.app.player.internal.member.consumer;

import com.mongs.play.app.player.internal.member.dto.res.IncreaseStarPointResDto;
import com.mongs.play.app.player.internal.member.service.PlayerInternalMemberService;
import com.mongs.play.client.publisher.mong.annotation.Notification;
import com.mongs.play.client.publisher.mong.code.PublishCode;
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

    @Notification(code = PublishCode.INCREASE_STAR_POINT)
    @KafkaListener(topics = { "commit.registerMapCollection" })
    public IncreaseStarPointResDto increaseStarPoint(RegisterMapCollectionEvent payload) {

        IncreaseStarPointResDto increaseStarPointResDto = null;

        try {
            Long accountId = payload.getAccountId();

            var vo = playerInternalMemberService.increaseStarPointMapCollection(accountId);
            increaseStarPointResDto = IncreaseStarPointResDto.builder()
                    .accountId(vo.accountId())
                    .starPoint(vo.starPoint())
                    .build();

        } catch (CommonErrorException e) {
            kafkaService.sendRollback(KafkaService.KafkaTopic.REGISTER_MAP_COLLECTION, payload);
        }

        return increaseStarPointResDto;
    }

    @Notification(code = PublishCode.INCREASE_STAR_POINT)
    @KafkaListener(topics = { "commit.registerMongCollection" })
    public IncreaseStarPointResDto increaseStarPoint(RegisterMongCollectionEvent payload) {

        IncreaseStarPointResDto increaseStarPointResDto = null;

        try {
            Long accountId = payload.getAccountId();

            var vo = playerInternalMemberService.increaseStarPointMongCollection(accountId);
            increaseStarPointResDto = IncreaseStarPointResDto.builder()
                    .accountId(vo.accountId())
                    .starPoint(vo.starPoint())
                    .build();

        } catch (CommonErrorException e) {
            kafkaService.sendRollback(KafkaService.KafkaTopic.REGISTER_MONG_COLLECTION, payload);
        }

        return increaseStarPointResDto;
    }
}
