package com.monglife.mongs.domain.battle.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OverBattleVo {

    private String playerId;

    private Long mongId;

    private String mongTypeCode;

    @Builder
    public OverBattleVo(String playerId, Long mongId, String mongTypeCode) {
        this.playerId = playerId;
        this.mongId = mongId;
        this.mongTypeCode = mongTypeCode;
    }
}
