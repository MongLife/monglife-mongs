package com.monglife.mongs.adapter.in.mong.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BuyRandomDrawTicketResponseDto {

    private Long mongId;

    private Integer payPoint;

    private Integer randomDrawTicketCount;

    @Builder
    public BuyRandomDrawTicketResponseDto(Long mongId, Integer payPoint, Integer randomDrawTicketCount) {
        this.mongId = mongId;
        this.payPoint = payPoint;
        this.randomDrawTicketCount = randomDrawTicketCount;
    }
}
