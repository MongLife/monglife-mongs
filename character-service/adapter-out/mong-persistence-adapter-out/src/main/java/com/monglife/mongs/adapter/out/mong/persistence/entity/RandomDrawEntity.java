package com.monglife.mongs.adapter.out.mong.persistence.entity;

import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import com.monglife.mongs.domain.mong.enums.InventoryTypeCode;
import com.monglife.mongs.domain.mong.model.RandomDraw;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_random_draw")
public class RandomDrawEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "random_draw_id")
    private Long randomDrawId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "random_draw_code")
    private ComnCodeEntity comn;

    @Enumerated(EnumType.STRING)
    @Column(name = "inventory_type_code")
    private InventoryTypeCode inventoryTypeCode;

    @Builder
    public RandomDrawEntity(Long randomDrawId, ComnCodeEntity comn, InventoryTypeCode inventoryTypeCode) {
        this.randomDrawId = randomDrawId;
        this.comn = comn;
        this.inventoryTypeCode = inventoryTypeCode;
    }

    public RandomDraw toDomain() {
        return RandomDraw.builder()
                .randomDrawId(this.randomDrawId)
                .randomDrawCode(this.comn.getCode())
                .randomDrawName(this.comn.getName())
                .inventoryTypeCode(this.inventoryTypeCode)
                .build();
    }
}
