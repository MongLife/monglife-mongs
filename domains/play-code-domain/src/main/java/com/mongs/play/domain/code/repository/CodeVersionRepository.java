package com.mongs.play.domain.code.repository;

import com.mongs.play.domain.code.entity.CodeVersion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CodeVersionRepository extends MongoRepository<CodeVersion, String> {
    Optional<CodeVersion> findByBuildVersion(String buildVersion);
    List<CodeVersion> findByBuildVersionIsBefore(String buildVersion);
}