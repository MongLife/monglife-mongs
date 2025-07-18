package com.monglife.mongs.adapter.in.device.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetStepResponseDto {

    private Integer consumeWalkingCount;

    private Integer walkingCount;

    @Builder
    public GetStepResponseDto(Integer consumeWalkingCount, Integer walkingCount) {
        this.consumeWalkingCount = consumeWalkingCount;
        this.walkingCount = walkingCount;
    }
}
