package com.mongs.management.domain.ateFood;

import com.mongs.management.domain.food.service.dto.FoodCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AteFoodHistoryController {

    private final AteFoodHistoryService ateFoodHistoryService;

    @GetMapping("/information/lastbuy")
    public ResponseEntity<FoodCategory> foodList (@Param("foodCode") String foodCode, @Param("mongId")Long mongId) {
        return ResponseEntity.status(HttpStatus.OK).body(ateFoodHistoryService.foodList(foodCode, mongId));
    }
}
