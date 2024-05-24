package com.mongs.play.domain.code.repository;

import com.mongs.play.domain.code.entity.MapCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MapCodeRepository extends JpaRepository<MapCode, String> {
    List<MapCode> findByBuildVersionIsLessThanEqual(String buildVersion);
}
