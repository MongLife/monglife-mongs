package com.monglife.mongs.application.mong.port.in.command;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class CreateMongCommand {

    private final Long accountId;

    private final String mongName;

    private final LocalTime sleepAt;

    private final LocalTime wakeupAt;

    @Builder
    public CreateMongCommand(Long accountId, String mongName, LocalTime sleepAt, LocalTime wakeupAt) {
        this.accountId = accountId;
        this.mongName = mongName;
        this.sleepAt = sleepAt;
        this.wakeupAt = wakeupAt;
    }
}
