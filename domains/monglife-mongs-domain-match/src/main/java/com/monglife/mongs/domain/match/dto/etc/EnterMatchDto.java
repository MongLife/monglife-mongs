package com.monglife.mongs.domain.match.dto.etc;

import com.monglife.mongs.domain.match.vo.MatchVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnterMatchDto {

    private Boolean isEnterAll;

    private MatchVo matchVo;

    @Builder
    public EnterMatchDto(Boolean isEnterAll, MatchVo matchVo) {
        this.isEnterAll = isEnterAll;
        this.matchVo = matchVo;
    }
}
