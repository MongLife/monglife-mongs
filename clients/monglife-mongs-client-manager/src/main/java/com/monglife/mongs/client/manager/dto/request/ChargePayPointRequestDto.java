package com.monglife.mongs.client.manager.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChargePayPointRequestDto {

    private Long mongId;

    private Integer payPoint;

    @Builder
    public ChargePayPointRequestDto(Long mongId, Integer payPoint) {
        this.mongId = mongId;
        this.payPoint = payPoint;
    }
}
