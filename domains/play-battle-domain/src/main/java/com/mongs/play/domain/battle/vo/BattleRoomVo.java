package com.mongs.play.domain.battle.vo;

import com.mongs.play.domain.battle.entity.BattleRoom;
import lombok.Builder;

import java.util.List;

@Builder
public record BattleRoomVo(
        String roomId,
        Integer round,
        List<BattlePlayerVo> battlePlayerVoList
) {
    public static BattleRoomVo of(BattleRoom battleRoom) {
        return BattleRoomVo.builder()
                .roomId(battleRoom.getRoomId())
                .round(battleRoom.getRound())
                .battlePlayerVoList(battleRoom.getBattlePlayerList().stream().map(BattlePlayerVo::of).toList())
                .build();
    }
}
