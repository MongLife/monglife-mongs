package com.monglife.mongs.adapter.out.mong.persistence.repository;

import com.monglife.mongs.adapter.out.mong.persistence.entity.InventoryEntity;
import com.monglife.mongs.adapter.out.mong.persistence.repository.dsl.InventoryDslRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long>, InventoryDslRepository {

    Page<InventoryEntity> findByMongId(Pageable pageable, Long mongId);
}
