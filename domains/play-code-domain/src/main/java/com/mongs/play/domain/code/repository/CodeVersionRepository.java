package com.mongs.play.domain.code.repository;

import com.mongs.play.domain.code.entity.CodeVersion;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeVersionRepository extends JpaRepository<CodeVersion, String> {
    @Query("SELECT cv FROM CodeVersion cv WHERE cv.buildVersion = :buildVersion")
    Optional<CodeVersion> findByBuildVersion(@Param("buildVersion") String buildVersion);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT cv FROM CodeVersion cv WHERE cv.buildVersion = :buildVersion")
    Optional<CodeVersion> findByBuildVersionWithLock(@Param("buildVersion") String buildVersion);

    @Query("SELECT cv FROM CodeVersion cv WHERE cv.buildVersion < :buildVersion")
    List<CodeVersion> findByBuildVersionIsBefore(String buildVersion);
}
