package com.monglife.mongs.domain.mong.dto.event;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MongDeleteEvent {

    private final Long mongId;

    @Builder
    public MongDeleteEvent(Long mongId) {
        this.mongId = mongId;
    }
}
