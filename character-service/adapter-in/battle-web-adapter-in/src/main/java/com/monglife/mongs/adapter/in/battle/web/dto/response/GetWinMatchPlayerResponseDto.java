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

    private String mongTypeCode;

    private String mongTypeName;

    private String mongName;

    @Builder
    public GetWinMatchPlayerResponseDto(String mongTypeCode, String playerId, String mongTypeName, String mongName) {
        this.mongTypeCode = mongTypeCode;
        this.playerId = playerId;
        this.mongTypeName = mongTypeName;
        this.mongName = mongName;
    }
}
