package com.mongs.common.repository;

import com.mongs.common.entity.CodeVersion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CodeVersionRepository extends MongoRepository<CodeVersion, String> {
    Optional<CodeVersion> findByBuildVersion(String buildVersion);
    List<CodeVersion> findByBuildVersionIsBefore(String buildVersion);
}
