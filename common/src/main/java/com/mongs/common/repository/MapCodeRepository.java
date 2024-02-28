package com.mongs.common.repository;

import com.mongs.core.code.entity.MapCode;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MapCodeRepository extends MongoRepository<MapCode, String> {
}
