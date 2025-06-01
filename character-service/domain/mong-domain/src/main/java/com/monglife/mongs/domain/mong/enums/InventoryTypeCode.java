package com.monglife.mongs.domain.mong.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InventoryTypeCode {
    FOOD("인벤토리 음식 타입"),
    SNACK("인벤토리 간식 타입"),
    MAP("인벤토리 맵 타입"),
    ;

    private final String description;
}
