package com.monglife.mongs.domain.match.dto.etc;

import com.monglife.mongs.domain.match.vo.MatchVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PickMatchDto {

    private Boolean isPickAll;

    private MatchVo matchVo;

    @Builder
    public PickMatchDto(Boolean isPickAll, MatchVo matchVo) {
        this.isPickAll = isPickAll;
        this.matchVo = matchVo;
    }
}
