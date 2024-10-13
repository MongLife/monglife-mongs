package com.monglife.mongs.app.management.business.vo;

import lombok.Builder;

@Builder
public record ValidationTrainingMongVo(
        Long mongId,
        Boolean isPossible
) {
}
