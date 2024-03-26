package com.mongs.common.repository;

import com.mongs.core.entity.MongCode;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongCodeRepository extends MongoRepository<MongCode, String> {
}
