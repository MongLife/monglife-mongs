package com.mongs.common.repository;

import com.mongs.core.entity.FoodCode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface FoodCodeRepository extends MongoRepository<FoodCode, String> {
}
