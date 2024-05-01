package com.mongs.play.app.auth.external.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record LogoutReqDto(
        @NotEmpty
        @NotBlank
        String refreshToken
) {}
