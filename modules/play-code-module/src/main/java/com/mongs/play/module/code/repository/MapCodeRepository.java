package com.mongs.play.module.code.repository;

import com.mongs.play.module.code.entity.MapCode;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MapCodeRepository extends MongoRepository<MapCode, String> {
    List<MapCode> findByBuildVersionIsLessThanEqual(String buildVersion);
}
