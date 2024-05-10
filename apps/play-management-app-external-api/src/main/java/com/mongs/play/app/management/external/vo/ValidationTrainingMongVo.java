package com.mongs.play.app.management.external.vo;

import lombok.Builder;

@Builder
public record ValidationTrainingMongVo(
        String trainingId,
        Long mongId,
        Boolean isPossible
) {
}
