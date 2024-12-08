package com.monglife.mongs.domain.battle.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateBattleVo {

    private String playerId;

    private String deviceId;

    private Long accountId;

    private Long mongId;

    private String mongTypeCode;

    private Double weight;

    private Double strength;

    private Double fatigue;

    private Boolean isBot;

    @Builder
    public CreateBattleVo(String playerId, String deviceId, Long accountId, Long mongId, String mongTypeCode, Double weight, Double strength, Double fatigue, Boolean isBot) {
        this.playerId = playerId;
        this.deviceId = deviceId;
        this.accountId = accountId;
        this.mongId = mongId;
        this.mongTypeCode = mongTypeCode;
        this.weight = weight;
        this.strength = strength;
        this.fatigue = fatigue;
        this.isBot = isBot;
    }
}
