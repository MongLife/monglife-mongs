package com.mongs.member.domain.collection.controller.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RegisterMapCollectionResDto(
        Long accountId,
        String code,
        LocalDateTime createdAt
) {
}
