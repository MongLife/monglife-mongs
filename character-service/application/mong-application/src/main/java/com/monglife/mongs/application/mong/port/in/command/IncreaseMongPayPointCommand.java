package com.monglife.mongs.application.mong.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class IncreaseMongPayPointCommand {

    private final Long mongId;

    private final Integer payPoint;

    @Builder
    public IncreaseMongPayPointCommand(Long mongId, Integer payPoint) {
        this.mongId = mongId;
        this.payPoint = payPoint;
    }
}
