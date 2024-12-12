package com.monglife.mongs.domain.member.dto.event;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberObserveEvent {

    private final Long accountId;

    private final Integer slotCount;

    private final Integer starPoint;

    private final Integer walkingCount;

    private final Boolean isActive;

    @Builder
    public MemberObserveEvent(Long accountId, Integer slotCount, Integer starPoint, Integer walkingCount, Boolean isActive) {
        this.accountId = accountId;
        this.slotCount = slotCount;
        this.starPoint = starPoint;
        this.walkingCount = walkingCount;
        this.isActive = isActive;
    }
}
