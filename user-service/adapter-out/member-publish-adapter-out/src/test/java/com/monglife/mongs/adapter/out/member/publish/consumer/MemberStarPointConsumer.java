package com.monglife.mongs.adapter.out.member.publish.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.mqtt.annotation.MqttConsumer;
import com.monglife.module.mqtt.annotation.MqttMapping;
import com.monglife.module.mqtt.annotation.MqttPayload;
import com.monglife.mongs.adapter.out.member.publish.dto.response.MemberStarPointPublishDto;
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
public class MemberStarPointConsumer {

    private final ObjectMapper objectMapper;

    private Long accountId;

    private MemberStarPointPublishDto memberStarPointPublishDto;

    private CountDownLatch countDownLatch;

    public MemberStarPointConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @MqttMapping("/member/starPoint/{accountId}")
    public void consumeMemberStarPoint(@PathVariable("accountId") Long accountId, @MqttPayload ResponseDto<Map<String, Objects>> payload) {

        // mqtt library 는 제네릭 타입 Payload 를 변환할 수 없음 (직접 변환)
        MemberStarPointPublishDto memberStarPointPublishDto = objectMapper.convertValue(payload.getResult(), MemberStarPointPublishDto.class);

        if (memberStarPointPublishDto != null && this.countDownLatch != null && this.accountId.equals(accountId)) {
            BeanUtils.copyProperties(memberStarPointPublishDto, this.memberStarPointPublishDto);
            this.countDownLatch.countDown();
        }
    }

    public void reset(Long accountId, MemberStarPointPublishDto memberStarPointPublishDto, CountDownLatch countDownLatch) {
        this.accountId = accountId;
        this.memberStarPointPublishDto = memberStarPointPublishDto;
        this.countDownLatch = countDownLatch;
    }
}
