package com.monglife.mongs.app.user.store.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateOrderResponseDto {

    private Long productOrderId;

    @Builder
    public CreateOrderResponseDto(Long productOrderId) {
        this.productOrderId = productOrderId;
    }
}
