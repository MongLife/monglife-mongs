package com.mongs.common.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record FindCodeResDto(
        Long version,
        List<FindMapCodeResDto> mapCodeList,
        List<FindMongCodeResDto> mongCodeList,
        List<FindFeedbackCodeResDto> feedbackCodeList
) {
}
