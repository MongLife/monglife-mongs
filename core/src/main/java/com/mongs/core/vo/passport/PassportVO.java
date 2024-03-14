package com.mongs.core.vo.passport;

import lombok.Builder;

@Builder(toBuilder = true)
public record PassportVO(
        PassportData data,
        String passportIntegrity
) {
}