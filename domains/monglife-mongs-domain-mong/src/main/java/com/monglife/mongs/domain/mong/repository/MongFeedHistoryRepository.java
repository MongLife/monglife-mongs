package com.monglife.mongs.domain.mong.repository;

import com.monglife.mongs.domain.mong.entity.history.MongFeedHistoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MongFeedHistoryRepository  extends CrudRepository<MongFeedHistoryEntity, Long> {

    Optional<MongFeedHistoryEntity> findByMongIdAndFoodTypeCode(Long mongId, String foodTypeCode);
}
