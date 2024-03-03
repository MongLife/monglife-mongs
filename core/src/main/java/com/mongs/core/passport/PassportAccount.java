package com.mongs.core.passport;

import lombok.Builder;

@Builder(toBuilder = true)
public record PassportAccount(
        Long id,
        String email,
        String name,
        String role
) {
}