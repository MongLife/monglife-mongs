package com.mongs.play.module.code.repository;

import com.mongs.play.module.code.entity.MongCode;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongCodeRepository extends MongoRepository<MongCode, String> {
    List<MongCode> findByBuildVersionIsLessThanEqual(String buildVersion);
    List<MongCode> findByLevel(Integer level);
    List<MongCode> findByLevelAndEvolutionPointIsLessThanEqualOrderByEvolutionPointDesc(Integer level, Integer evolutionPoint);
}
