package com.mongs.auth.dto.response;

import com.mongs.passport.PassportData;
import lombok.Builder;

@Builder
public record PassportResDto(
        PassportData data,
        String passportIntegrity
) {
}
