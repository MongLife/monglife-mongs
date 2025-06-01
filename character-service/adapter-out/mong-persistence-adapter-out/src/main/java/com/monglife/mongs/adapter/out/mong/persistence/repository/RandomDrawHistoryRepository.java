package com.monglife.mongs.adapter.out.mong.persistence.repository;

import com.monglife.mongs.adapter.out.mong.persistence.entity.RandomDrawHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RandomDrawHistoryRepository extends JpaRepository<RandomDrawHistoryEntity, Long> {
}
