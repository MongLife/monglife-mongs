package com.monglife.mongs.app.user.player.listener;

import com.monglife.mongs.app.user.player.dto.response.MemberObserveResponseDto;
import com.monglife.mongs.app.user.player.enums.PlayerResponse;
import com.monglife.mongs.domain.member.dto.event.MemberObserveEvent;
import com.monglife.mongs.module.mqtt.service.MqttSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MemberObserveListener {

    private final MqttSendService mqttSendService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void memberObserveEventListener(MemberObserveEvent event) {

        String topic = String.valueOf(event.getAccountId());

        MemberObserveResponseDto memberObserveResponseDto = MemberObserveResponseDto.builder()
                .accountId(event.getAccountId())
                .slotCount(event.getSlotCount())
                .starPoint(event.getStarPoint())
                .walkingCount(event.getWalkingCount())
                .build();

        mqttSendService.sendMessage(topic, PlayerResponse.USER_PLAYER_OBSERVE_MEMBER.toResponseDto(memberObserveResponseDto));
    }
}
