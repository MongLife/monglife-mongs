package com.monglife.mongs.client.manager.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChargePayPointRequestDto {

    private Long mongId;

    private Integer payPoint;

    @Builder
    public ChargePayPointRequestDto(Long mongId, Integer payPoint) {
        this.mongId = mongId;
        this.payPoint = payPoint;
    }
}
