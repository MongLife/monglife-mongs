package com.monglife.mongs.adapter.out.mong.persistence.repository;

import com.monglife.mongs.adapter.out.mong.persistence.entity.InventoryEntity;
import com.monglife.mongs.adapter.out.mong.persistence.repository.dsl.InventoryItemDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryItemRepository extends JpaRepository<InventoryEntity, Long>, InventoryItemDslRepository {

    List<InventoryEntity> findByMongId(Long mongId);
}
