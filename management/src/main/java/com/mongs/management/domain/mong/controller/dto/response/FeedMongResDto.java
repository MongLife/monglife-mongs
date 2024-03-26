package com.mongs.management.domain.mong.controller.dto.response;

import lombok.Builder;

@Builder
public record FeedMongResDto(
        Long mongId,
        Double weight,
        Double strength,
        Double satiety,
        Double healthy,
        Double sleep,
        Double exp
) {
}
