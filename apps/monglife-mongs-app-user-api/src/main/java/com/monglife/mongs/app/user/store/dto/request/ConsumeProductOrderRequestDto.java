package com.monglife.mongs.app.user.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConsumeProductOrderRequestDto {

    @NotBlank
    private String productId;

    @NotNull
    private String orderId;

    @NotBlank
    private String purchaseToken;

    @Builder
    public ConsumeProductOrderRequestDto(String productId, String orderId, String purchaseToken) {
        this.productId = productId;
        this.orderId = orderId;
        this.purchaseToken = purchaseToken;
    }
}
