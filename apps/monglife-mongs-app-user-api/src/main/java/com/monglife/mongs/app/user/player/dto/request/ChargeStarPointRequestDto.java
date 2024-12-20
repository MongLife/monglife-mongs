package com.monglife.mongs.app.user.player.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChargeStarPointRequestDto {

    private Integer starPoint;

    @Builder
    public ChargeStarPointRequestDto(Integer starPoint) {
        this.starPoint = starPoint;
    }
}
