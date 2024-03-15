package com.mongs.common.controller.dto.response;

import com.mongs.core.entity.FeedbackCode;
import lombok.Builder;

import java.util.List;

@Builder
public record FindFeedbackCodeResDto(
        String code,
        String groupCode,
        String message
) {
    public static FindFeedbackCodeResDto of(FeedbackCode feedbackCode) {
        return FindFeedbackCodeResDto.builder()
                .code(feedbackCode.code())
                .groupCode(feedbackCode.groupCode())
                .message(feedbackCode.message())
                .build();
    }

    public static List<FindFeedbackCodeResDto> toList(List<FeedbackCode> feedbackCodeList) {
        return feedbackCodeList.stream()
                .map(FindFeedbackCodeResDto::of)
                .toList();
    }
}
