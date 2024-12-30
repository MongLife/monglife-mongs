package com.monglife.mongs.domain.member.dto.etc;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetProductOrderDto {

    private Long accountId;

    private String productId;

    @Builder
    public GetProductOrderDto(Long accountId, String productId) {
        this.accountId = accountId;
        this.productId = productId;
    }
}
