package com.mongs.play.app.player.external.feedback.dto.res;

import lombok.Builder;

@Builder
public record RegisterFeedbackResDto(
        Long id,
        Long accountId,
        String deviceId
) {
}
