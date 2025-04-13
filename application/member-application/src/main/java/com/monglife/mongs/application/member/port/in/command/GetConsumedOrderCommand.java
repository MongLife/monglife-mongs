package com.monglife.mongs.application.member.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetConsumedOrderCommand {

    private final Long accountId;

    @Builder
    public GetConsumedOrderCommand(Long accountId) {
        this.accountId = accountId;
    }
}
