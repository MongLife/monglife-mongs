package com.mongs.play.app.common.external.dto.res;

import com.mongs.play.app.common.external.vo.FindFeedbackCodeVo;
import com.mongs.play.app.common.external.vo.FindFoodCodeVo;
import com.mongs.play.app.common.external.vo.FindMapCodeVo;
import com.mongs.play.app.common.external.vo.FindMongCodeVo;
import lombok.Builder;

import java.util.List;

@Builder
public record FindCodeResDto(
        String codeIntegrity,
        List<FindMapCodeVo> mapCodeList,
        List<FindMongCodeVo> mongCodeList,
        List<FindFoodCodeVo> foodCodeList,
        List<FindFeedbackCodeVo> feedbackCodeList
) {
}
