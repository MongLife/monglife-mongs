package com.monglife.mongs.adapter.out.battle.publish.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MatchEndPublishDto {

    private Long matchId;

    private String playerId;

    private String mongName;

    private String mongTypeCode;

    private String mongTypeName;

    @Builder
    public MatchEndPublishDto(Long matchId, String playerId, String mongName, String mongTypeCode, String mongTypeName) {
        this.matchId = matchId;
        this.playerId = playerId;
        this.mongName = mongName;
        this.mongTypeCode = mongTypeCode;
        this.mongTypeName = mongTypeName;
    }
}
