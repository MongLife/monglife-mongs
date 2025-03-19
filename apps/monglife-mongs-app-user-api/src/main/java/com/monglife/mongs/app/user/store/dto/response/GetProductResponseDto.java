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

    @Builder
    public GetProductResponseDto(String productId) {
        this.productId = productId;
    }
}
