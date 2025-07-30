package com.monglife.mongs.adapter.in.battle.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetWinMatchPlayerResponseDto {

    private String playerId;

    private String mongCode;

    private String mongName;

    private String name;

    private Integer rewardPayPoint;

    @Builder
    public GetWinMatchPlayerResponseDto(String mongCode, String playerId, String mongName, String name, Integer rewardPayPoint) {
        this.mongCode = mongCode;
        this.playerId = playerId;
        this.mongName = mongName;
        this.name = name;
        this.rewardPayPoint = rewardPayPoint;
    }
}
