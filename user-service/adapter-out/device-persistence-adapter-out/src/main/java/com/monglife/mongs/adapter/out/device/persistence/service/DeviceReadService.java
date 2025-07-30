package com.monglife.mongs.adapter.out.device.persistence.service;

import com.monglife.mongs.adapter.out.device.persistence.entity.DeviceEntity;
import com.monglife.mongs.adapter.out.device.persistence.repository.DeviceRepository;
import com.monglife.mongs.application.device.port.out.DeviceReadPort;
import com.monglife.mongs.domain.device.model.Step;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceReadService implements DeviceReadPort {

    private final DeviceRepository deviceRepository;

    /**
     * 걸음 수 조회
     * @param deviceId 기기 ID
     * @return 걸음 수 도메인 옵셔널 객체
     */
    @Override
    public Optional<Step> getStepPort(String deviceId) {
        return deviceRepository.findByDeviceId(deviceId).map(DeviceEntity::toDomain);
    }
}
