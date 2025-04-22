package com.monglife.mongs.adapter.in.device.web.step.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExchangeCurrentWalkingCountResponseDto {

    private Integer consumeWalkingCount;

    private Integer walkingCount;

    @Builder
    public ExchangeCurrentWalkingCountResponseDto(Integer consumeWalkingCount, Integer walkingCount) {
        this.consumeWalkingCount = consumeWalkingCount;
        this.walkingCount = walkingCount;
    }
}
