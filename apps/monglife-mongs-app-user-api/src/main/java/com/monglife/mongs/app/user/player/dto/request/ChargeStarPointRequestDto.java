package com.monglife.mongs.app.user.player.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChargeStarPointRequestDto {

    private String receipt;

    @Min(1)
    private Integer starPoint;

    @Builder
    public ChargeStarPointRequestDto(String receipt, Integer starPoint) {
        this.receipt = receipt;
        this.starPoint = starPoint;
    }
}
