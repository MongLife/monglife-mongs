package com.monglife.mongs.domain.mong.dto.event;

import com.monglife.mongs.domain.mong.enums.MongStateCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class MongStateObserveEvent {

    private final Long mongId;

    private final MongStateCode stateCode;

    private final Boolean isSleep;

    private final LocalDateTime updatedAt;

    @Builder
    public MongStateObserveEvent(Long mongId, MongStateCode stateCode, Boolean isSleep, LocalDateTime updatedAt) {
        this.mongId = mongId;
        this.stateCode = stateCode;
        this.isSleep = isSleep;
        this.updatedAt = updatedAt;
    }
}
