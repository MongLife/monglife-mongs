package com.monglife.mongs.adapter.out.mong.persistence.repository;

import com.monglife.mongs.adapter.out.mong.persistence.entity.TrainingTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingTypeRepository extends JpaRepository<TrainingTypeEntity, Long> {

    Optional<TrainingTypeEntity> findByTrainingTypeCode(String trainingTypeCode);
}
