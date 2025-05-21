package com.monglife.mongs.adapter.out.member.publish.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.mqtt.annotation.MqttConsumer;
import com.monglife.module.mqtt.annotation.MqttMapping;
import com.monglife.module.mqtt.annotation.MqttPayload;
import com.monglife.mongs.adapter.out.member.publish.dto.response.MemberSlotCountPublishDto;
import com.monglife.mongs.adapter.out.member.publish.dto.response.MemberStarPointPublishDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
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

    private final Map<Mapping, VerifyMessage<?>> verifyMessageMap;

    public Consumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
        this.verifyMessageMap = new HashMap<>();
    }

    @MqttMapping("/player/starPoint/{accountId}")
    public void consumeMemberStarPoint(@PathVariable("accountId") String accountId, @MqttPayload ResponseDto<Map<String, Objects>> payload) {

        VerifyMessage<?> verifyMessage = verifyMessageMap.get(Mapping.STAR_POINT);

        // mqtt library 는 제네릭 타입 Payload 를 변환할 수 없음 (직접 변환)
        MemberStarPointPublishDto memberStarPointPublishDto = objectMapper.convertValue(payload.getResult(), MemberStarPointPublishDto.class);

        if (verifyMessage != null && memberStarPointPublishDto != null && verifyMessage.getCountDownLatch() != null && accountId.equals(verifyMessage.getTopic())) {
            MemberStarPointPublishDto expected = (MemberStarPointPublishDto) verifyMessage.getPayload();
            expected.setAccountId(memberStarPointPublishDto.getAccountId());
            expected.setStarPoint(memberStarPointPublishDto.getStarPoint());
            verifyMessage.getCountDownLatch().countDown();
        }
    }

    @MqttMapping("/player/slotCount/{accountId}")
    public void consumeMemberSlotCount(@PathVariable("accountId") String accountId, @MqttPayload ResponseDto<Map<String, Objects>> payload) {

        VerifyMessage<?> verifyMessage = verifyMessageMap.get(Mapping.SLOT_COUNT);

        // mqtt library 는 제네릭 타입 Payload 를 변환할 수 없음 (직접 변환)
        MemberSlotCountPublishDto memberSlotCountPublishDto = objectMapper.convertValue(payload.getResult(), MemberSlotCountPublishDto.class);

        if (verifyMessage != null && memberSlotCountPublishDto != null && verifyMessage.getCountDownLatch() != null && accountId.equals(verifyMessage.getTopic())) {
            MemberSlotCountPublishDto expected = (MemberSlotCountPublishDto) verifyMessage.getPayload();
            expected.setAccountId(memberSlotCountPublishDto.getAccountId());
            expected.setSlotCount(memberSlotCountPublishDto.getSlotCount());
            verifyMessage.getCountDownLatch().countDown();
        }
    }

    public void resetConsumeStarPoint(Long accountId, MemberStarPointPublishDto payload, CountDownLatch countDownLatch) {
        this.verifyMessageMap.put(Mapping.STAR_POINT, new VerifyMessage<>(String.valueOf(accountId), payload, countDownLatch));
    }

    public void resetConsumeSlotCount(Long accountId, MemberSlotCountPublishDto payload, CountDownLatch countDownLatch) {
        this.verifyMessageMap.put(Mapping.SLOT_COUNT, new VerifyMessage<>(String.valueOf(accountId), payload, countDownLatch));
    }

    private enum Mapping {
        STAR_POINT,
        SLOT_COUNT,
    }
}
