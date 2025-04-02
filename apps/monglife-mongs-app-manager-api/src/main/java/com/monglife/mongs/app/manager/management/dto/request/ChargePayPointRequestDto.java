package com.monglife.mongs.app.manager.management.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChargePayPointRequestDto {

    private Integer payPoint;

    @Builder
    public ChargePayPointRequestDto(Integer payPoint) {
        this.payPoint = payPoint;
    }
}
