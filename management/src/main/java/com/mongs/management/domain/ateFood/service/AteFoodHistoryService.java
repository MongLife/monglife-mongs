package com.mongs.management.domain.ateFood.service;

import com.mongs.management.domain.ateFood.service.dto.FoodHistory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AteFoodHistoryService {
    List<FoodHistory> findAteFoodHistory(Long mongId, Long version);
}
