package com.monglife.mongs.domain.mong.dto.event;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MongWakeupEvent {

    private final Long mongId;

    @Builder
    public MongWakeupEvent(Long mongId) {
        this.mongId = mongId;
    }
}
