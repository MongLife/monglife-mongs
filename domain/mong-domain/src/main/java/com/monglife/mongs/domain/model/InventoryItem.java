package com.monglife.mongs.domain.model;

import com.monglife.mongs.domain.enums.InventoryItemTypeCode;
import com.monglife.mongs.domain.exception.ForbiddenInventoryItemException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class InventoryItem {

    private final Long inventoryItemId;

    private final Long mongId;

    private final String typeCode;

    private final String typeName;

    private final InventoryItemTypeCode inventoryItemTypeCode;

    @Builder
    public InventoryItem(Long inventoryItemId, Long mongId, String typeCode, String typeName, InventoryItemTypeCode inventoryItemTypeCode) {
        this.inventoryItemId = inventoryItemId;
        this.mongId = mongId;
        this.typeCode = typeCode;
        this.typeName = typeName;
        this.inventoryItemTypeCode = inventoryItemTypeCode;
    }

    /**
     * 인벤토리 아이템 권한 확인
     * @param mongId 몽 ID
     * @return 몽 도메인 객체
     */
    public InventoryItem verify(Long mongId) {

        if (!this.mongId.equals(mongId)) {
            throw new ForbiddenInventoryItemException();
        }

        return this;
    }
}
