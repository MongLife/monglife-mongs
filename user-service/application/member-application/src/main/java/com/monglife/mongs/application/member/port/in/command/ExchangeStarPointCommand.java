package com.monglife.mongs.application.member.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExchangeStarPointCommand {

    private final Long accountId;

    private final Long mongId;

    private final Integer starPoint;

    @Builder
    public ExchangeStarPointCommand(Long accountId, Long mongId, Integer starPoint) {
        this.accountId = accountId;
        this.mongId = mongId;
        this.starPoint = starPoint;
    }
}
