package com.monglife.mongs.app.manager.management.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MongObserveResponseDto {

    private Integer payPoint;

    @Builder
    public MongObserveResponseDto(Integer payPoint) {
        this.payPoint = payPoint;
    }
}
