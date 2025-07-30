package com.monglife.mongs.adapter.out.mong.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.mong.persistence.entity.InventoryEntity;

import java.util.Optional;

public interface InventoryDslRepository {

    Optional<InventoryEntity> findByIdWithLock(Long inventoryId);
}
