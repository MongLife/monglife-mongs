package com.monglife.mongs.application.member.port.in.command;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class GetConsumedOrderCommand {

    private final List<String> socialOrderIds;

    @Builder
    public GetConsumedOrderCommand(List<String> socialOrderIds) {
        this.socialOrderIds = socialOrderIds;
    }
}
