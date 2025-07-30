package com.monglife.mongs.application.member.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetNoticeCommand {

    private final Long noticeId;

    @Builder
    public GetNoticeCommand(Long noticeId) {
        this.noticeId = noticeId;
    }
}
