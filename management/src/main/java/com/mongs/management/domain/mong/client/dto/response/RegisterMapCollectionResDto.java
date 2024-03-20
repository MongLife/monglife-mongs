package com.mongs.management.domain.mong.client.dto.response;

import java.time.LocalDateTime;

public record RegisterMapCollectionResDto(
        Long accountId,
        String code,
        LocalDateTime createdAt
) {
}
