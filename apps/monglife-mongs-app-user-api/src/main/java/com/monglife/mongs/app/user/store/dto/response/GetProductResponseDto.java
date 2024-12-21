package com.monglife.mongs.app.user.store.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetProductResponseDto {

    private String productId;

    private String productName;

    private Double price;

    @Builder
    public GetProductResponseDto(String productId, String productName, Double price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }
}
