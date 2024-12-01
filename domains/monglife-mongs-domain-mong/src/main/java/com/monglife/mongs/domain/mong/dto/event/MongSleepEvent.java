package com.monglife.mongs.domain.mong.dto.event;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MongSleepEvent {

    private final Long mongId;

    @Builder
    public MongSleepEvent(Long mongId) {
        this.mongId = mongId;
    }
}
