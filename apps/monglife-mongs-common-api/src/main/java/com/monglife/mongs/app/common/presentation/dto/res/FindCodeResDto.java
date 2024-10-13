package com.monglife.mongs.app.common.presentation.dto.res;

import com.monglife.mongs.app.common.business.vo.FindFoodCodeVo;
import com.monglife.mongs.app.common.business.vo.FindMapCodeVo;
import com.monglife.mongs.app.common.business.vo.FindMongCodeVo;
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
