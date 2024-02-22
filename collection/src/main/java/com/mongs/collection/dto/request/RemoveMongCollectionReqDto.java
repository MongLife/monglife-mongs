package com.mongs.collection.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record RemoveMongCollectionReqDto(
        @NotEmpty
        @NotBlank
        String mongCode
) {
}
