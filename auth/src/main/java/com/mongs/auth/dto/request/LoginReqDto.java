package com.mongs.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record LoginReqDto(
        @NotEmpty
        @NotBlank
        String deviceId,
        @NotEmpty
        @NotBlank
        @Email
        String email,
        @NotEmpty
        @NotBlank
        String name
) {}
