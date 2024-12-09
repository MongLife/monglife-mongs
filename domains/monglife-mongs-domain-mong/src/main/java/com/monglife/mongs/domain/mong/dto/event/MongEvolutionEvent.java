package com.monglife.mongs.domain.mong.dto.event;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MongEvolutionEvent {

    private final Long mongId;

    @Builder
    public MongEvolutionEvent(Long mongId) {
        this.mongId = mongId;
    }
}
