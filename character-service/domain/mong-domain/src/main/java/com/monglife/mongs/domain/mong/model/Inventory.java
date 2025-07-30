package com.monglife.mongs.domain.mong.model;

import com.monglife.mongs.domain.mong.enums.InventoryTypeCode;
import com.monglife.mongs.domain.mong.exception.ForbiddenInventoryItemException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Inventory {

    private final Long inventoryId;

    private final Long mongId;

    private final String inventoryCode;

    private final String inventoryName;

    private final InventoryTypeCode inventoryTypeCode;

    @Builder
    public Inventory(Long inventoryId, Long mongId, String inventoryCode, String inventoryName, InventoryTypeCode inventoryTypeCode) {
        this.inventoryId = inventoryId;
        this.mongId = mongId;
        this.inventoryCode = inventoryCode;
        this.inventoryName = inventoryName;
        this.inventoryTypeCode = inventoryTypeCode;
    }

    /**
     * 인벤토리 아이템 권한 확인
     * @param mongId 몽 ID
     * @return 몽 도메인 객체
     */
    public Inventory verify(Long mongId) {

        if (!this.mongId.equals(mongId)) {
            throw new ForbiddenInventoryItemException();
        }

        return this;
    }
}
