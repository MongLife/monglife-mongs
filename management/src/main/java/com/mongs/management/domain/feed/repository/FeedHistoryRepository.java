package com.mongs.management.domain.feed.repository;

import com.mongs.management.domain.feed.entity.FeedHistory;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedHistoryRepository extends MongoRepository<FeedHistory, Long> {
    @Aggregation(pipeline = {
            "{ $match: { mongId: ?0 } }",
            "{ $group: { _id: '$mongCode', latestData: { $last: '$$ROOT' } } }",
            "{ $replaceRoot: { newRoot: '$latestData' } }"
    })
    List<FeedHistory> findByMongIdOrderByBuyAt(@Param("mongId") Long mongId);
}
