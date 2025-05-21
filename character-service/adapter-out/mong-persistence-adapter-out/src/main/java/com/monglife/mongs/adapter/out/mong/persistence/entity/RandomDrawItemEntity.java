package com.monglife.mongs.adapter.out.mong.persistence.entity;

import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import com.monglife.mongs.domain.mong.enums.InventoryItemTypeCode;
import com.monglife.mongs.domain.mong.model.RandomDrawItem;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_random_draw_item")
public class RandomDrawItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "random_draw_item_id")
    private Long randomDrawItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_code")
    private ComnCodeEntity type;

    @Enumerated(EnumType.STRING)
    @Column(name = "inventory_item_type_code")
    private InventoryItemTypeCode inventoryItemTypeCode;

    @Builder
    public RandomDrawItemEntity(Long randomDrawItemId, ComnCodeEntity type, InventoryItemTypeCode inventoryItemTypeCode) {
        this.randomDrawItemId = randomDrawItemId;
        this.type = type;
        this.inventoryItemTypeCode = inventoryItemTypeCode;
    }

    public RandomDrawItem toDomain() {
        return RandomDrawItem.builder()
                .randomDrawItemId(this.randomDrawItemId)
                .typeCode(this.type.getCode())
                .typeName(this.type.getName())
                .inventoryItemTypeCode(this.inventoryItemTypeCode)
                .build();
    }
}
