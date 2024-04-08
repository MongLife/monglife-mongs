package com.mongs.common.controller.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record FindCodeResDto(
        String codeIntegrity,
        List<FindMapCodeResDto> mapCodeList,
        List<FindMongCodeResDto> mongCodeList,
        List<FindFoodCodeResDto> foodCodeList,
        List<FindFeedbackCodeResDto> feedbackCodeList
) {
}
