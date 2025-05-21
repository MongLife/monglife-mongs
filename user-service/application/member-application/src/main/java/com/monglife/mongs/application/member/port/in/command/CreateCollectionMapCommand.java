package com.monglife.mongs.application.member.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateCollectionMapCommand {

    private final Long accountId;

    private final String mapTypeCode;

    @Builder
    public CreateCollectionMapCommand(Long accountId, String mapTypeCode) {
        this.accountId = accountId;
        this.mapTypeCode = mapTypeCode;
    }
}
