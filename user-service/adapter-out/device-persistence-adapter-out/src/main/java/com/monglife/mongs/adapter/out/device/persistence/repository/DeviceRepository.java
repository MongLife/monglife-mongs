package com.monglife.mongs.adapter.out.device.persistence.repository;

import com.monglife.mongs.adapter.out.device.persistence.entity.DeviceEntity;
import com.monglife.mongs.adapter.out.device.persistence.repository.dsl.DeviceDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<DeviceEntity, String>, DeviceDslRepository {

    Optional<DeviceEntity> findByDeviceId(String deviceId);
}
