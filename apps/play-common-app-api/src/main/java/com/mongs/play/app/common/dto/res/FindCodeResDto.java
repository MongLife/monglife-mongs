package com.mongs.play.app.common.dto.res;

import com.mongs.play.app.common.vo.FindFoodCodeVo;
import com.mongs.play.app.common.vo.FindMapCodeVo;
import com.mongs.play.app.common.vo.FindMongCodeVo;
import lombok.Builder;

import java.util.List;

@Builder
public record FindCodeResDto(
        String codeIntegrity,
        List<FindMapCodeVo> mapCodeList,
        List<FindMongCodeVo> mongCodeList,
        List<FindFoodCodeVo> foodCodeList
) {
}
