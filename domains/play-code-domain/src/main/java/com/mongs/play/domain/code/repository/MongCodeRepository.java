package com.mongs.play.domain.code.repository;

import com.mongs.play.domain.code.entity.MongCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MongCodeRepository extends JpaRepository<MongCode, String> {
    List<MongCode> findByBuildVersionIsLessThanEqual(String buildVersion);
    List<MongCode> findByLevel(Integer level);
    List<MongCode> findByLevelAndEvolutionPointIsLessThanEqualOrderByEvolutionPointDesc(Integer level, Integer evolutionPoint);
}
