package com.mongs.management.domain.ateFood;

import com.mongs.management.domain.ateFood.Query.CustomFoodRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AteFoodHistoryRepository extends JpaRepository<AteFoodHistory, Long>, CustomFoodRepository {
}
