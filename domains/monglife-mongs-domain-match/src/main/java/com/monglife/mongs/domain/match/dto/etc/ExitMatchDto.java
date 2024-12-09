package com.monglife.mongs.domain.match.dto.etc;

import com.monglife.mongs.domain.match.vo.OverMatchVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ExitMatchDto {

    private Boolean isExitAll;

    private List<OverMatchVo> overMatchVos;

    @Builder
    public ExitMatchDto(Boolean isExitAll, List<OverMatchVo> overMatchVos) {
        this.isExitAll = isExitAll;
        this.overMatchVos = overMatchVos;
    }
}
