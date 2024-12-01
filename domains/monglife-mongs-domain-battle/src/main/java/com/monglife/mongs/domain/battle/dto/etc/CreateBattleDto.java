package com.monglife.mongs.domain.battle.dto.etc;

import com.monglife.mongs.domain.battle.vo.BattlePlayerVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CreateBattleDto {

    private Long roomId;

    private Set<BattlePlayerVo> battlePlayers;

    @Builder
    public CreateBattleDto(Long roomId, Set<BattlePlayerVo> battlePlayers) {
        this.roomId = roomId;
        this.battlePlayers = battlePlayers;
    }
}
