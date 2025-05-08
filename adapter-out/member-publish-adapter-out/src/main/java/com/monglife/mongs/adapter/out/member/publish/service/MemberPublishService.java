package com.monglife.mongs.adapter.out.member.publish.service;

import com.monglife.mongs.adapter.out.member.publish.client.MemberPublishClient;
import com.monglife.mongs.adapter.out.member.publish.dto.response.MemberSlotCountPublishDto;
import com.monglife.mongs.adapter.out.member.publish.dto.response.MemberStarPointPublishDto;
import com.monglife.mongs.application.member.port.out.MemberPublishPort;
import com.monglife.mongs.domain.model.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberPublishService implements MemberPublishPort {

    private final MemberPublishClient memberPublishClient;

    /**
     * 회원 스타 포인트 변동 사항 비동기 응답
     * @param player 플레이어 도메인 객체
     */
    @Override
    public void publishStarPointPort(Player player) {

        MemberStarPointPublishDto memberStarPointPublishDto = MemberStarPointPublishDto.builder()
                .accountId(player.getAccountId())
                .starPoint(player.getStarPoint())
                .build();

        memberPublishClient.publishMemberStarPoint(memberStarPointPublishDto);

    }

    /**
     * 회원 슬롯 수 변동 사항 비동기 응답
     * @param player 플레이어 도메인 객체
     */
    @Override
    public void publishSlotCountPort(Player player) {

        MemberSlotCountPublishDto memberSlotCountPublishDto = MemberSlotCountPublishDto.builder()
                .accountId(player.getAccountId())
                .slotCount(player.getSlotCount())
                .build();

        memberPublishClient.publishMemberSlotCount(memberSlotCountPublishDto);
    }
}
