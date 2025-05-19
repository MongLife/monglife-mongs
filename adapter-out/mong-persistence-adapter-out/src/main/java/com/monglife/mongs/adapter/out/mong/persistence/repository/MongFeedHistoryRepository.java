package com.monglife.mongs.adapter.out.mong.persistence.repository;

import com.monglife.mongs.adapter.out.mong.persistence.entity.MongFeedHistoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MongFeedHistoryRepository  extends CrudRepository<MongFeedHistoryEntity, Long> {

    Optional<MongFeedHistoryEntity> findByMongIdAndFoodTypeCode(Long mongId, String foodTypeCode);
}
