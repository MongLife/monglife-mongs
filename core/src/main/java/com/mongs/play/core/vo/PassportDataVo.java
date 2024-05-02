package com.mongs.play.core.vo;

import lombok.Builder;

@Builder(toBuilder = true)
public record PassportDataVo(
        PassportAccountVo account
) {
}