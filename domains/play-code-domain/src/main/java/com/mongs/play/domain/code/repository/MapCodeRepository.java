package com.mongs.play.domain.code.repository;

import com.mongs.play.domain.code.entity.MapCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapCodeRepository extends JpaRepository<MapCode, String> {
    @Query("SELECT mc FROM MapCode mc WHERE mc.buildVersion <= :buildVersion")
    List<MapCode> findByBuildVersionIsLessThanEqual(@Param("buildVersion") String buildVersion);
}
