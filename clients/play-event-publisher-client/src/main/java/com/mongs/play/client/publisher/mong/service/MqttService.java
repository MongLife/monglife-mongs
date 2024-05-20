package com.mongs.play.client.publisher.mong.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.play.client.publisher.mong.client.MqttClient;
import com.mongs.play.client.publisher.mong.code.PublishCode;
import com.mongs.play.client.publisher.mong.dto.res.BasicPublishDto;
import com.mongs.play.client.publisher.mong.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MqttService {

    private final MqttClient mqttClient;
    private final ObjectMapper objectMapper;

    @Value("${application.mqtt.topic.mong_data}")
    private String TOPIC_FILTER;

    private void send(PublishCode publishCode, Long accountId, Object data) {
        try {
            mqttClient.sendToMqtt(TOPIC_FILTER + accountId, objectMapper.writeValueAsString(
                    BasicPublishDto.builder()
                            .code(publishCode)
                            .data(data)
                            .build()));
        } catch (JsonProcessingException ignored) {}
    }

    public void sendMongCode(Long accountId, Long mongId, String mongCode) {
        this.send(PublishCode.MONG_CODE, accountId, MongCodeVo.builder()
                .mongId(mongId)
                .mongCode(mongCode)
                .build());
    }

    public void sendMongState(Long accountId, Long mongId, String stateCode) {
        this.send(PublishCode.MONG_STATE, accountId, MongStateVo.builder()
                .mongId(mongId)
                .stateCode(stateCode)
                .build());
    }

    public void sendMongShift(Long accountId, Long mongId, String shiftCode) {
        this.send(PublishCode.MONG_SHIFT, accountId, MongShiftVo.builder()
                .mongId(mongId)
                .shiftCode(shiftCode)
                .build());
    }

    public void sendMongPoopCount(Long accountId, Long mongId, Integer poopCount) {
        this.send(PublishCode.MONG_POOP_COUNT, accountId, MongPoopCountVo.builder()
                .mongId(mongId)
                .poopCount(poopCount)
                .build());
    }

    public void sendMongPayPoint(Long accountId, Long mongId, Integer payPoint) {
        this.send(PublishCode.MONG_PAY_POINT, accountId, MongPayPointVo.builder()
                .mongId(mongId)
                .payPoint(payPoint)
                .build());
    }

    public void sendMongIsSleeping(Long accountId, Long mongId, Boolean isSleeping) {
        this.send(PublishCode.MONG_IS_SLEEPING, accountId, MongIsSleepingVo.builder()
                .mongId(mongId)
                .isSleeping(isSleeping)
                .build());
    }

    public void sendMongStatus(Long accountId, Long mongId, Double weight, Double strength, Double satiety, Double healthy, Double sleep, Double exp) {
        this.send(PublishCode.MONG_STATUS, accountId, MongStatusVo.builder()
                .mongId(mongId)
                .weight(weight)
                .strength(strength)
                .satiety(satiety)
                .healthy(healthy)
                .sleep(sleep)
                .exp(exp)
                .build());
    }

    public void sendMemberStarPoint(Long accountId, Integer starPoint) {
        this.send(PublishCode.MEMBER_STAR_POINT, accountId, MemberStarPointVo.builder()
                .accountId(accountId)
                .starPoint(starPoint)
                .build());
    }
}
