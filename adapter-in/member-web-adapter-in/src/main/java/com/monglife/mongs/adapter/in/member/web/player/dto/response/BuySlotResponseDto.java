package com.monglife.mongs.adapter.in.member.web.player.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BuySlotResponseDto {

    private Long accountId;

    private Integer slotCount;

    private Integer starPoint;

    @Builder
    public BuySlotResponseDto(Long accountId, Integer slotCount, Integer starPoint) {
        this.accountId = accountId;
        this.slotCount = slotCount;
        this.starPoint = starPoint;
    }
}
