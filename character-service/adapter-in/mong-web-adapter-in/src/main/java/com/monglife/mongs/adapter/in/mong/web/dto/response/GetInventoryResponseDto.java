package com.monglife.mongs.adapter.in.mong.web.dto.response;

import com.monglife.mongs.domain.mong.enums.InventoryTypeCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetInventoryResponseDto {

    private Long inventoryId;

    private String inventoryCode;

    private String inventoryName;

    private InventoryTypeCode inventoryTypeCode;

    @Builder
    public GetInventoryResponseDto(Long inventoryId, String inventoryCode, String inventoryName, InventoryTypeCode inventoryTypeCode) {
        this.inventoryId = inventoryId;
        this.inventoryCode = inventoryCode;
        this.inventoryName = inventoryName;
        this.inventoryTypeCode = inventoryTypeCode;
    }
}
