package com.monglife.mongs.application.mong.port.out.vo;

import com.monglife.mongs.domain.enums.InventoryItemTypeCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateInventoryItemVo {

    private final Long mongId;

    private final String typeCode;

    private final InventoryItemTypeCode inventoryItemTypeCode;

    @Builder
    public CreateInventoryItemVo(Long mongId, String typeCode, InventoryItemTypeCode inventoryItemTypeCode) {
        this.mongId = mongId;
        this.typeCode = typeCode;
        this.inventoryItemTypeCode = inventoryItemTypeCode;
    }
}
