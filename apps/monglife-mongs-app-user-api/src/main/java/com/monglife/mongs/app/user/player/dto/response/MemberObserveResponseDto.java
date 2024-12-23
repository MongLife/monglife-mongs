package com.monglife.mongs.app.user.player.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberObserveResponseDto {

    private Long accountId;

    private Integer slotCount;

    private Integer starPoint;

    @Builder
    public MemberObserveResponseDto(Long accountId, Integer slotCount, Integer starPoint) {
        this.accountId = accountId;
        this.slotCount = slotCount;
        this.starPoint = starPoint;
    }
}
