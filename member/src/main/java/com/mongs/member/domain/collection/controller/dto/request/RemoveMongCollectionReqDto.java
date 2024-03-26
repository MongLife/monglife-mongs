package com.mongs.member.domain.collection.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record RemoveMongCollectionReqDto(
        @NotEmpty
        @NotBlank
        String mongCode
) {
}
