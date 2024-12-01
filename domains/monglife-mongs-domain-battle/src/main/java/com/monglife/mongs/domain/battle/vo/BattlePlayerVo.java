package com.monglife.mongs.domain.battle.vo;

import com.monglife.mongs.domain.battle.entity.BattlePlayerEntity;
import com.monglife.mongs.domain.battle.enums.BattleRoundCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BattlePlayerVo {

    private final String playerId;

    private final String deviceId;

    private final String mongTypeCode;

    private final Double hp;

    private final BattleRoundCode roundCode;

    public static BattlePlayerVo of(BattlePlayerEntity battlePlayerEntity, BattleRoundCode roundCode) {
        return BattlePlayerVo.builder()
                .playerId(battlePlayerEntity.getPlayerId())
                .deviceId(battlePlayerEntity.getDeviceId())
                .mongTypeCode(battlePlayerEntity.getMongTypeCode())
                .hp(battlePlayerEntity.getHp())
                .roundCode(roundCode)
                .build();
    }
}
