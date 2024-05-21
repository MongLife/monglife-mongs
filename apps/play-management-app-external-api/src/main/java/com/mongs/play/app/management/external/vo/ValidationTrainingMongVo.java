package com.mongs.play.app.management.external.vo;

import lombok.Builder;

@Builder
public record ValidationTrainingMongVo(
        Long mongId,
        Boolean isPossible
) {
}
