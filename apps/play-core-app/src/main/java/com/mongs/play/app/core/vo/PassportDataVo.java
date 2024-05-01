package com.mongs.play.app.core.vo;

import lombok.Builder;

@Builder(toBuilder = true)
public record PassportDataVo(
        PassportAccountVo account
) {
}