package com.monglife.mongs.domain.mong.model;

import com.monglife.mongs.domain.mong.enums.InventoryTypeCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RandomDraw {

    private final Long randomDrawId;

    private final String randomDrawCode;

    private final String randomDrawName;

    private final InventoryTypeCode inventoryTypeCode;

    @Builder
    public RandomDraw(Long randomDrawId, String randomDrawCode, String randomDrawName, InventoryTypeCode inventoryTypeCode) {
        this.randomDrawId = randomDrawId;
        this.randomDrawCode = randomDrawCode;
        this.randomDrawName = randomDrawName;
        this.inventoryTypeCode = inventoryTypeCode;
    }
}
