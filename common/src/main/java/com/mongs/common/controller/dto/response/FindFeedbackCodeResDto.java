package com.mongs.common.controller.dto.response;

import com.mongs.core.entity.FeedbackCode;
import lombok.Builder;

import java.util.List;

@Builder
public record FindFeedbackCodeResDto(
        String code,
        String groupCode,
        String message,
        Long version
) {
    public static FindFeedbackCodeResDto of(FeedbackCode feedbackCode) {
        return FindFeedbackCodeResDto.builder()
                .code(feedbackCode.code())
                .groupCode(feedbackCode.groupCode())
                .message(feedbackCode.message())
                .version(feedbackCode.version())
                .build();
    }

    public static List<FindFeedbackCodeResDto> toList(List<FeedbackCode> feedbackCodeList) {
        return feedbackCodeList.stream()
                .map(FindFeedbackCodeResDto::of)
                .toList();
    }
}
