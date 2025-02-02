package com.monglife.mongs.app.activity.battle.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetBattleRewardResponseDto {

    private Integer payPoint;

    @Builder
    public GetBattleRewardResponseDto(Integer payPoint) {
        this.payPoint = payPoint;
    }
}
