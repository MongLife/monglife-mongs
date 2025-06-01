package com.monglife.mongs.application.member.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreatePlayerCommand {

    private final Long accountId;

    @Builder
    public CreatePlayerCommand(Long accountId) {
        this.accountId = accountId;
    }
}
