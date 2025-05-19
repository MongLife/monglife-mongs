package com.monglife.mongs.adapter.out.mong.persistence.repository;

import com.monglife.mongs.adapter.out.mong.persistence.entity.MongEntity;
import com.monglife.mongs.adapter.out.mong.persistence.repository.dsl.LockMongDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LockMongRepository extends JpaRepository<MongEntity, Long>, LockMongDslRepository {
}

