package com.mongs.member.domain.feedback.dto.response;

import com.mongs.member.domain.feedback.entity.Feedback;
import lombok.Builder;

@Builder
public record RegisterFeedbackResDto(
        Long id,
        Long accountId,
        String deviceId,
        String code
) {
    public static RegisterFeedbackResDto of(Feedback feedback) {
        return RegisterFeedbackResDto.builder()
                .id(feedback.getId())
                .accountId(feedback.getAccountId())
                .deviceId(feedback.getDeviceId())
                .code(feedback.getCode())
                .build();
    }
}
