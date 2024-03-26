package com.mongs.member.domain.collection.repository;

import com.mongs.core.entity.MongCode;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongCodeRepository extends MongoRepository<MongCode, String> {
}
