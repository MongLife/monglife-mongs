package com.monglife.mongs.app.user.step.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExchangeWalkingCountResponseDto {

    private Integer consumeWalkingCount;

    private Integer walkingCount;

    @Builder
    public ExchangeWalkingCountResponseDto(Integer consumeWalkingCount, Integer walkingCount) {
        this.consumeWalkingCount = consumeWalkingCount;
        this.walkingCount = walkingCount;
    }
}
