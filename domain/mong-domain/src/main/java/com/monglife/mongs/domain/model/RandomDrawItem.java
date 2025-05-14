package com.monglife.mongs.domain.model;

import com.monglife.mongs.domain.enums.InventoryItemTypeCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RandomDrawItem {

    private Long randomDrawItemId;

    private String typeCode;

    private String typeName;

    private InventoryItemTypeCode inventoryItemTypeCode;

    @Builder
    public RandomDrawItem(Long randomDrawItemId, String typeCode, String typeName, InventoryItemTypeCode inventoryItemTypeCode) {
        this.randomDrawItemId = randomDrawItemId;
        this.typeCode = typeCode;
        this.typeName = typeName;
        this.inventoryItemTypeCode = inventoryItemTypeCode;
    }
}
