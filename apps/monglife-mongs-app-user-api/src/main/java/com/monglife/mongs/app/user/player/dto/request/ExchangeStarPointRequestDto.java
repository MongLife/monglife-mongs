package com.monglife.mongs.app.user.player.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExchangeStarPointRequestDto {

    private Long mongId;

    @Min(1)
    private Integer starPoint;

    @Builder
    public ExchangeStarPointRequestDto(Long mongId, Integer starPoint) {
        this.mongId = mongId;
        this.starPoint = starPoint;
    }
}
