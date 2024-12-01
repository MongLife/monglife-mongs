package com.monglife.mongs.domain.mong.dto.event;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MongGraduateEvent {

    private final Long mongId;

    @Builder
    public MongGraduateEvent(Long mongId) {
        this.mongId = mongId;
    }
}
