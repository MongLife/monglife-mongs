package com.monglife.mongs.app.user.player.service;

import com.monglife.mongs.app.user.player.dto.etc.GetPlayerDto;
import com.monglife.mongs.domain.member.dto.etc.GetMemberDto;
import com.monglife.mongs.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final MemberService memberService;

    @Transactional(readOnly = true)
    public GetPlayerDto getPlayer(Long accountId) {

        GetMemberDto getMemberDto = memberService.getMember(accountId);

        return GetPlayerDto.builder()
                .accountId(getMemberDto.getAccountId())
                .slotCount(getMemberDto.getSlotCount())
                .starPoint(getMemberDto.getStarPoint())
                .walkingCount(getMemberDto.getWalkingCount())
                .build();
    }

    @Transactional
    public void buySlot(Long accountId) {
        memberService.increaseSlot(accountId);
    }

    @Transactional
    public void chargeStarPoint(Long accountId, Integer starPoint) {
        memberService.increaseStarPoint(accountId, starPoint);
    }

    @Transactional
    public void exchangeStarPoint(Long accountId, Long mongId, Integer starPoint) {
        memberService.decreaseStarPoint(accountId, starPoint);
    }

    @Transactional
    public void chargeWalkingCount(Long accountId, Integer walkingCount) {
        memberService.increaseWalkingCount(accountId, walkingCount);
    }

    @Transactional
    public void exchangeWalkingCount(Long accountId, Long mongId, Integer walkingCount) {
        memberService.decreaseWalkingCount(accountId, walkingCount);
    }
}
