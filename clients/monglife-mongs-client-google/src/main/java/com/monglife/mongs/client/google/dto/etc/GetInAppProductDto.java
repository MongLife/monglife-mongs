package com.monglife.mongs.client.google.dto.etc;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetInAppProductDto {

    private String productId;

    private String productName;

    private Double price;

    @Builder
    public GetInAppProductDto(String productId, String productName, Double price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }
}
