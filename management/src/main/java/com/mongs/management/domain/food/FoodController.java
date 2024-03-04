package com.mongs.management.domain.food;

import com.mongs.management.domain.food.service.FoodService;
import com.mongs.management.domain.food.service.dto.FoodCategory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/food")
public class FoodController {

    private final FoodService foodService;

    // 전체 음식 목록
    @GetMapping("/all")
    public ResponseEntity<Map<String, List<FoodCategory>>> foodCategory() {
        return ResponseEntity.status(HttpStatus.OK).body(foodService.foodCategory());
    }

    // 카테고리별 음식 불러오기 (PathVariable foodCategory)
    @GetMapping("/{foodCategory}")
    public ResponseEntity<List<FoodCategory>> foodList(@PathVariable String foodCategory) {
        return ResponseEntity.status(HttpStatus.OK).body(foodService.foodList(foodCategory));
    }
}
