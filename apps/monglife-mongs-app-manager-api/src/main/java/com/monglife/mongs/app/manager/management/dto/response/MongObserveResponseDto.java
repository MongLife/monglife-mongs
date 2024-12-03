package com.monglife.mongs.app.manager.management.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MongObserveResponseDto {

    private String mongTypeCode;

    private Integer payPoint;

    @Builder
    public MongObserveResponseDto(String mongTypeCode, Integer payPoint) {
        this.mongTypeCode = mongTypeCode;
        this.payPoint = payPoint;
    }
}
