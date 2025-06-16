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

    private Long mongId;

    private String inventoryCode;

    private String inventoryName;

    private InventoryTypeCode inventoryTypeCode;

    @Builder
    public GetInventoryResponseDto(Long inventoryId, Long mongId, String inventoryCode, String inventoryName, InventoryTypeCode inventoryTypeCode) {
        this.inventoryId = inventoryId;
        this.mongId = mongId;
        this.inventoryCode = inventoryCode;
        this.inventoryName = inventoryName;
        this.inventoryTypeCode = inventoryTypeCode;
    }
}
