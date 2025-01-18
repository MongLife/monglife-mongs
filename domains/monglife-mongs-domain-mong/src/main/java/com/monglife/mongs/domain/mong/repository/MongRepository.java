package com.monglife.mongs.domain.mong.repository;

import com.monglife.mongs.domain.mong.entity.MongEntity;
import com.monglife.mongs.domain.mong.repositoryCustom.MongDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MongRepository extends JpaRepository<MongEntity, Long>, MongDslRepository {
}
