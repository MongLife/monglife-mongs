package com.mongs.play.app.auth.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record ReissueReqDto(
        @NotEmpty
        @NotBlank
        String refreshToken
) {}
