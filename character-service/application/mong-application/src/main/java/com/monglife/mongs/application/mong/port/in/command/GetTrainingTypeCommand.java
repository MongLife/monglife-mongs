package com.monglife.mongs.application.mong.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetTrainingTypeCommand {

    private final String trainingCode;

    @Builder
    public GetTrainingTypeCommand(String trainingCode) {
        this.trainingCode = trainingCode;
    }
}
