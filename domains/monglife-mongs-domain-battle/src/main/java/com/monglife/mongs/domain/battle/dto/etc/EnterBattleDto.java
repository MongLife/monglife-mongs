package com.monglife.mongs.domain.battle.dto.etc;

import com.monglife.mongs.domain.battle.vo.FightBattleVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnterBattleDto {

    private Boolean isEnterAll;

    private FightBattleVo fightBattleVo;

    @Builder
    public EnterBattleDto(Boolean isEnterAll, FightBattleVo fightBattleVo) {
        this.isEnterAll = isEnterAll;
        this.fightBattleVo = fightBattleVo;
    }
}
