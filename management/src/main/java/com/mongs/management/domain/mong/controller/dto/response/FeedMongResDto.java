package com.mongs.management.domain.mong.controller.dto.response;

import lombok.Builder;

@Builder
public record FeedMongResDto(
        Long mongId,
        Double weight,
        Double health,
        Double satiety,
        Double strength,
        Double sleep,
        Integer exp
) {
}
