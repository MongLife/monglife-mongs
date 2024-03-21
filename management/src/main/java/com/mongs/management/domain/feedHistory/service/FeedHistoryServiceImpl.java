package com.mongs.management.domain.feedHistory.service;

import com.mongs.core.entity.FoodCode;
import com.mongs.management.domain.feedHistory.entity.FeedHistory;
import com.mongs.management.domain.feedHistory.repository.FeedHistoryRepository;
import com.mongs.management.domain.feedHistory.service.vo.FindFeedHistoryVo;
import com.mongs.management.domain.mong.repository.FoodCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedHistoryServiceImpl implements FeedHistoryService {

    private final FoodCodeRepository foodCodeRepository;
    private final FeedHistoryRepository feedHistoryRepository;

    @Override
    @Transactional
    public List<FindFeedHistoryVo> findFeedHistory(Long mongId) {
        Map<String, FeedHistory> feedHistoryMap = feedHistoryRepository.findByMongIdOrderByBuyAt(mongId).stream()
                .collect(Collectors.toMap(FeedHistory::getCode, feedHistory -> feedHistory));

        List<FoodCode> foodCodeList = foodCodeRepository.findAll();

        List<FindFeedHistoryVo> findFeedHistoryVoList = FindFeedHistoryVo.toList(foodCodeList);

        return findFeedHistoryVoList.stream()
                .map(findFeedHistoryVo -> {
                    if (feedHistoryMap.containsKey(findFeedHistoryVo.code())) {
                        return findFeedHistoryVo.toBuilder()
                                .lastBuyAt(feedHistoryMap.get(findFeedHistoryVo.code()).getBuyAt())
                                .build();
                    } else {
                        return findFeedHistoryVo.toBuilder()
                                .lastBuyAt(LocalDateTime.now())
                                .build();
                    }
                }).toList();
    }
}
