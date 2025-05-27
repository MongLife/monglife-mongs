package com.monglife.mongs.adapter.out.mong.persistence.repository;

import com.monglife.mongs.adapter.out.mong.persistence.entity.MongFeedHistoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MongFeedHistoryRepository  extends CrudRepository<MongFeedHistoryEntity, Long> {

    List<MongFeedHistoryEntity> findByMongId(Long mongId);

    Optional<MongFeedHistoryEntity> findByMongIdAndCode(Long mongId, String typeCode);
}
