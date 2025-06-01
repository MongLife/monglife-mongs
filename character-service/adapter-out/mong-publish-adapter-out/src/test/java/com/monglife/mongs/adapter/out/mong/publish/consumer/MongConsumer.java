package com.monglife.mongs.adapter.out.mong.publish.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.mqtt.annotation.MqttConsumer;
import com.monglife.module.mqtt.annotation.MqttMapping;
import com.monglife.module.mqtt.annotation.MqttPayload;
import com.monglife.mongs.adapter.out.mong.publish.dto.response.MongPublishDto;
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
public class MongConsumer {

    private final ObjectMapper objectMapper;

    private Long mongId;

    private MongPublishDto mongPublishDto;

    private CountDownLatch countDownLatch;

    public MongConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @MqttMapping("/mong/management/{mongId}")
    public void consumeMemberSlotCount(@PathVariable("mongId") Long mongId, @MqttPayload ResponseDto<Map<String, Objects>> payload) {

        // mqtt library 는 제네릭 타입 Payload 를 변환할 수 없음 (직접 변환)
        MongPublishDto mongPublishDto = objectMapper.convertValue(payload.getResult(), MongPublishDto.class);

        if (mongPublishDto != null && this.countDownLatch != null && this.mongId.equals(mongId)) {
            BeanUtils.copyProperties(mongPublishDto, this.mongPublishDto);
            this.countDownLatch.countDown();
        }
    }

    public void reset(Long mongId, MongPublishDto mongPublishDto, CountDownLatch countDownLatch) {
        this.mongId = mongId;
        this.mongPublishDto = mongPublishDto;
        this.countDownLatch = countDownLatch;
    }
}
