package com.monglife.mongs.domain.mong.dto.event;

import com.monglife.mongs.domain.mong.entity.MongStateHistoryEntity;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MongStateHistoryEvent {

    private final MongStateHistoryEntity.MongStateHistoryType type;

    private final MongStateCode code;

    private final Long accountId;

    private final String mongName;

    @Builder
    public MongStateHistoryEvent(MongStateHistoryEntity.MongStateHistoryType type, MongStateCode code, Long accountId, String mongName) {
        this.type = type;
        this.code = code;
        this.accountId = accountId;
        this.mongName = mongName;
    }
}
