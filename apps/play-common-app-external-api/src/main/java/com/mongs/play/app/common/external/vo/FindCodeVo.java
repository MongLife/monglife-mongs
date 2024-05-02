package com.mongs.play.app.common.external.vo;

import lombok.Builder;

import java.util.List;

@Builder
public record FindCodeVo(
        String codeIntegrity,
        List<FindMapCodeVo> mapCodeList,
        List<FindMongCodeVo> mongCodeList,
        List<FindFoodCodeVo> foodCodeList,
        List<FindFeedbackCodeVo> feedbackCodeList
) {
}
