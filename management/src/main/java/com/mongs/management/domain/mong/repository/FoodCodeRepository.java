package com.mongs.management.domain.mong.repository;

import com.mongs.core.entity.FoodCode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FoodCodeRepository extends MongoRepository<FoodCode, String> {
    Optional<FoodCode> findByCode(String code);
    @Query("{ 'version': { '$lte' : ?0 } }")
    List<FoodCode> findByVersion(Long version);
}
