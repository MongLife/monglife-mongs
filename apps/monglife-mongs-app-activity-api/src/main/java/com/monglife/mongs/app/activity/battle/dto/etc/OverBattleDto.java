package com.monglife.mongs.app.activity.battle.dto.etc;

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

    private String mongTypeCode;

    @Builder
    public OverBattleDto(String playerId, Long mongId, String mongTypeCode) {
        this.playerId = playerId;
        this.mongId = mongId;
        this.mongTypeCode = mongTypeCode;
    }
}
