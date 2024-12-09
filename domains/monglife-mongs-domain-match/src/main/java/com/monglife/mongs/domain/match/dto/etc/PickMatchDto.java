package com.monglife.mongs.domain.match.dto.etc;

import com.monglife.mongs.domain.match.vo.FightMatchVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PickMatchDto {

    private Boolean isPickAll;

    private FightMatchVo fightMatchVo;

    @Builder
    public PickMatchDto(Boolean isPickAll, FightMatchVo fightMatchVo) {
        this.isPickAll = isPickAll;
        this.fightMatchVo = fightMatchVo;
    }
}
