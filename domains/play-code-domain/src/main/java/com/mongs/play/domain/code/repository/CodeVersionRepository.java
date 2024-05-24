package com.mongs.play.domain.code.repository;

import com.mongs.play.domain.code.entity.CodeVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CodeVersionRepository extends JpaRepository<CodeVersion, String> {
    Optional<CodeVersion> findByBuildVersion(String buildVersion);
    List<CodeVersion> findByBuildVersionIsBefore(String buildVersion);
}
