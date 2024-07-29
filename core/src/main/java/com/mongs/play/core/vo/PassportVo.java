package com.mongs.play.core.vo;

import lombok.Builder;

@Builder(toBuilder = true)
public record PassportVo(
        PassportDataVo data,
        String passportIntegrity
) {
}