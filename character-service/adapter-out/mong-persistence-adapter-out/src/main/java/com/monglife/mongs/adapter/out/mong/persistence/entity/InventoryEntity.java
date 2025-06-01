package com.monglife.mongs.adapter.out.mong.persistence.entity;

import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import com.monglife.mongs.domain.mong.enums.InventoryTypeCode;
import com.monglife.mongs.domain.mong.model.Inventory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_inventory")
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long inventoryId;

    @Column(name = "mong_id")
    private Long mongId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_code")
    private ComnCodeEntity comn;

    @Enumerated(EnumType.STRING)
    @Column(name = "inventory_type_code")
    private InventoryTypeCode inventoryTypeCode;

    @Builder
    public InventoryEntity(Long inventoryId, Long mongId, ComnCodeEntity comn, InventoryTypeCode inventoryTypeCode) {
        this.inventoryId = inventoryId;
        this.mongId = mongId;
        this.comn = comn;
        this.inventoryTypeCode = inventoryTypeCode;
    }

    public Inventory toDomain() {
        return Inventory.builder()
                .inventoryId(this.inventoryId)
                .mongId(this.mongId)
                .inventoryCode(this.comn.getCode())
                .inventoryName(this.comn.getName())
                .inventoryTypeCode(this.inventoryTypeCode)
                .build();
    }
}
