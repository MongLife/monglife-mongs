package com.monglife.mongs.domain.mong.dto.event;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MongObserveMetaEvent {

    private final Long mongId;

    private final Boolean isActive;

    @Builder
    public MongObserveMetaEvent(Long mongId, Boolean isActive) {
        this.mongId = mongId;
        this.isActive = isActive;
    }
}
