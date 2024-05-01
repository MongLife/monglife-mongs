package com.mongs.play.app.core.vo;

import lombok.Builder;

@Builder(toBuilder = true)
public record PassportVo(
        PassportDataVo data,
        String passportIntegrity
) {
}