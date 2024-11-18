package com.monglife.mongs.app.battle.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monglife.mongs.app.battle.client.MqttOutBoundClient;
import com.monglife.mongs.app.battle.dto.etc.CreateBattleDto;
import com.monglife.mongs.app.battle.dto.etc.OverBattleDto;
import com.monglife.mongs.app.battle.dto.response.BattleResponseDto;
import com.monglife.mongs.app.battle.dto.response.CreateBattleResponseDto;
import com.monglife.mongs.app.battle.global.enums.BattleResponse;
import com.monglife.mongs.app.battle.global.enums.BattleStateCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class MqttSendService {

    @Value("${application.mqtt.topic}")
    private String TOPIC_FILTER;

    private final MqttOutBoundClient mqttOutBoundClient;

    private final ObjectMapper objectMapper;


    public void createBattleSend(Set<CreateBattleDto> createBattleDtoSet) {
        try {

            createBattleDtoSet.forEach(createBattleDto -> {

                CreateBattleResponseDto createBattleResponseDto  = CreateBattleResponseDto.builder()
                        .roomId(createBattleDto.get)
                        .build();

                BattleResponseDto<CreateBattleResponseDto> createBattleResponseDto =
                        BattleResponseDto.<CreateBattleResponseDto>builder()
                                .code(BattleStateCode.BATTLE_CREATE)
                                .data()
                                .build();

                String topic = TOPIC_FILTER + "battle/search/" + deviceId;

                String dataJson = objectMapper.writeValueAsString(createBattleResponseDto);

                mqttOutBoundClient.send(, dataJson);

            });
        } catch (JsonProcessingException ignored) {}
    }

    public void overBattleSend(OverBattleDto overBattleDto) {
        try {
            String dataJson = objectMapper.writeValueAsString(BasicPublishBattleDto.builder().code(PublishBattleCode.MATCH_FIND).data(data).build());
            mqttOutBoundClient.send(TOPIC_FILTER + "battle/match/" + deviceId, dataJson);
        } catch (JsonProcessingException ignored) {}
    }
}
