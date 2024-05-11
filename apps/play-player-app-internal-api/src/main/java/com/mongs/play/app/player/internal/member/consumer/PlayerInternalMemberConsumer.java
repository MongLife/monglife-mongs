package com.mongs.play.app.player.internal.member.consumer;

import com.mongs.play.app.player.internal.member.service.PlayerInternalMemberService;
import com.mongs.play.app.player.internal.member.vo.IncreaseStarPointVo;
import com.mongs.play.module.kafka.event.playerInternal.IncreaseStarPointEvent;
import com.mongs.play.module.kafka.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlayerInternalMemberConsumer {

    private final PlayerInternalMemberService playerInternalMemberService;
    private final KafkaService kafkaService;

    @KafkaListener(topics = { "commit.registerMapCollection", "commit.registerMongCollection" })
    public void increaseStarPoint(IncreaseStarPointEvent event) {

        Long accountId = event.getAccountId();
        Integer starPoint = event.getAddStarPoint();

        IncreaseStarPointVo increaseStarPointVo = playerInternalMemberService.increaseStarPoint(accountId, starPoint);

        kafkaService.sendCommit(KafkaService.KafkaTopic.INCREASE_STAR_POINT, IncreaseStarPointEvent.builder()
                .accountId(increaseStarPointVo.accountId())
                .addStarPoint(increaseStarPointVo.addStarPoint())
                .build());
    }

    @KafkaListener(topics = { "commit.increasePayPoint" })
    public void increasePayPoint() {

    }
}
