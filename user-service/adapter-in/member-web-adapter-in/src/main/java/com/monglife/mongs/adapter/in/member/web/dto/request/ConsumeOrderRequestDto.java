package com.monglife.mongs.adapter.in.member.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConsumeOrderRequestDto {

    @NotBlank
    private String socialOrderId;

    @NotBlank
    private String productId;

    @NotBlank
    private String purchaseToken;

    @Builder
    public ConsumeOrderRequestDto(String socialOrderId, String productId, String purchaseToken) {
        this.socialOrderId = socialOrderId;
        this.productId = productId;
        this.purchaseToken = purchaseToken;
    }
}
