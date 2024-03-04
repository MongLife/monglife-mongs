package com.mongs.member.domain.feedback.dto.response;

import com.mongs.member.domain.feedback.entity.Feedback;
import lombok.Builder;

import java.util.List;

@Builder
public record FindFeedbackResDto(
        Long id,
        Long accountId,
        String deviceId,
        String code,
        String title,
        String content,
        Boolean isSolved
) {
    public static FindFeedbackResDto of(Feedback feedback) {
        return FindFeedbackResDto.builder()
                .id(feedback.getId())
                .accountId(feedback.getAccountId())
                .deviceId(feedback.getDeviceId())
                .code(feedback.getCode())
                .title(feedback.getTitle())
                .content(feedback.getContent())
                .isSolved(feedback.getIsSolved())
                .build();
    }

    public static List<FindFeedbackResDto> toList(List<Feedback> feedbackList) {
        return feedbackList.stream()
                .map(FindFeedbackResDto::of)
                .toList();
    }
}
