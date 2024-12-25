package com.monglife.mongs.app.user.player.service;

import com.monglife.mongs.app.user.player.dto.etc.GetPlayerDto;
import com.monglife.mongs.app.user.player.vo.PlayerStepVo;
import com.monglife.mongs.domain.member.dto.etc.GetMemberDto;
import com.monglife.mongs.domain.member.service.StepService;
import com.monglife.mongs.domain.member.vo.StepVo;
import com.monglife.mongs.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final MemberService memberService;

    private final StepService stepService;

    @Transactional
    public GetPlayerDto getPlayer(Long accountId) {

        GetMemberDto getMemberDto = memberService.getMember(accountId);

        return GetPlayerDto.builder()
                .accountId(getMemberDto.getAccountId())
                .slotCount(getMemberDto.getSlotCount())
                .starPoint(getMemberDto.getStarPoint())
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
    public PlayerStepVo syncWalkingCount(String deviceId, Integer totalWalkingCount, LocalDateTime deviceBootedDt) {

        StepVo stepVo = stepService.updateWalkingCount(deviceId, totalWalkingCount, deviceBootedDt);

        return PlayerStepVo.builder()
                .totalWalkingCount(stepVo.getTotalWalkingCount())
                .consumeWalkingCount(stepVo.getConsumeWalkingCount())
                .walkingCount(stepVo.getWalkingCount())
                .build();
    }

    @Transactional
    public PlayerStepVo exchangeWalkingCount(String deviceId, Long mongId, Integer totalWalkingCount, Integer walkingCount, LocalDateTime deviceBootedDt) {

        StepVo stepVo = stepService.decreaseWalkingCount(deviceId, totalWalkingCount, walkingCount, deviceBootedDt);

        // TODO: 몽 페이포인트 증가 로직 추가

        return PlayerStepVo.builder()
                .totalWalkingCount(stepVo.getTotalWalkingCount())
                .consumeWalkingCount(stepVo.getConsumeWalkingCount())
                .walkingCount(stepVo.getWalkingCount())
                .build();
    }
}
