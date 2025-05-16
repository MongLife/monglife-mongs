package com.monglife.mongs.application.device.port.out.dto;

import com.monglife.mongs.domain.device.model.Step;

public class StepEventMapper {

    public static StepEventDto toEventDto(Step step, Long mongId, Integer walkingCount, Integer payPoint) {
        return StepEventDto.builder()
                .deviceId(step.getDeviceId())
                .mongId(mongId)
                .walkingCount(walkingCount)
                .payPoint(payPoint)
                .build();
    }
}
