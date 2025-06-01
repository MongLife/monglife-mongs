package com.monglife.mongs.adapter.out.device.persistence.repository;

import com.monglife.mongs.adapter.out.device.persistence.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<DeviceEntity, String> {

    Optional<DeviceEntity> findByDeviceId(String deviceId);
}
