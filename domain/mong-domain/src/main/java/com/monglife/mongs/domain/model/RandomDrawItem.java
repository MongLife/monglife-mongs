package com.monglife.mongs.domain.model;

import com.monglife.mongs.domain.enums.InventoryItemTypeCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RandomDrawItem {

    private final Long randomDrawItemId;

    private final String typeCode;

    private final String typeName;

    private final InventoryItemTypeCode inventoryItemTypeCode;

    @Builder
    public RandomDrawItem(Long randomDrawItemId, String typeCode, String typeName, InventoryItemTypeCode inventoryItemTypeCode) {
        this.randomDrawItemId = randomDrawItemId;
        this.typeCode = typeCode;
        this.typeName = typeName;
        this.inventoryItemTypeCode = inventoryItemTypeCode;
    }
}
