package com.mongs.member.domain.collection.controller.dto.response;

import lombok.Builder;

@Builder
public record RemoveMongCollectionResDto(
        Long accountId
) {
}