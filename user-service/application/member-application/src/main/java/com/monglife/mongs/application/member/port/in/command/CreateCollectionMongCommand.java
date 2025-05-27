package com.monglife.mongs.application.member.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateCollectionMongCommand {

    private final Long accountId;

    private final String mongCode;

    @Builder
    public CreateCollectionMongCommand(Long accountId, String mongCode) {
        this.accountId = accountId;
        this.mongCode = mongCode;
    }
}
