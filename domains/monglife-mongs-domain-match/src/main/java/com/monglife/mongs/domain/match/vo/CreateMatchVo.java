package com.monglife.mongs.domain.match.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CreateMatchVo {

    private final String playerId;

    private final String deviceId;

    private final Long accountId;

    private final Long mongId;

    private final String mongTypeCode;

    private final Double weight;

    private final Double strength;

    private final Double fatigue;

    private final Boolean isBot;


    @Builder
    public CreateMatchVo(String playerId, String deviceId, Long accountId, Long mongId, String mongTypeCode, Double weight, Double strength, Double fatigue, Boolean isBot) {
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
