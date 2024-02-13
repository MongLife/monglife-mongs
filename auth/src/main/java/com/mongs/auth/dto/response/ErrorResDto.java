package com.mongs.auth.dto.response;

import lombok.Builder;

@Builder
public record ErrorResDto(
        String code,
        Object message
) {}

