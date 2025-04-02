package com.monglife.mongs.domain.mong.dto.event;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class MongBasicObserveEvent {

    private final Long mongId;

    private final String mongName;

    private final String mongTypeCode;

    private final Integer payPoint;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    @Builder
    public MongBasicObserveEvent(Long mongId, String mongName, String mongTypeCode, Integer payPoint, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.mongId = mongId;
        this.mongName = mongName;
        this.mongTypeCode = mongTypeCode;
        this.payPoint = payPoint;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
