package com.mongs.management.domain.feedHistory.repository;

import com.mongs.management.domain.feedHistory.entity.FeedHistory;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FeedHistoryRepository extends MongoRepository<FeedHistory, Long> {
    @Aggregation(pipeline = {
            "{ $group: { _id: '$mongCode', latestData: { $last: '$$ROOT' } } }",
            "{ $replaceRoot: { newRoot: '$latestData' } }"
    })
    List<FeedHistory> findByMongIdOrderByBuyAt(Long mongId);
}