package com.monglife.mongs.application.mong.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FeedFoodCommand {

    private final Long accountId;

    private final Long mongId;

    private final String foodCode;

    @Builder
    public FeedFoodCommand(Long accountId, Long mongId, String foodCode) {
        this.accountId = accountId;
        this.mongId = mongId;
        this.foodCode = foodCode;
    }
}
