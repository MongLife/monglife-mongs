package com.mongs.play.domain.code.repository;

import com.mongs.play.domain.code.entity.MongCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongCodeRepository extends JpaRepository<MongCode, String> {
    @Query("SELECT mgc FROM MongCode mgc WHERE mgc.buildVersion <= :buildVersion")
    List<MongCode> findByBuildVersionIsLessThanEqual(@Param("buildVersion") String buildVersion);

    @Query("SELECT mgc FROM MongCode mgc WHERE mgc.level = :level")
    List<MongCode> findByLevel(@Param("level") Integer level);

    @Query("SELECT mgc FROM MongCode mgc WHERE mgc.level = :level AND mgc.evolutionPoint <= :evolutionPoint ORDER BY mgc.evolutionPoint DESC")
    List<MongCode> findByLevelAndEvolutionPointIsLessThanEqualOrderByEvolutionPointDesc(@Param("level") Integer level, @Param("evolutionPoint") Integer evolutionPoint);
}
