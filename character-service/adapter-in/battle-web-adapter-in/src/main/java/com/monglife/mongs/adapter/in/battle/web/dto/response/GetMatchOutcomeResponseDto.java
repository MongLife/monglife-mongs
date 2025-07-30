package com.monglife.mongs.adapter.in.battle.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetMatchOutcomeResponseDto {

    private Integer rewardPayPoint;

    private Integer battingPayPoint;

    @Builder
    public GetMatchOutcomeResponseDto(Integer rewardPayPoint, Integer battingPayPoint) {
        this.rewardPayPoint = rewardPayPoint;
        this.battingPayPoint = battingPayPoint;
    }
}
