package com.mongs.common.controller.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record FindVersionResDto(
        Long version,
        LocalDateTime createdAt
) {
}
