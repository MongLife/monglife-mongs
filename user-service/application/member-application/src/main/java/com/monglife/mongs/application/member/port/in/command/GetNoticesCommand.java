package com.monglife.mongs.application.member.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetNoticesCommand {

    private final Integer page;

    private final Integer size;

    @Builder
    public GetNoticesCommand(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }
}
