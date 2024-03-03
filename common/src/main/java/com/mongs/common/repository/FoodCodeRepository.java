package com.mongs.common.repository;

import com.mongs.core.code.entity.FoodCode;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FoodCodeRepository extends MongoRepository<FoodCode, String> {
}
