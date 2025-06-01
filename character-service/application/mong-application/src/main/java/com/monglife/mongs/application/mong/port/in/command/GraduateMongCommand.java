package com.monglife.mongs.application.mong.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GraduateMongCommand {

    private final Long accountId;

    private final Long mongId;

    @Builder
    public GraduateMongCommand(Long accountId, Long mongId) {
        this.accountId = accountId;
        this.mongId = mongId;
    }
}
