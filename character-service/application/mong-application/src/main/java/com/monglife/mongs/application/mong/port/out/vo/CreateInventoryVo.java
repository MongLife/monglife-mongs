package com.monglife.mongs.application.mong.port.out.vo;

import com.monglife.mongs.domain.mong.enums.InventoryTypeCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateInventoryVo {

    private final Long mongId;

    private final String typeCode;

    private final InventoryTypeCode inventoryTypeCode;

    @Builder
    public CreateInventoryVo(Long mongId, String typeCode, InventoryTypeCode inventoryTypeCode) {
        this.mongId = mongId;
        this.typeCode = typeCode;
        this.inventoryTypeCode = inventoryTypeCode;
    }
}
