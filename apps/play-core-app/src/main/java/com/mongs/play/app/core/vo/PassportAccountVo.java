package com.mongs.play.app.core.vo;

import lombok.Builder;

@Builder(toBuilder = true)
public record PassportAccountVo(
        Long id,
        String deviceId,
        String email,
        String name,
        String role
) {
}
