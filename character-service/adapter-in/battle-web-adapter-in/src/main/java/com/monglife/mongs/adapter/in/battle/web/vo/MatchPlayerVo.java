package com.monglife.mongs.adapter.in.battle.web.vo;

import com.monglife.mongs.domain.battle.enums.MatchRoundCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MatchPlayerVo {

    private final String playerId;

    private final String deviceId;

    private final String mongCode;

    private final String mongName;

    private final String name;

    private final Double hp;

    private final MatchRoundCode roundCode;

    @Builder
    public MatchPlayerVo(String playerId, String deviceId, String mongCode, String mongName, String name, Double hp, MatchRoundCode roundCode) {
        this.playerId = playerId;
        this.deviceId = deviceId;
        this.mongCode = mongCode;
        this.mongName = mongName;
        this.name = name;
        this.hp = hp;
        this.roundCode = roundCode;
    }
}
