package com.mongs.member.domain.feedback.dto.response;

import com.mongs.member.domain.feedback.entity.Feedback;
import lombok.Builder;

@Builder
public record SolveFeedbackResDto(
        Long id,
        Long accountId,
        String deviceId,
        Boolean isSolved
) {
    public static SolveFeedbackResDto of(Feedback feedback) {
        return SolveFeedbackResDto.builder()
                .id(feedback.getId())
                .accountId(feedback.getAccountId())
                .deviceId(feedback.getDeviceId())
                .isSolved(feedback.getIsSolved())
                .build();
    }
}
