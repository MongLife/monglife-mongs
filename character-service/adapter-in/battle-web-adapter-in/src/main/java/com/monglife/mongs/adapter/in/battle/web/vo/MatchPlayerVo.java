package com.monglife.mongs.adapter.in.battle.web.vo;

import com.monglife.mongs.domain.battle.enums.MatchRoundCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MatchPlayerVo {

    private final String playerId;

    private final String deviceId;

    private final String mongTypeCode;

    private final String mongTypeName;

    private final String mongName;

    private final Double hp;

    private final MatchRoundCode roundCode;

    @Builder
    public MatchPlayerVo(String playerId, String deviceId, String mongTypeCode, String mongTypeName, String mongName, Double hp, MatchRoundCode roundCode) {
        this.playerId = playerId;
        this.deviceId = deviceId;
        this.mongTypeCode = mongTypeCode;
        this.mongTypeName = mongTypeName;
        this.mongName = mongName;
        this.hp = hp;
        this.roundCode = roundCode;
    }
}
