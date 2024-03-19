package com.mongs.core.vo.mqtt;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PublishCreateVo(
        Long mongId,
        String name,
        String mongCode,
        Double weight,
        Double strength,
        Double satiety,
        Double health,
        Double sleep,
        Integer poopCount,
        String stateCode,
        String shiftCode,
        Integer payPoint,
        LocalDateTime born

) {
}
