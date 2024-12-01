package com.monglife.mongs.domain.mong.dto.event;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MongCreateEvent {

    private final Long mongId;

    @Builder
    public MongCreateEvent(Long mongId) {
        this.mongId = mongId;
    }
}
