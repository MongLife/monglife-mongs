package com.monglife.mongs.application.device.port.out.dto;

import com.monglife.mongs.domain.device.model.Step;

public class StepPersistenceMapper {

    public static StepPersistenceDto toPersistenceDto(Step step) {
        return StepPersistenceDto.builder()
                .deviceId(step.getDeviceId())
                .walkingCount(step.getWalkingCount())
                .totalWalkingCount(step.getTotalWalkingCount())
                .consumeWalkingCount(step.getConsumeWalkingCount())
                .deviceBootedDt(step.getDeviceBootedDt())
                .build();
    }

    public static Step toDomain(StepPersistenceDto stepPersistenceDto) {
        return Step.builder()
                .deviceId(stepPersistenceDto.getDeviceId())
                .walkingCount(stepPersistenceDto.getWalkingCount())
                .totalWalkingCount(stepPersistenceDto.getTotalWalkingCount())
                .consumeWalkingCount(stepPersistenceDto.getConsumeWalkingCount())
                .deviceBootedDt(stepPersistenceDto.getDeviceBootedDt())
                .build();
    }
}
