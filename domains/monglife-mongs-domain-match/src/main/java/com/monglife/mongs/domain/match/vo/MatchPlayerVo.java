package com.monglife.mongs.domain.match.vo;

import com.monglife.mongs.domain.match.entity.MatchPlayerEntity;
import com.monglife.mongs.domain.match.enums.MatchRoundCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MatchPlayerVo {

    private final String playerId;

    private final String deviceId;

    private final String mongTypeCode;

    private final Double hp;

    private final MatchRoundCode roundCode;

    @Builder
    public MatchPlayerVo(String playerId, String deviceId, String mongTypeCode, Double hp, MatchRoundCode roundCode) {
        this.playerId = playerId;
        this.deviceId = deviceId;
        this.mongTypeCode = mongTypeCode;
        this.hp = hp;
        this.roundCode = roundCode;
    }

    public static MatchPlayerVo of(MatchPlayerEntity matchPlayerEntity) {
        return MatchPlayerVo.builder()
                .playerId(matchPlayerEntity.getPlayerId())
                .deviceId(matchPlayerEntity.getDeviceId())
                .mongTypeCode(matchPlayerEntity.getMongTypeCode())
                .hp(matchPlayerEntity.getHp() / MatchPlayerEntity.MAX_HP * 100)
                .roundCode(matchPlayerEntity.getRoundCode())
                .build();
    }
}
