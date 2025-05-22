package com.monglife.mongs.adapter.out.mong.persistence.entity;

import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import com.monglife.mongs.domain.mong.enums.InventoryItemTypeCode;
import com.monglife.mongs.domain.mong.model.InventoryItem;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_inventory_item")
@ToString
public class InventoryItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_item_id")
    private Long inventoryItemId;

    @Column(name = "mong_id")
    private Long mongId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_code")
    private ComnCodeEntity type;

    @Enumerated(EnumType.STRING)
    @Column(name = "inventory_item_type_code")
    private InventoryItemTypeCode inventoryItemTypeCode;

    @Builder
    public InventoryItemEntity(Long inventoryItemId, Long mongId, ComnCodeEntity type, InventoryItemTypeCode inventoryItemTypeCode) {
        this.inventoryItemId = inventoryItemId;
        this.mongId = mongId;
        this.type = type;
        this.inventoryItemTypeCode = inventoryItemTypeCode;
    }

    public InventoryItem toDomain() {
        return InventoryItem.builder()
                .inventoryItemId(this.inventoryItemId)
                .mongId(this.mongId)
                .typeCode(this.type.getCode())
                .typeName(this.type.getName())
                .inventoryItemTypeCode(this.inventoryItemTypeCode)
                .build();
    }
}
