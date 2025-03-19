package com.monglife.mongs.domain.mong.dto.event;

import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import com.monglife.mongs.domain.mong.enums.MongStatusHistoryType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MongStatusHistoryEvent {

    private final MongStatusHistoryType type;

    private final MongStatusCode code;

    private final Long accountId;

    private final String mongName;

    @Builder
    public MongStatusHistoryEvent(MongStatusHistoryType type, MongStatusCode code, Long accountId, String mongName) {
        this.type = type;
        this.code = code;
        this.accountId = accountId;
        this.mongName = mongName;
    }
}
