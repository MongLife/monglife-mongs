package com.monglife.mongs.adapter.out.mong.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.mong.persistence.entity.InventoryItemEntity;

import java.util.Optional;

public interface InventoryItemDslRepository {

    Optional<InventoryItemEntity> findByIdWithLock(Long inventoryItemId);
}
