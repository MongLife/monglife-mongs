package com.monglife.mongs.application.member.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class IncreaseStarPointCommand {

    private final Long accountId;

    private final Integer starPoint;

    @Builder
    public IncreaseStarPointCommand(Long accountId, Integer starPoint) {
        this.accountId = accountId;
        this.starPoint = starPoint;
    }
}
