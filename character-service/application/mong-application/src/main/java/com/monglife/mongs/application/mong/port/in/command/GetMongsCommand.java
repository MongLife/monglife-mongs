package com.monglife.mongs.application.mong.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetMongsCommand {

    private final Long accountId;

    @Builder
    public GetMongsCommand(Long accountId) {
        this.accountId = accountId;
    }
}
