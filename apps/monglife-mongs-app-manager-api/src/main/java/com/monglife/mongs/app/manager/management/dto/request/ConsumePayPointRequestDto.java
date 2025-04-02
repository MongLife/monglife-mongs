package com.monglife.mongs.app.manager.management.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConsumePayPointRequestDto {

    private Integer payPoint;

    @Builder
    public ConsumePayPointRequestDto(Integer payPoint) {
        this.payPoint = payPoint;
    }
}
