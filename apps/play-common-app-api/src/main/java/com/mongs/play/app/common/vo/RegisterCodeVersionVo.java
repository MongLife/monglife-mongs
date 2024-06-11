package com.mongs.play.app.common.vo;

import lombok.Builder;

@Builder
public record RegisterCodeVersionVo(
        String buildVersion
) {
}
