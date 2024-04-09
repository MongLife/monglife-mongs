package com.mongs.management.domain.feed.service;

import com.mongs.management.domain.feed.service.vo.FeedMongVo;
import com.mongs.management.domain.feed.service.vo.FindFeedHistoryVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FeedService {
    List<FindFeedHistoryVo> findFeedHistory(Long mongId);
    FeedMongVo feedMong(Long accountId, Long mongId, String foodCode);
}
