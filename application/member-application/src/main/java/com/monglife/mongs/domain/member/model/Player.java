package com.monglife.mongs.domain.member.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Player {

    private final Long accountId;

    private final Integer slotCount;

    private final Integer starPoint;

    @Builder
    public Player(Long accountId, Integer slotCount, Integer starPoint) {
        this.accountId = accountId;
        this.slotCount = slotCount;
        this.starPoint = starPoint;
    }
}
