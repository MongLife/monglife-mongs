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

    private Integer walkingCount;

    @Builder
    public MemberObserveResponseDto(Long accountId, Integer slotCount, Integer starPoint, Integer walkingCount) {
        this.accountId = accountId;
        this.slotCount = slotCount;
        this.starPoint = starPoint;
        this.walkingCount = walkingCount;
    }
}
