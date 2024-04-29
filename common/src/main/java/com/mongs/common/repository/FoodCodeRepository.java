package com.mongs.common.repository;

import com.mongs.core.entity.FoodCode;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FoodCodeRepository extends MongoRepository<FoodCode, String> {
    List<FoodCode> findByBuildVersionIsLessThanEqual(String buildVersion);
}
