package com.mongs.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record ReissueReqDto(
        @NotEmpty
        @NotBlank
        String refreshToken
) {}
