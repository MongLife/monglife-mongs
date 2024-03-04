package com.mongs.management.domain.food.Query;

import com.mongs.management.domain.food.service.dto.FoodCategory;
import com.querydsl.core.types.Projections;
import lombok.RequiredArgsConstructor;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mongs.management.domain.food.QFood.food;
import static com.mongs.management.domain.ateFood.QAteFoodHistory.ateFoodHistory;

@RequiredArgsConstructor
public class CustomFoodRepositoryImpl implements CustomFoodRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<String, List<FoodCategory>> findFoodCategories() {
        List<FoodCategory> results = queryFactory
                .select(Projections.constructor(FoodCategory.class,
                        food.foodCode.concat(food.detailCode),
                        food.name,
                        food.price,
                        ateFoodHistory.createdAt.max()))
                .from(food)
                .join(ateFoodHistory).on(food.name.eq(ateFoodHistory.foodName))
                .fetch();

        return results.stream()
                .collect(Collectors.groupingBy(FoodCategory::foodCode));
    }

    @Override
    public List<FoodCategory> foodList(String foodCode) {
        return queryFactory
                .select(Projections.constructor(FoodCategory.class,
                        food.foodCode.concat(food.detailCode),
                        food.name,
                        food.price,
                        ateFoodHistory.createdAt.max()))
                .from(food)
                .join(ateFoodHistory).on(food.name.eq(ateFoodHistory.foodName))
                .where(food.foodCode.eq(foodCode))
                .fetch();
    }

}
