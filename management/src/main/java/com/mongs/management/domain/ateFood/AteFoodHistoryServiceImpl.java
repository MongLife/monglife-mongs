package com.mongs.management.domain.ateFood;

import com.mongs.common.repository.FoodCodeRepository;
import com.mongs.core.code.entity.FoodCode;
import com.mongs.management.domain.food.service.dto.FoodCategory;
import com.mongs.management.domain.mong.repository.MongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class AteFoodHistoryServiceImpl implements AteFoodHistoryService{

    private final AteFoodHistoryRepository ateFoodHistoryRepository;
    private final MongRepository mongRepository;
    private final FoodCodeRepository foodCodeRepository;

    @Override
    @Transactional
    public FoodCategory foodList (String foodCode, Long mongId) {
        mongRepository.findById(mongId).orElseThrow();
        FoodCode code = foodCodeRepository.findByCode(foodCode).orElseThrow();
        return ateFoodHistoryRepository.foodList(code);
    }
}
