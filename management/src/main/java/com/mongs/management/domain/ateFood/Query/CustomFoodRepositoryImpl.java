package com.mongs.management.domain.ateFood.Query;

import com.mongs.core.code.entity.FoodCode;
import com.mongs.management.domain.food.service.dto.FoodCategory;
import com.mongs.management.exception.ManagementErrorCode;
import com.mongs.management.exception.ManagementException;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.mongs.management.domain.ateFood.QAteFoodHistory.ateFoodHistory;


@RequiredArgsConstructor
public class CustomFoodRepositoryImpl implements CustomFoodRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public FoodCategory foodList(FoodCode foodCode) {
        Tuple result = queryFactory
                .select(ateFoodHistory.foodName, ateFoodHistory.createdAt)
                .from(ateFoodHistory)
                .where(ateFoodHistory.foodName.eq(foodCode.name()))
                .orderBy(ateFoodHistory.createdAt.desc())
                .fetchOne();

        if (result == null) {
            throw new ManagementException(ManagementErrorCode.NOT_FOUND);
        }
        return new FoodCategory(
                foodCode.code(),
                result.get(ateFoodHistory.foodName),
                foodCode.point(),
                result.get(ateFoodHistory.createdAt)
        );
    }

}
