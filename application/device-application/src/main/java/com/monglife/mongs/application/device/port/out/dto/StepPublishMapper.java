package com.monglife.mongs.application.device.port.out.dto;

import com.monglife.mongs.domain.device.model.Step;

public class StepPublishMapper {

    public static StepPublishDto toPublishDto(Step step) {
        return StepPublishDto.builder()
                .deviceId(step.getDeviceId())
                .walkingCount(step.getWalkingCount())
                .totalWalkingCount(step.getTotalWalkingCount())
                .consumeWalkingCount(step.getConsumeWalkingCount())
                .deviceBootedDt(step.getDeviceBootedDt())
                .build();
    }
}
