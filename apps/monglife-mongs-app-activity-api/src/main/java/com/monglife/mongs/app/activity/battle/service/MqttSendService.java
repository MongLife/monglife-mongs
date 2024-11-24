package com.monglife.mongs.app.activity.battle.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.activity.battle.client.MqttOutBoundClient;
import com.monglife.mongs.app.activity.battle.dto.response.BattleResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MqttSendService {

    @Value("${application.client.mqtt.base-topic}")
    private String BASE_TOPIC;

    private final MqttOutBoundClient mqttOutBoundClient;

    private final ObjectMapper objectMapper;


    public <T> void sendMessage(ResponseDto<BattleResponseDto<T>> responseDto) {
        try {
            String topic = BASE_TOPIC;
            String subTopic = responseDto.getResult().getCode().getSubTopic();
            List<String> thirdTopics = responseDto.getResult().getTopics();

            for (String thirdTopic : thirdTopics) {

                String sendTopic = String.format("%s/%s/%s", topic, subTopic, thirdTopic);

                String dataJson = objectMapper.writeValueAsString(responseDto);

                mqttOutBoundClient.send(sendTopic, dataJson);

                log.info("[MqttSendService] [sendMessage] {} : {}", sendTopic, dataJson);
            }
        } catch (JsonProcessingException e) {
            log.error("[MqttSendService] [sendMessage] {} : {}", e.getClass().getSimpleName(), e.getMessage());
        }
    }

    public <T> void sendMessage(ResponseDto<T> responseDto, String receiveTopic) {
        try {
            String topic = BASE_TOPIC;

            String sendTopic = String.format("%s/%s", topic, receiveTopic);

            String dataJson = objectMapper.writeValueAsString(responseDto);

            mqttOutBoundClient.send(sendTopic, dataJson);

            log.info("[MqttSendService] [sendMessage] {} : {}", sendTopic, dataJson);

        } catch (JsonProcessingException e) {
            log.error("[MqttSendService] [sendMessage] {} : {}", e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
