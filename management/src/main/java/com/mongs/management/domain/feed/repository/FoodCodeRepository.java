package com.mongs.management.domain.feed.repository;

import com.mongs.core.entity.FoodCode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FoodCodeRepository extends MongoRepository<FoodCode, String> {
    Optional<FoodCode> findByCode(String code);
    List<FoodCode> findByBuildVersionIsLessThanEqual(String buildVersion);
}
