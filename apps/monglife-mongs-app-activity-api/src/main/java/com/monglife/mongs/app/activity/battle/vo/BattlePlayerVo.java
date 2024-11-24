package com.monglife.mongs.app.activity.battle.vo;

import com.monglife.mongs.app.activity.battle.domain.BattlePlayerEntity;
import com.monglife.mongs.app.activity.battle.enums.BattleRoundCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BattlePlayerVo {

    private final String playerId;

    private final String deviceId;

    private final String mongCode;

    private final Double hp;

    private final BattleRoundCode roundCode;

    public static BattlePlayerVo of(BattlePlayerEntity battlePlayerEntity, BattleRoundCode roundCode) {
        return BattlePlayerVo.builder()
                .playerId(battlePlayerEntity.getPlayerId())
                .deviceId(battlePlayerEntity.getDeviceId())
                .mongCode(battlePlayerEntity.getMongCode())
                .hp(battlePlayerEntity.getHp())
                .roundCode(roundCode)
                .build();
    }
}
