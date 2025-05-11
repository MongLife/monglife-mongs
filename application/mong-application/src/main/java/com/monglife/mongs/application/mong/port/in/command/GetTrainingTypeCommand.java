package com.monglife.mongs.application.mong.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetTrainingTypeCommand {

    private final String trainingTypeCode;

    @Builder
    public GetTrainingTypeCommand(String trainingTypeCode) {
        this.trainingTypeCode = trainingTypeCode;
    }
}
