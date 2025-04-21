package com.monglife.mongs.application.member.port.in.command;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class GetConsumedOrderCommand {

    private final Long accountId;

    private final List<String> socialOrderIds;

    @Builder
    public GetConsumedOrderCommand(Long accountId, List<String> socialOrderIds) {
        this.accountId = accountId;
        this.socialOrderIds = socialOrderIds;
    }
}
