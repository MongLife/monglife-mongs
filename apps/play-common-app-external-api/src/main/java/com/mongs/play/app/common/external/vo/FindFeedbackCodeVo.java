package com.mongs.play.app.common.external.vo;

import com.mongs.play.domain.code.entity.FeedbackCode;
import lombok.Builder;

import java.util.List;

@Builder
public record FindFeedbackCodeVo(
        String code,
        String groupCode,
        String message,
        String buildVersion
) {
    public static FindFeedbackCodeVo of(FeedbackCode feedbackCode) {
        return FindFeedbackCodeVo.builder()
                .code(feedbackCode.code())
                .groupCode(feedbackCode.groupCode())
                .message(feedbackCode.message())
                .buildVersion(feedbackCode.buildVersion())
                .build();
    }

    public static List<FindFeedbackCodeVo> toList(List<FeedbackCode> feedbackCodeList) {
        return feedbackCodeList.stream()
                .map(FindFeedbackCodeVo::of)
                .toList();
    }
}
