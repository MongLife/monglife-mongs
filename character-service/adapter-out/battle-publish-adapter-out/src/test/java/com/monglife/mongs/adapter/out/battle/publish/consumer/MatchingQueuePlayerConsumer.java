package com.monglife.mongs.adapter.out.battle.publish.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.mqtt.annotation.MqttConsumer;
import com.monglife.module.mqtt.annotation.MqttMapping;
import com.monglife.module.mqtt.annotation.MqttPayload;
import com.monglife.mongs.adapter.out.battle.publish.dto.response.MatchingQueuePlayerFailPublishDto;
import com.monglife.mongs.adapter.out.battle.publish.dto.response.MatchingQueuePlayerPublishDto;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * Mqtt Consume Component
 */
@MqttConsumer
public class MatchingQueuePlayerConsumer {

    private final ObjectMapper objectMapper;

    private List<String> deviceIds;

    private MatchingQueuePlayerPublishDto matchingQueuePlayerPublishDto;

    private String deviceId;

    private MatchingQueuePlayerFailPublishDto matchingQueuePlayerFailPublishDto;

    private CountDownLatch countDownLatch;

    public MatchingQueuePlayerConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @MqttMapping("/battle/queue/{deviceId}")
    public void consumeMatchingQueuePlayer(@PathVariable("deviceId") String deviceId, @MqttPayload ResponseDto<Map<String, Objects>> payload) {

        // mqtt library 는 제네릭 타입 Payload 를 변환할 수 없음 (직접 변환)
        MatchingQueuePlayerPublishDto matchingQueuePlayerPublishDto = objectMapper.convertValue(payload.getResult(), MatchingQueuePlayerPublishDto.class);
        MatchingQueuePlayerFailPublishDto matchingQueuePlayerFailPublishDto = objectMapper.convertValue(payload.getResult(), MatchingQueuePlayerFailPublishDto.class);

        if (matchingQueuePlayerPublishDto != null && this.countDownLatch != null && this.matchingQueuePlayerPublishDto != null) {
            BeanUtils.copyProperties(matchingQueuePlayerPublishDto, this.matchingQueuePlayerPublishDto);
            this.deviceIds.add(deviceId);
            this.countDownLatch.countDown();
        } else if (matchingQueuePlayerFailPublishDto != null && this.countDownLatch != null && this.matchingQueuePlayerFailPublishDto != null && this.deviceId.equals(deviceId)) {
            BeanUtils.copyProperties(matchingQueuePlayerFailPublishDto, this.matchingQueuePlayerFailPublishDto);
            this.countDownLatch.countDown();
        }
    }

    public void reset(List<String> deviceIds, MatchingQueuePlayerPublishDto matchingQueuePlayerPublishDto, CountDownLatch countDownLatch) {
        this.deviceIds = deviceIds;
        this.matchingQueuePlayerPublishDto = matchingQueuePlayerPublishDto;
        this.deviceId = null;
        this.matchingQueuePlayerFailPublishDto = null;
        this.countDownLatch = countDownLatch;
    }

    public void reset(String deviceId, MatchingQueuePlayerFailPublishDto matchingQueuePlayerFailPublishDto, CountDownLatch countDownLatch) {
        this.deviceIds = null;
        this.matchingQueuePlayerPublishDto = null;
        this.deviceId = deviceId;
        this.matchingQueuePlayerFailPublishDto = matchingQueuePlayerFailPublishDto;
        this.countDownLatch = countDownLatch;
    }
}
