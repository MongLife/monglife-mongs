package com.monglife.mongs.application.member.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetCollectionMapsCommand {

    private final Long accountId;

    @Builder
    public GetCollectionMapsCommand(Long accountId) {
        this.accountId = accountId;
    }
}
