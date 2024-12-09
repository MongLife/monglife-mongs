package com.monglife.mongs.domain.match.dto.etc;

import com.monglife.mongs.domain.match.vo.FightMatchVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnterMatchDto {

    private Boolean isEnterAll;

    private FightMatchVo fightMatchVo;

    @Builder
    public EnterMatchDto(Boolean isEnterAll, FightMatchVo fightMatchVo) {
        this.isEnterAll = isEnterAll;
        this.fightMatchVo = fightMatchVo;
    }
}
