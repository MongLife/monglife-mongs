package com.mongs.management.domain.ateFood.service;

import com.mongs.management.domain.ateFood.entity.AteFoodHistory;
import com.mongs.management.domain.ateFood.service.dto.FoodHistoryResDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AteFoodHistoryService {
    List<FoodHistoryResDto> findAteFoodHistory(Long mongId, Long version);
}
