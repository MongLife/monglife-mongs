package com.monglife.mongs.app.manager.management.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MongObserveResponseDto {

    private Long mongId;

    private String mongTypeCode;

    private Integer payPoint;

    @Builder
    public MongObserveResponseDto(Long mongId, String mongTypeCode, Integer payPoint) {
        this.mongId = mongId;
        this.mongTypeCode = mongTypeCode;
        this.payPoint = payPoint;
    }
}
