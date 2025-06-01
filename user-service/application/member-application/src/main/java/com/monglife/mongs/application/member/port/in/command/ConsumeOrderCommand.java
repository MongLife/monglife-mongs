package com.monglife.mongs.application.member.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ConsumeOrderCommand {

    private final String socialOrderId;

    @Builder
    public ConsumeOrderCommand(String socialOrderId) {
        this.socialOrderId = socialOrderId;
    }
}
