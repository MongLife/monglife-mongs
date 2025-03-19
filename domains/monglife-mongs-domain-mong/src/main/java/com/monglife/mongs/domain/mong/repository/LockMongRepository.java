package com.monglife.mongs.domain.mong.repository;

import com.monglife.mongs.domain.mong.entity.MongEntity;
import com.monglife.mongs.domain.mong.repositoryCustom.LockMongDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LockMongRepository extends JpaRepository<MongEntity, Long>, LockMongDslRepository {
}

