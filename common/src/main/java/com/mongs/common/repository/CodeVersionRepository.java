package com.mongs.common.repository;

import com.mongs.common.entity.CodeVersion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CodeVersionRepository extends MongoRepository<CodeVersion, String> {
    Optional<CodeVersion> findTopByOrderByCreatedAtDesc();
}
