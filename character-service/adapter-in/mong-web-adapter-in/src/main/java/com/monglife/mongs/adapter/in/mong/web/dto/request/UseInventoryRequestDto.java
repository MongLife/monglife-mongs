package com.monglife.mongs.adapter.in.mong.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UseInventoryRequestDto {

    @Min(1)
    @NotNull
    private Long inventoryId;

    @Builder
    public UseInventoryRequestDto(Long inventoryId) {
        this.inventoryId = inventoryId;
    }
}
