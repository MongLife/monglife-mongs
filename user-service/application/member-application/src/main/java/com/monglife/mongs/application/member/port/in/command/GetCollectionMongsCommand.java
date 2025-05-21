package com.monglife.mongs.application.member.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetCollectionMongsCommand {

    private final Long accountId;

    @Builder
    public GetCollectionMongsCommand(Long accountId) {
        this.accountId = accountId;
    }
}
