package com.mongs.play.auth.internal.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record PassportReqDto(
        @NotEmpty
        @NotBlank
        String accessToken
) {}
