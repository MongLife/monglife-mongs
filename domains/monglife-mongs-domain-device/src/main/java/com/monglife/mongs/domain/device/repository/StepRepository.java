package com.monglife.mongs.domain.device.repository;

import com.monglife.mongs.domain.device.entity.StepEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StepRepository extends JpaRepository<StepEntity, Long> {

    Optional<StepEntity> findByDeviceId(String deviceId);
}
