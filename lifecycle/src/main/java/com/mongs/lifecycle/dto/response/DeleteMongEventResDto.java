package com.mongs.lifecycle.dto.response;

import lombok.Builder;

@Builder
public record DeleteMongEventResDto(
        Long mongId
) {
}