package com.monglife.mongs.app.user.player.publisher;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.user.player.dto.response.MemberObserveResponseDto;
import com.monglife.mongs.app.user.player.enums.PlayerResponse;
import com.monglife.mongs.module.mqtt.annotation.MqttPublish;
import com.monglife.mongs.module.mqtt.dto.MqttResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerPublisher {

    @MqttPublish("/player/{topic}")
    public MqttResponseEntity<ResponseDto<MemberObserveResponseDto>> memberObservePublish(Long accountId, MemberObserveResponseDto memberObserveResponseDto) {

        return MqttResponseEntity
                .body(PlayerResponse.APP_USER_PLAYER_OBSERVE_MEMBER.toResponseDto(memberObserveResponseDto))
                .topic(accountId.toString());
    }
}
