package com.monglife.mongs.domain.device.repository;

import com.monglife.mongs.domain.device.entity.StepEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface LockStepRepository extends JpaRepository<StepEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<StepEntity> findByDeviceId(String deviceId);
}
