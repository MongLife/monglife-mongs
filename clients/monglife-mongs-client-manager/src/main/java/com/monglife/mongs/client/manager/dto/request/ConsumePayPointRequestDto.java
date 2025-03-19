package com.monglife.mongs.client.manager.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ConsumePayPointRequestDto {

    private Integer payPoint;

    @Builder
    public ConsumePayPointRequestDto(Integer payPoint) {
        this.payPoint = payPoint;
    }
}
