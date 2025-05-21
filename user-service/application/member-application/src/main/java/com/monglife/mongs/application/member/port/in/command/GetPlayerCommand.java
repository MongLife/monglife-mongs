package com.monglife.mongs.application.member.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetPlayerCommand {

    private final Long accountId;

    @Builder
    public GetPlayerCommand(Long accountId) {
        this.accountId = accountId;
    }
}
