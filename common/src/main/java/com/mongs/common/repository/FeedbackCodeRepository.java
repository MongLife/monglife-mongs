package com.mongs.common.repository;

import com.mongs.core.entity.FeedbackCode;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FeedbackCodeRepository extends MongoRepository<FeedbackCode, String> {
}
