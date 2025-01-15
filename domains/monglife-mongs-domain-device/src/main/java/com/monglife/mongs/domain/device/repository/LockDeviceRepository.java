package com.monglife.mongs.domain.device.repository;

import com.monglife.mongs.domain.device.entity.DeviceEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface LockDeviceRepository extends JpaRepository<DeviceEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<DeviceEntity> findByDeviceId(String deviceId);
}
