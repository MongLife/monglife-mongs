package com.monglife.mongs.domain.mong.dto.event;

import com.monglife.mongs.domain.mong.enums.MongStateCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MongObserveStateEvent {

    private final Long mongId;

    private final MongStateCode stateCode;

    private final Boolean isSleep;

    @Builder
    public MongObserveStateEvent(Long mongId, MongStateCode stateCode, Boolean isSleep) {
        this.mongId = mongId;
        this.stateCode = stateCode;
        this.isSleep = isSleep;
    }
}
