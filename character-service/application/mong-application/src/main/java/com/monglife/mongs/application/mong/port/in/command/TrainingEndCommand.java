package com.monglife.mongs.application.mong.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TrainingEndCommand {

    private final Long accountId;

    private final String trainingCode;

    private final Long mongId;

    private final Integer score;

    @Builder
    public TrainingEndCommand(Long accountId, String trainingCode, Long mongId, Integer score) {
        this.accountId = accountId;
        this.trainingCode = trainingCode;
        this.mongId = mongId;
        this.score = score;
    }
}
