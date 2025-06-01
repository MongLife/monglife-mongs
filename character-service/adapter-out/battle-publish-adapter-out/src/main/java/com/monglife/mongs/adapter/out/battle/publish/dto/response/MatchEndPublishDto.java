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

    private String name;

    private String mongCode;

    private String mongName;

    @Builder
    public MatchEndPublishDto(Long matchId, String playerId, String name, String mongCode, String mongName) {
        this.matchId = matchId;
        this.playerId = playerId;
        this.name = name;
        this.mongCode = mongCode;
        this.mongName = mongName;
    }
}
