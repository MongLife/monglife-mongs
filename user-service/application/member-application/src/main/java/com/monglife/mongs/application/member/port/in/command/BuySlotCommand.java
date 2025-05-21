package com.monglife.mongs.application.member.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BuySlotCommand {

    private final Long accountId;

    @Builder
    public BuySlotCommand(Long accountId) {
        this.accountId = accountId;
    }
}
