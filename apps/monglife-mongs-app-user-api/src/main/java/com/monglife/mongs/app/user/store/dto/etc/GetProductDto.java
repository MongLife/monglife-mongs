package com.monglife.mongs.app.user.store.dto.etc;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetProductDto {

    private String productId;

    private String productName;

    private Double price;

    @Builder
    public GetProductDto(String productId, String productName, Double price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }
}
