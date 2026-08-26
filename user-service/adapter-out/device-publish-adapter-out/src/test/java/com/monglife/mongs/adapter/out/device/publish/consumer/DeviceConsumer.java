package com.monglife.mongs.adapter.out.device.publish.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.mqtt.annotation.MqttConsumer;
import com.monglife.module.mqtt.annotation.MqttMapping;
import com.monglife.module.mqtt.annotation.MqttPayload;
import com.monglife.mongs.adapter.out.device.publish.dto.response.DevicePublishDto;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Mqtt Consume Component
 * @hidden
 */
@MqttConsumer
public class DeviceConsumer {

    private final ObjectMapper objectMapper;

    private String deviceId;

    private DevicePublishDto devicePublishDto;

    private CountDownLatch countDownLatch;

    /**
     * 구 토픽 수신 횟수.
     * 구버전 앱이 아직 구독 중인 토픽이라, 여기로 무언가 나가면 그 기기들의 잔액이 오염된다.
     */
    private final AtomicInteger legacyTopicReceiveCount = new AtomicInteger();

    public DeviceConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @MqttMapping("/device/{id}/step/restore")
    public void consumeRestoreWalkingCount(@PathVariable("id") String id, @MqttPayload ResponseDto<Map<String, Objects>> payload) {

        // mqtt library 는 제네릭 타입 Payload 를 변환할 수 없음 (직접 변환)
        DevicePublishDto devicePublishDto = objectMapper.convertValue(payload.getResult(), DevicePublishDto.class);

        if (devicePublishDto != null && countDownLatch != null && id.equals(deviceId)) {
            BeanUtils.copyProperties(devicePublishDto, this.devicePublishDto);
            countDownLatch.countDown();
        }
    }

    @MqttMapping("/device/{id}")
    public void consumeLegacyDevice(@PathVariable("id") String id, @MqttPayload ResponseDto<Map<String, Objects>> payload) {
        if (id.equals(deviceId)) {
            legacyTopicReceiveCount.incrementAndGet();
        }
    }

    public int getLegacyTopicReceiveCount() {
        return legacyTopicReceiveCount.get();
    }

    public void reset(String deviceId, DevicePublishDto devicePublishDto, CountDownLatch countDownLatch) {
        this.deviceId = deviceId;
        this.devicePublishDto = devicePublishDto;
        this.countDownLatch = countDownLatch;
        this.legacyTopicReceiveCount.set(0);
    }
}
