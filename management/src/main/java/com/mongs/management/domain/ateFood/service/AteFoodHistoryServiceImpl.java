package com.mongs.management.domain.ateFood.service;

import com.mongs.management.domain.ateFood.entity.AteFoodHistory;
import com.mongs.management.domain.ateFood.repository.AteFoodHistoryRepository;
import com.mongs.management.domain.ateFood.service.dto.FoodHistory;
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
public class AteFoodHistoryServiceImpl implements AteFoodHistoryService {

    private final FoodCodeRepository foodCodeRepository;
    private final AteFoodHistoryRepository ateFoodHistoryRepository;

    @Override
    @Transactional
    public List<FoodHistory> findAteFoodHistory(Long mongId, Long version) {
        Map<String, AteFoodHistory> ateFoodHistoryMap = ateFoodHistoryRepository.findByMongIdOrderByBuyAt(mongId).stream()
                .collect(Collectors.toMap(AteFoodHistory::getCode, ateFoodHistory -> ateFoodHistory));

        log.info(ateFoodHistoryMap.toString());
        return FoodHistory.toList(foodCodeRepository.findByVersion(version)).stream()
                .map(foodHistory -> ateFoodHistoryMap.containsKey(foodHistory.code())
                        ? foodHistory.toBuilder()
                            .lastBuyAt(ateFoodHistoryMap.get(foodHistory.code()).getBuyAt())
                            .build()
                        : foodHistory.toBuilder()
                            .lastBuyAt(LocalDateTime.now())
                            .build())
                .toList();
    }
}
