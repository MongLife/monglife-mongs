package com.monglife.mongs.adapter.out.device.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.device.persistence.entity.DeviceEntity;

import java.util.Optional;

public interface DeviceDslRepository {

    Optional<DeviceEntity> findByDeviceIdWithLock(String deviceId);
}
