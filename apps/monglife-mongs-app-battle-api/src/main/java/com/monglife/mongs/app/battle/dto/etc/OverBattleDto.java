package com.monglife.mongs.app.battle.dto.etc;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OverBattleDto {

    private String playerId;

    private Long mongId;

    private String mongCode;

    @Builder
    public OverBattleDto(String playerId, Long mongId, String mongCode) {
        this.playerId = playerId;
        this.mongId = mongId;
        this.mongCode = mongCode;
    }
}
