package com.monglife.mongs.app.user.player.service;

import com.monglife.mongs.app.user.player.dto.etc.GetPlayerDto;
import com.monglife.mongs.app.user.player.exception.AlreadyMaxSlotCountException;
import com.monglife.mongs.client.manager.service.ManagementService;
import com.monglife.mongs.domain.member.service.MemberService;
import com.monglife.mongs.domain.member.vo.MemberVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlayerService {

    @Value("${application.service.player.price-slot}")
    private Integer PRICE_SLOT;

    @Value("${application.service.player.max-slot-count}")
    private Integer MAX_SLOT_COUNT;

    private final MemberService memberService;

    private final ManagementService managementService;

    /**
     * 플레이어 등록
     * @param accountId 계정 ID
     */
    @Transactional
    public void createPlayer(Long accountId) {
        memberService.createMember(accountId);
    }

    /**
     * 플레이어 조회
     * @param accountId 계정 ID
     * @return 플레이어 정보 Dto
     */
    @Transactional
    public GetPlayerDto getPlayer(Long accountId) {

        MemberVo memberVo = memberService.getMember(accountId);

        return GetPlayerDto.builder()
                .accountId(memberVo.getAccountId())
                .slotCount(memberVo.getSlotCount())
                .starPoint(memberVo.getStarPoint())
                .build();
    }

    /**
     * 슬롯 구매
     * @param accountId 계정 ID
     */
    @Transactional
    public void buySlot(Long accountId) {

        MemberVo memberVo = memberService.getMember(accountId);

        if (memberVo.getSlotCount() >= MAX_SLOT_COUNT) {
            throw new AlreadyMaxSlotCountException(memberVo.getSlotCount());
        }

        memberService.decreaseStarPoint(accountId, PRICE_SLOT);

        memberService.increaseSlot(accountId);
    }

    /**
     * 스타 포인트 환전
     * @param accountId 계정 ID
     * @param mongId 페이 포인트 지급할 몽 ID
     * @param starPoint 차감할 스타 포인트
     */
    @Transactional
    public void exchangeStarPoint(Long accountId, Long mongId, Integer starPoint) {

        memberService.decreaseStarPoint(accountId, starPoint);

        // 스타 포인트 1개당 1000 페이 포인트 적립
        Integer payPoint = starPoint * 1000;

        managementService.chargePayPoint(mongId, payPoint);
    }
}
