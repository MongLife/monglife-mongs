package com.monglife.mongs.adapter.out.mong.persistence.repository;

import com.monglife.mongs.adapter.out.mong.persistence.entity.RandomDrawEntity;
import com.monglife.mongs.adapter.out.mong.persistence.repository.dsl.RandomDrawDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RandomDrawRepository extends JpaRepository<RandomDrawEntity, Long>, RandomDrawDslRepository {
}
