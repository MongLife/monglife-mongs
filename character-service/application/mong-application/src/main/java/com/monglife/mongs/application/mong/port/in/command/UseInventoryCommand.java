package com.monglife.mongs.application.mong.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UseInventoryCommand {

    private final Long accountId;

    private final Long mongId;

    private final Long inventoryId;

    @Builder
    public UseInventoryCommand(Long accountId, Long mongId, Long inventoryId) {
        this.accountId = accountId;
        this.mongId = mongId;
        this.inventoryId = inventoryId;
    }
}
