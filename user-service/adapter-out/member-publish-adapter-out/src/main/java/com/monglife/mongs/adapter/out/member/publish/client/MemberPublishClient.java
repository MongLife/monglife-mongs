package com.monglife.mongs.adapter.out.member.publish.client;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.mqtt.annotation.MqttPublish;
import com.monglife.module.mqtt.dto.MqttResponseEntity;
import com.monglife.mongs.adapter.out.member.publish.dto.response.MemberSlotCountPublishDto;
import com.monglife.mongs.adapter.out.member.publish.dto.response.MemberStarPointPublishDto;
import com.monglife.mongs.adapter.out.member.publish.enums.AdapterOutPublishMemberResponse;
import org.springframework.stereotype.Component;

@Component
public class MemberPublishClient {

    /**
     * 회원 스타 포인트 변동 사항 비동기 응답
     * @param memberStarPointPublishDto 회원 스타 포인트 변동 사항 비동기 응답 Dto
     */
    @MqttPublish("/member/starPoint/{topic}")
    public MqttResponseEntity<ResponseDto<MemberStarPointPublishDto>> publishMemberStarPoint(MemberStarPointPublishDto memberStarPointPublishDto) {

        String topic = String.valueOf(memberStarPointPublishDto.getAccountId());

        return MqttResponseEntity
                .body(AdapterOutPublishMemberResponse.MEMBER_PUBLISH_MEMBER_STAR_POINT.toResponseDto(memberStarPointPublishDto))
                .topic(topic);
    }

    /**
     * 회원 슬롯 수 변동 사항 비동기 응답
     * @param memberSlotCountPublishDto 회원 슬롯 수 변동 사항 비동기 응답 Dto
     */
    @MqttPublish("/member/slotCount/{topic}")
    public MqttResponseEntity<ResponseDto<MemberSlotCountPublishDto>> publishMemberSlotCount(MemberSlotCountPublishDto memberSlotCountPublishDto) {

        String topic = String.valueOf(memberSlotCountPublishDto.getAccountId());

        return MqttResponseEntity
                .body(AdapterOutPublishMemberResponse.MEMBER_PUBLISH_MEMBER_SLOT_COUNT.toResponseDto(memberSlotCountPublishDto))
                .topic(topic);
    }
}
