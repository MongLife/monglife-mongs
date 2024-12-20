package com.monglife.mongs.app.user.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateProductOrderRequestDto {

    @NotBlank
    private String productId;

    @Builder
    public CreateProductOrderRequestDto(String productId) {
        this.productId = productId;
    }
}
