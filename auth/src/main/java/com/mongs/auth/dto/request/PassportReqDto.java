package com.mongs.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record PassportReqDto(
        @NotEmpty
        @NotBlank
        String accessToken
) {}
