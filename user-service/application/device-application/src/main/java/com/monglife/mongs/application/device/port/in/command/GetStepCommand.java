package com.monglife.mongs.application.device.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetStepCommand {

    private final String deviceId;

    @Builder
    public GetStepCommand(String deviceId) {
        this.deviceId = deviceId;
    }
}
