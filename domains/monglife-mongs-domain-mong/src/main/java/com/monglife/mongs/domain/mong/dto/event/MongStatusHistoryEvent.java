package com.monglife.mongs.domain.mong.dto.event;

import com.monglife.mongs.domain.mong.entity.MongStatusHistoryEntity;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MongStatusHistoryEvent {

    private final MongStatusHistoryEntity.MongStatusHistoryType type;

    private final MongStatusCode code;

    private final Long accountId;

    private final String mongName;

    @Builder
    public MongStatusHistoryEvent(MongStatusHistoryEntity.MongStatusHistoryType type, MongStatusCode code, Long accountId, String mongName) {
        this.type = type;
        this.code = code;
        this.accountId = accountId;
        this.mongName = mongName;
    }
}
