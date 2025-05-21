package com.monglife.mongs.application.member.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateCollectionMongCommand {

    private final Long accountId;

    private final String mongTypeCode;

    @Builder
    public CreateCollectionMongCommand(Long accountId, String mongTypeCode) {
        this.accountId = accountId;
        this.mongTypeCode = mongTypeCode;
    }
}
