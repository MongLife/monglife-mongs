package com.monglife.mongs.adapter.in.member.web.player.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExchangeStarPointRequestDto {

    @NotNull
    private Long mongId;

    @Min(1)
    private Integer starPoint;

    @Builder
    public ExchangeStarPointRequestDto(Long mongId, Integer starPoint) {
        this.mongId = mongId;
        this.starPoint = starPoint;
    }
}
