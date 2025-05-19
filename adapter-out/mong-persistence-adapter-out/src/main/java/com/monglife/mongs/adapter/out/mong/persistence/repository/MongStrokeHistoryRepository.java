package com.monglife.mongs.adapter.out.mong.persistence.repository;

import com.monglife.mongs.adapter.out.mong.persistence.entity.MongStrokeHistoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MongStrokeHistoryRepository extends CrudRepository<MongStrokeHistoryEntity, Long> {

    Optional<MongStrokeHistoryEntity> findByMongId(Long mongId);
}
