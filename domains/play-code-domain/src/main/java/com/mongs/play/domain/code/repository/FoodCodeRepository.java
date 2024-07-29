package com.mongs.play.domain.code.repository;

import com.mongs.play.domain.code.entity.FoodCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodCodeRepository extends JpaRepository<FoodCode, String> {
    @Query("SELECT fc FROM FoodCode fc WHERE fc.buildVersion <= :buildVersion")
    List<FoodCode> findByBuildVersionIsLessThanEqual(@Param("buildVersion") String buildVersion);
}
