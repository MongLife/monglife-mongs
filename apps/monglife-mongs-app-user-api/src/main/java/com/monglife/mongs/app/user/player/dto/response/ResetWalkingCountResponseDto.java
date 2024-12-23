package com.monglife.mongs.app.user.player.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResetWalkingCountResponseDto {

    private Integer consumeWalkingCount;

    private Integer walkingCount;

    @Builder
    public ResetWalkingCountResponseDto(Integer consumeWalkingCount, Integer walkingCount) {
        this.consumeWalkingCount = consumeWalkingCount;
        this.walkingCount = walkingCount;
    }
}
