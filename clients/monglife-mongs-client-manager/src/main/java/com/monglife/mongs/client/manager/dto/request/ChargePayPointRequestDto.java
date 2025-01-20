package com.monglife.mongs.client.manager.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChargePayPointRequestDto {

    private Integer payPoint;

    @Builder
    public ChargePayPointRequestDto(Integer payPoint) {
        this.payPoint = payPoint;
    }
}
