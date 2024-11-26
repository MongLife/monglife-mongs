package com.monglife.mongs.app.manager.management.repository;

import com.monglife.mongs.app.manager.management.domain.MongFeedHistoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MongFeedHistoryRepository  extends CrudRepository<MongFeedHistoryEntity, Long> {

    Optional<MongFeedHistoryEntity> findByMongIdAndFoodTypeCode(Long mongId, String foodTypeCode);
}
