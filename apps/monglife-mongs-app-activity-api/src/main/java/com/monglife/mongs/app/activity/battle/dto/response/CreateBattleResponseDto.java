package com.monglife.mongs.app.activity.battle.dto.response;

import com.monglife.mongs.app.activity.battle.vo.BattlePlayerVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CreateBattleResponseDto {

    private Long roomId;

    private Set<BattlePlayerVo> battlePlayers;

    @Builder
    public CreateBattleResponseDto(Long roomId, Set<BattlePlayerVo> battlePlayers) {
        this.roomId = roomId;
        this.battlePlayers = battlePlayers;
    }
}
