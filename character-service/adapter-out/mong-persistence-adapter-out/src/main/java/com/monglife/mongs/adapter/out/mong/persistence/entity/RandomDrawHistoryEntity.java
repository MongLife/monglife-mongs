package com.monglife.mongs.adapter.out.mong.persistence.entity;

import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import com.monglife.mongs.domain.mong.enums.InventoryTypeCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_random_draw_history")
public class RandomDrawHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "random_draw_history_id")
    private Long randomDrawHistoryId;

    @Column(name = "account_id")
    private Long accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "random_draw_code")
    private ComnCodeEntity comn;

    @Enumerated(EnumType.STRING)
    @Column(name = "inventory_type_code")
    private InventoryTypeCode inventoryTypeCode;

    @Builder
    public RandomDrawHistoryEntity(Long randomDrawHistoryId, Long accountId, ComnCodeEntity comn, InventoryTypeCode inventoryTypeCode) {
        this.randomDrawHistoryId = randomDrawHistoryId;
        this.accountId = accountId;
        this.comn = comn;
        this.inventoryTypeCode = inventoryTypeCode;
    }
}
