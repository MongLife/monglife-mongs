package com.monglife.mongs.adapter.in.mong.web.dto.response;

import com.monglife.mongs.domain.mong.enums.InventoryTypeCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RandomDrawResponseDto {

    private String randomDrawCode;

    private String randomDrawName;

    private InventoryTypeCode inventoryTypeCode;

    @Builder
    public RandomDrawResponseDto(String randomDrawCode, String randomDrawName, InventoryTypeCode inventoryTypeCode) {
        this.randomDrawCode = randomDrawCode;
        this.randomDrawName = randomDrawName;
        this.inventoryTypeCode = inventoryTypeCode;
    }
}
