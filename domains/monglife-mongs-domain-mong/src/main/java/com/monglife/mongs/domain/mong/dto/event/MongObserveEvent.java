package com.monglife.mongs.domain.mong.dto.event;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MongObserveEvent {

    private final Long mongId;

    private final Integer payPoint;

    private final String mongTypeCode;

    @Builder
    public MongObserveEvent(Long mongId, Integer payPoint, String mongTypeCode) {
        this.mongId = mongId;
        this.payPoint = payPoint;
        this.mongTypeCode = mongTypeCode;
    }
}
