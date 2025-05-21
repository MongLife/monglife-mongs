package com.monglife.mongs.adapter.in.member.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetConsumedOrderResponseDto {

    private Long orderId;

    private String socialOrderId;

    private String productId;

    @Builder
    public GetConsumedOrderResponseDto(Long orderId, String socialOrderId, String productId) {
        this.orderId = orderId;
        this.socialOrderId = socialOrderId;
        this.productId = productId;
    }
}
