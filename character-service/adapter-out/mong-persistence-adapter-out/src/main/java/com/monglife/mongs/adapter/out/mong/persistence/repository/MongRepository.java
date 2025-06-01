package com.monglife.mongs.adapter.out.mong.persistence.repository;

import com.monglife.mongs.adapter.out.mong.persistence.entity.MongEntity;
import com.monglife.mongs.adapter.out.mong.persistence.repository.dsl.MongDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MongRepository extends JpaRepository<MongEntity, Long>, MongDslRepository {

    Optional<MongEntity> findByMongId(Long mongId);

    List<MongEntity> findAllByAccountId(Long accountId);
}
