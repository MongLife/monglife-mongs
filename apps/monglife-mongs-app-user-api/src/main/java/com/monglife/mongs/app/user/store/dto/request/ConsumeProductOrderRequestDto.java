package com.monglife.mongs.app.user.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConsumeProductOrderRequestDto {

    @NotBlank
    private Long productOrderId;

    @NotBlank
    private String purchaseToken;

    @Builder
    public ConsumeProductOrderRequestDto(Long productOrderId, String purchaseToken) {
        this.productOrderId = productOrderId;
        this.purchaseToken = purchaseToken;
    }
}
