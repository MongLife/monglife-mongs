package com.mongs.common.repository;

import com.mongs.core.entity.MapCode;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MapCodeRepository extends MongoRepository<MapCode, String> {
    List<MapCode> findByVersionIsAfter(Long version);
}
