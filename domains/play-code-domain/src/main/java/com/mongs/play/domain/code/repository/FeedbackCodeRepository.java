package com.mongs.play.domain.code.repository;

import com.mongs.play.domain.code.entity.FeedbackCode;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FeedbackCodeRepository extends MongoRepository<FeedbackCode, String> {
    List<FeedbackCode> findByBuildVersionIsLessThanEqual(String buildVersion);
}
