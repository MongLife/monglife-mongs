package com.monglife.mongs.application.mong.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetInventoriesCommand {

    private final Long accountId;

    private final Long mongId;

    private final Integer page;

    private final Integer size;

    @Builder
    public GetInventoriesCommand(Long accountId, Long mongId, Integer page, Integer size) {
        this.accountId = accountId;
        this.mongId = mongId;
        this.page = page;
        this.size = size;
    }
}
