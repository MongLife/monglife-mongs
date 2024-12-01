package com.monglife.mongs.domain.battle.dto.etc;

import com.monglife.mongs.domain.battle.vo.OverBattleVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExitBattleDto {

    private Boolean isExitAll;

    private List<OverBattleVo> overBattleVos;

    @Builder
    public ExitBattleDto(Boolean isExitAll, List<OverBattleVo> overBattleVos) {
        this.isExitAll = isExitAll;
        this.overBattleVos = overBattleVos;
    }
}
