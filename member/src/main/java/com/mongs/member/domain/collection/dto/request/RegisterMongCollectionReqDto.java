package com.mongs.member.domain.collection.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record RegisterMongCollectionReqDto(
        @NotEmpty
        @NotBlank
        String mongCode
) {
}
