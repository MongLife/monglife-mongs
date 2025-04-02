package com.mongs.play.app.player.external.feedback.vo;

import com.mongs.play.domain.feedback.entity.Feedback;
import lombok.Builder;

@Builder
public record RegisterFeedbackVo(
        Long id,
        Long accountId,
        String deviceId
) {
    public static RegisterFeedbackVo of(Feedback feedback) {
        return RegisterFeedbackVo.builder()
                .id(feedback.getId())
                .accountId(feedback.getAccountId())
                .deviceId(feedback.getDeviceId())
                .build();
    }
}
