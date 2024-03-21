package com.mongs.auth.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record LogoutReqDto(
        @NotEmpty
        @NotBlank
        String refreshToken
) {}