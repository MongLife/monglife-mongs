package com.monglife.mongs.application.member.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateFeedbackCommand {

    private final Long accountId;

    private final String deviceId;

    private final String deviceName;

    private final String title;

    private final String content;

    @Builder
    public CreateFeedbackCommand(Long accountId, String deviceId, String deviceName, String title, String content) {
        this.accountId = accountId;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.title = title;
        this.content = content;
    }
}
