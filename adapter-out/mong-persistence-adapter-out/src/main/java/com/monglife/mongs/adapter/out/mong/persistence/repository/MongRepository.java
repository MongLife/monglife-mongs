package com.monglife.mongs.adapter.out.mong.persistence.repository;

import com.monglife.mongs.adapter.out.mong.persistence.entity.MongEntity;
import com.monglife.mongs.adapter.out.mong.persistence.repository.dsl.MongDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MongRepository extends JpaRepository<MongEntity, Long>, MongDslRepository {
}
