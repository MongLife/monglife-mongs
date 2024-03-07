package com.mongs.core.passport;

import lombok.Builder;

import java.time.LocalDate;

@Builder(toBuilder = true)
public record PassportAccount(
        Long id,
        String deviceId,
        String email,
        String name,
        LocalDate loginAt,
        String role
) {
}
