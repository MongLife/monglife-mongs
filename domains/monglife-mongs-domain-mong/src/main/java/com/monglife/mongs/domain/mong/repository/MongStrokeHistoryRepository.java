package com.monglife.mongs.domain.mong.repository;

import com.monglife.mongs.domain.mong.entity.history.MongStrokeHistoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MongStrokeHistoryRepository extends CrudRepository<MongStrokeHistoryEntity, Long> {

    Optional<MongStrokeHistoryEntity> findByMongId(Long mongId);
}
