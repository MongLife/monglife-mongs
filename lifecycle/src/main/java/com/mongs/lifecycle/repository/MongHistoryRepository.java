package com.mongs.lifecycle.repository;

import com.mongs.core.entity.MongHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongHistoryRepository extends MongoRepository<MongHistory, String> {
}
