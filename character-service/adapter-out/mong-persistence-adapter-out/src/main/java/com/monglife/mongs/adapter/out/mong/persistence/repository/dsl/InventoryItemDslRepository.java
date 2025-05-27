package com.monglife.mongs.adapter.out.mong.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.mong.persistence.entity.InventoryEntity;

import java.util.Optional;

public interface InventoryItemDslRepository {

    Optional<InventoryEntity> findByIdWithLock(Long inventoryItemId);
}
