package com.monglife.mongs.adapter.out.mong.persistence.repository;

import com.monglife.mongs.adapter.out.mong.persistence.entity.RandomDrawEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RandomDrawRepository extends JpaRepository<RandomDrawEntity, Long> {
}
