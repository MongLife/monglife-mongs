package com.monglife.mongs.application.member.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateCollectionMapCommand {

    private final Long accountId;

    private final String mapCode;

    @Builder
    public CreateCollectionMapCommand(Long accountId, String mapCode) {
        this.accountId = accountId;
        this.mapCode = mapCode;
    }
}
