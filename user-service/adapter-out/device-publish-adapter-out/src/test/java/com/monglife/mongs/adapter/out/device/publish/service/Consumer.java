package com.monglife.mongs.adapter.out.device.publish.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.mqtt.annotation.MqttConsumer;
import com.monglife.module.mqtt.annotation.MqttMapping;
import com.monglife.module.mqtt.annotation.MqttPayload;
import com.monglife.mongs.adapter.out.device.publish.dto.response.DevicePublishDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * Mqtt Consume Component
 * @hidden
 */
@MqttConsumer
public class Consumer {

    private final ObjectMapper objectMapper;

    private String deviceId;

    private DevicePublishDto devicePublishDto;

    private CountDownLatch countDownLatch;

    public Consumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @MqttMapping("/device/{id}")
    public void consumeDevice(@PathVariable("id") String id, @MqttPayload ResponseDto<Map<String, Objects>> payload) {

        // mqtt library 는 제네릭 타입 Payload 를 변환할 수 없음 (직접 변환)
        DevicePublishDto devicePublishDto = objectMapper.convertValue(payload.getResult(), DevicePublishDto.class);

        if (devicePublishDto != null && countDownLatch != null && id.equals(deviceId)) {
            this.devicePublishDto.setDeviceId(devicePublishDto.getDeviceId());
            this.devicePublishDto.setWalkingCount(devicePublishDto.getWalkingCount());
            this.devicePublishDto.setConsumeWalkingCount(devicePublishDto.getConsumeWalkingCount());
            countDownLatch.countDown();
        }
    }

    public void reset(String deviceId, DevicePublishDto devicePublishDto, CountDownLatch countDownLatch) {
        this.deviceId = deviceId;
        this.devicePublishDto = devicePublishDto;
        this.countDownLatch = countDownLatch;
    }
}
