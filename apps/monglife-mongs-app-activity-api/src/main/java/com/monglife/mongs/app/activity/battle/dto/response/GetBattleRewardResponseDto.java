package com.monglife.mongs.app.activity.battle.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetBattleRewardResponseDto {

    private Integer rewardPayPoint;

    private Integer bettingPayPoint;

    @Builder
    public GetBattleRewardResponseDto(Integer rewardPayPoint, Integer bettingPayPoint) {
        this.rewardPayPoint = rewardPayPoint;
        this.bettingPayPoint = bettingPayPoint;
    }
}
