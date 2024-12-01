package com.monglife.mongs.domain.battle.dto.etc;

import com.monglife.mongs.domain.battle.vo.FightBattleVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickBattleDto {

    private Boolean isPickAll;

    private FightBattleVo fightBattleVo;

    @Builder
    public PickBattleDto(Boolean isPickAll, FightBattleVo fightBattleVo) {
        this.isPickAll = isPickAll;
        this.fightBattleVo = fightBattleVo;
    }
}
