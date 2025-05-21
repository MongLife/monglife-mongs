package com.monglife.mongs.application.mong.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UseInventoryItemCommand {

    private final Long accountId;

    private final Long mongId;

    private final Long inventoryItemId;

    @Builder
    public UseInventoryItemCommand(Long accountId, Long mongId, Long inventoryItemId) {
        this.accountId = accountId;
        this.mongId = mongId;
        this.inventoryItemId = inventoryItemId;
    }
}
