package com.monglife.mongs.domain.mong.dto.event;

import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStateHistoryType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MongStateHistoryEvent {

    private final MongStateHistoryType type;

    private final MongStateCode code;

    private final Long accountId;

    private final String mongName;

    @Builder
    public MongStateHistoryEvent(MongStateHistoryType type, MongStateCode code, Long accountId, String mongName) {
        this.type = type;
        this.code = code;
        this.accountId = accountId;
        this.mongName = mongName;
    }
}
