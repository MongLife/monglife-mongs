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

    @Builder
    public GetProductDto(String productId) {
        this.productId = productId;
    }
}
