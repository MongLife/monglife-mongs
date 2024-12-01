package com.monglife.mongs.domain.mong.dto.event;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class MongEggEvolutionEvent {

    private final Long mongId;

    private final LocalTime sleepAt;

    private final LocalTime wakeupAt;

    @Builder
    public MongEggEvolutionEvent(Long mongId, LocalTime sleepAt, LocalTime wakeupAt) {
        this.mongId = mongId;
        this.sleepAt = sleepAt;
        this.wakeupAt = wakeupAt;
    }
}
