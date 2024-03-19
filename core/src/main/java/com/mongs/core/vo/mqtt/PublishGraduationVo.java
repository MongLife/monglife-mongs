package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record PublishGraduationVo(
        Long mongId,
        Boolean isActive,
        Integer poopCount,
        Double health,
        Double satiety,
        Double sleep,
        Double strength,
        Double weight,
        String shiftCode,
        String stateCode
) {
}
