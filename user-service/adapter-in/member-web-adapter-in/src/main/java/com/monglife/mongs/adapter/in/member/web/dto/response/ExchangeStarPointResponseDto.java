package com.monglife.mongs.adapter.in.member.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExchangeStarPointResponseDto {

    private Long accountId;

    private Integer starPoint;

    @Builder
    public ExchangeStarPointResponseDto(Long accountId, Integer starPoint) {
        this.accountId = accountId;
        this.starPoint = starPoint;
    }
}
