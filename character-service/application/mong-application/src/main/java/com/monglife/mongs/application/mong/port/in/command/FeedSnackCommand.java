package com.monglife.mongs.application.mong.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FeedSnackCommand {

    private final Long accountId;

    private final Long mongId;

    private final String snackCode;

    @Builder
    public FeedSnackCommand(Long accountId, Long mongId, String snackCode) {
        this.accountId = accountId;
        this.mongId = mongId;
        this.snackCode = snackCode;
    }
}
