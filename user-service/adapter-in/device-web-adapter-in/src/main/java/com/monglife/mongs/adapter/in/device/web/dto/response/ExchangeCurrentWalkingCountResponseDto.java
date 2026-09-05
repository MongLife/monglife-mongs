package com.monglife.mongs.adapter.in.device.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 걸음 수 환전 응답
 *
 * 걸음 수 잔액은 기기가 들고 있으므로 서버가 돌려줄 것이 없다. 지급 예정 페이 포인트만 알린다.
 * (실제 지급은 character-service 가 비동기로 처리한다.)
 */
@Getter
@Setter
@NoArgsConstructor
public class ExchangeCurrentWalkingCountResponseDto {

    private Integer walkingCount;

    private Integer payPoint;

    @Builder
    public ExchangeCurrentWalkingCountResponseDto(Integer walkingCount, Integer payPoint) {
        this.walkingCount = walkingCount;
        this.payPoint = payPoint;
    }
}
