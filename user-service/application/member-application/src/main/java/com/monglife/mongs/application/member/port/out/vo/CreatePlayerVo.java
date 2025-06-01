package com.monglife.mongs.application.member.port.out.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreatePlayerVo {

    private final Long accountId;

    private final Integer slotCount;

    private final Integer starPoint;

    @Builder
    public CreatePlayerVo(Long accountId, Integer slotCount, Integer starPoint) {
        this.accountId = accountId;
        this.slotCount = slotCount;
        this.starPoint = starPoint;
    }
}
