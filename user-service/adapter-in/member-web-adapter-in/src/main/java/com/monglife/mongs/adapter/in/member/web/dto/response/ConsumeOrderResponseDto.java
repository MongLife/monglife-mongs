package com.monglife.mongs.adapter.in.member.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConsumeOrderResponseDto {

    private Long accountId;

    private Long orderId;

    private String socialOrderId;

    private String productId;

    private String purchaseToken;

    private Integer starPoint;

    private Integer slotCount;

    @Builder
    public ConsumeOrderResponseDto(Long accountId, Long orderId, String socialOrderId, String productId, String purchaseToken, Integer starPoint, Integer slotCount) {
        this.accountId = accountId;
        this.orderId = orderId;
        this.socialOrderId = socialOrderId;
        this.productId = productId;
        this.purchaseToken = purchaseToken;
        this.starPoint = starPoint;
        this.slotCount = slotCount;
    }
}
