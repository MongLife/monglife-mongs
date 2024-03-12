package com.mongs.management.domain.ateFood.repository;

import com.mongs.management.domain.ateFood.entity.AteFoodHistory;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AteFoodHistoryRepository extends MongoRepository<AteFoodHistory, Long> {
    @Aggregation(pipeline = {
            "{ $group: { _id: '$code', latestData: { $last: '$$ROOT' } } }",
            "{ $replaceRoot: { newRoot: '$latestData' } }"
    })
    List<AteFoodHistory> findByMongIdOrderByBuyAt(Long mongId);
}
