package com.monglife.mongs.app.user.player.service;

import com.monglife.mongs.app.user.player.dto.etc.GetPlayerDto;
import com.monglife.mongs.app.user.player.exception.AlreadyMaxSlotCountException;
import com.monglife.mongs.app.user.player.vo.PlayerStepVo;
import com.monglife.mongs.client.manager.service.ManagementService;
import com.monglife.mongs.domain.device.service.StepService;
import com.monglife.mongs.domain.device.vo.StepVo;
import com.monglife.mongs.domain.member.vo.MemberVo;
import com.monglife.mongs.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PlayerService {

    @Value("${application.service.player.price-slot}")
    private Integer PRICE_SLOT;

    @Value("${application.service.player.max-slot-count}")
    private Integer MAX_SLOT_COUNT;

    private final MemberService memberService;

    private final StepService stepService;

    private final ManagementService managementService;

    @Transactional
    public GetPlayerDto getPlayer(Long accountId) {

        MemberVo memberVo = memberService.getMember(accountId);

        return GetPlayerDto.builder()
                .accountId(memberVo.getAccountId())
                .slotCount(memberVo.getSlotCount())
                .starPoint(memberVo.getStarPoint())
                .build();
    }

    @Transactional
    public void buySlot(Long accountId) {

        MemberVo memberVo = memberService.getMember(accountId);

        if (memberVo.getSlotCount() >= MAX_SLOT_COUNT) {
            throw new AlreadyMaxSlotCountException(memberVo.getSlotCount());
        }

        memberService.decreaseStarPoint(accountId, PRICE_SLOT);

        memberService.increaseSlot(accountId);
    }

    @Transactional
    public void exchangeStarPoint(Long accountId, Long mongId, Integer starPoint) {

        memberService.decreaseStarPoint(accountId, starPoint);

        // 스타 포인트 1개당 1000 페이 포인트 적립
        Integer payPoint = starPoint * 1000;

        managementService.chargePayPoint(mongId, payPoint);
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

        // 100 걸음 당 10 페이 포인트 적립
        Integer payPoint = walkingCount / 100 * 10;

        managementService.chargePayPoint(mongId, payPoint);

        return PlayerStepVo.builder()
                .totalWalkingCount(stepVo.getTotalWalkingCount())
                .consumeWalkingCount(stepVo.getConsumeWalkingCount())
                .walkingCount(stepVo.getWalkingCount())
                .build();
    }
}
