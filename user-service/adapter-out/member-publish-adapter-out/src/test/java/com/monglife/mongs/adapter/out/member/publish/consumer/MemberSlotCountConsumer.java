package com.monglife.mongs.adapter.out.member.publish.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.mqtt.annotation.MqttConsumer;
import com.monglife.module.mqtt.annotation.MqttMapping;
import com.monglife.module.mqtt.annotation.MqttPayload;
import com.monglife.mongs.adapter.out.member.publish.dto.response.MemberSlotCountPublishDto;
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
public class MemberSlotCountConsumer {

    private final ObjectMapper objectMapper;

    private Long accountId;

    private MemberSlotCountPublishDto memberSlotCountPublishDto;

    private CountDownLatch countDownLatch;

    public MemberSlotCountConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @MqttMapping("/member/slotCount/{accountId}")
    public void consumeMemberSlotCount(@PathVariable("accountId") Long accountId, @MqttPayload ResponseDto<Map<String, Objects>> payload) {

        // mqtt library 는 제네릭 타입 Payload 를 변환할 수 없음 (직접 변환)
        MemberSlotCountPublishDto memberSlotCountPublishDto = objectMapper.convertValue(payload.getResult(), MemberSlotCountPublishDto.class);

        if (memberSlotCountPublishDto != null && this.countDownLatch != null && this.accountId.equals(accountId)) {
            BeanUtils.copyProperties(memberSlotCountPublishDto, this.memberSlotCountPublishDto);
            this.countDownLatch.countDown();
        }
    }

    public void reset(Long accountId, MemberSlotCountPublishDto memberSlotCountPublishDto, CountDownLatch countDownLatch) {
        this.accountId = accountId;
        this.memberSlotCountPublishDto = memberSlotCountPublishDto;
        this.countDownLatch = countDownLatch;
    }
}
