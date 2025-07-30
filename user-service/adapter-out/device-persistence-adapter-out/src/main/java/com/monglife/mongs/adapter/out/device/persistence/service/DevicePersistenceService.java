package com.monglife.mongs.adapter.out.device.persistence.service;

import com.monglife.mongs.adapter.out.device.persistence.entity.DeviceEntity;
import com.monglife.mongs.adapter.out.device.persistence.repository.DeviceRepository;
import com.monglife.mongs.application.device.port.out.DevicePersistencePort;
import com.monglife.mongs.application.device.port.out.vo.CreateStepVo;
import com.monglife.mongs.domain.device.model.Step;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DevicePersistenceService implements DevicePersistencePort {

    private final DeviceRepository deviceRepository;

    /**
     * 걸음 수 등록
     * @param createStepVo 등록할 걸음 수 정보 Vo
     * @return 등록한 걸음 수 도메인 객체
     */
    @Override
    @Transactional
    public Step createStepPort(CreateStepVo createStepVo) {

        DeviceEntity deviceEntity = DeviceEntity.builder()
                .deviceId(createStepVo.getDeviceId())
                .walkingCount(createStepVo.getWalkingCount())
                .totalWalkingCount(createStepVo.getTotalWalkingCount())
                .consumeWalkingCount(createStepVo.getConsumeWalkingCount())
                .deviceBootedAt(createStepVo.getDeviceBootedAt())
                .build();

        return deviceRepository.save(deviceEntity).toDomain();
    }

    /**
     * 걸음 수 수정
     * @param step 수정할 걸음 수 도메인 객체
     */
    @Override
    @Transactional
    public Optional<Step> saveStepPort(Step step) {

        Optional<DeviceEntity> deviceEntityOptional = deviceRepository.findByDeviceIdWithLock(step.getDeviceId());

        if (deviceEntityOptional.isPresent()) {
            deviceEntityOptional.get().update(step);
            return Optional.of(deviceEntityOptional.get().toDomain());
        } else {
            return Optional.empty();
        }
    }

    /**
     * 걸음 수 조회
     * @param deviceId 기기 ID
     * @return 걸음 수 도메인 옵셔널 객체
     */
    @Override
    @Transactional
    public Optional<Step> getStepPort(String deviceId) {
        return deviceRepository.findByDeviceIdWithLock(deviceId).map(DeviceEntity::toDomain);
    }
}
