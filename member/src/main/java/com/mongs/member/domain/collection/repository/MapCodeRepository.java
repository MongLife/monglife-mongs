package com.mongs.member.domain.collection.repository;

import com.mongs.core.entity.MapCode;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MapCodeRepository extends MongoRepository<MapCode, String> {
}
