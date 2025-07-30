package com.monglife.mongs.adapter.out.battle.publish.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.mqtt.annotation.MqttConsumer;
import com.monglife.module.mqtt.annotation.MqttMapping;
import com.monglife.module.mqtt.annotation.MqttPayload;
import com.monglife.mongs.adapter.out.battle.publish.dto.response.MatchPublishDto;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * Mqtt Consume Component
 * @hidden
 */
@MqttConsumer
public class MatchConsumer {

    private final ObjectMapper objectMapper;

    private Long matchId;

    private MatchPublishDto matchPublishDto;

    private CountDownLatch countDownLatch;

    public MatchConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @MqttMapping("/battle/match/{matchId}")
    public void consumeMatch(@PathVariable("matchId") Long matchId, @MqttPayload ResponseDto<Map<String, Objects>> payload) {

        // mqtt library 는 제네릭 타입 Payload 를 변환할 수 없음 (직접 변환)
        MatchPublishDto matchPublishDto = objectMapper.convertValue(payload.getResult(), MatchPublishDto.class);

        if (matchPublishDto != null && this.countDownLatch != null && this.matchPublishDto != null && this.matchId.equals(matchId)) {
            BeanUtils.copyProperties(matchPublishDto, this.matchPublishDto);
            this.countDownLatch.countDown();
        }
    }

    public void reset(Long matchId, MatchPublishDto matchPublishDto, CountDownLatch countDownLatch) {
        this.matchId = matchId;
        this.matchPublishDto = matchPublishDto;
        this.countDownLatch = countDownLatch;
    }
}
