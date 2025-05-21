package com.monglife.mongs.application.mong.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FeedSnackCommand {

    private final Long accountId;

    private final Long mongId;

    private final String snackTypeCode;

    @Builder
    public FeedSnackCommand(Long accountId, Long mongId, String snackTypeCode) {
        this.accountId = accountId;
        this.mongId = mongId;
        this.snackTypeCode = snackTypeCode;
    }
}
