package com.mongs.management.domain.feedHistory.service;

import com.mongs.management.domain.feedHistory.service.vo.FindFeedHistoryVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FeedHistoryService {
    List<FindFeedHistoryVo> findFeedHistory(Long mongId);

}
