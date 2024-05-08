package com.mongs.play.module.code.repository;

import com.mongs.play.module.code.entity.FoodCode;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FoodCodeRepository extends MongoRepository<FoodCode, String> {
    List<FoodCode> findByBuildVersionIsLessThanEqual(String buildVersion);
}
