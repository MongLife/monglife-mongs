package com.monglife.mongs.domain.device.service;

import com.monglife.mongs.domain.device.entity.DeviceEntity;
import com.monglife.mongs.domain.device.exception.NotEnoughWalkingCountException;
import com.monglife.mongs.domain.device.exception.NotExistsDeviceException;
import com.monglife.mongs.domain.device.repository.LockDeviceRepository;
import com.monglife.mongs.domain.device.vo.StepVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final LockDeviceRepository lockDeviceRepository;

    @Transactional
    public void createDevice(String deviceId, Integer totalWalkingCount, LocalDateTime deviceBootedDt, String fcmToken) {

        DeviceEntity deviceEntity = lockDeviceRepository.findByDeviceId(deviceId)
                .orElseGet(() -> lockDeviceRepository.save(DeviceEntity.builder()
                        .deviceId(deviceId)
                        .walkingCount(0)
                        .totalWalkingCount(totalWalkingCount)
                        .consumeWalkingCount(totalWalkingCount)
                        .deviceBootedDt(deviceBootedDt)
                        .fcmToken(fcmToken)
                        .build()));

        // FCM 토큰 갱신
        deviceEntity.setFcmToken(fcmToken);

        if (deviceBootedDt.equals(deviceEntity.getDeviceBootedDt())) {
            // 동기화
            if (totalWalkingCount > deviceEntity.getTotalWalkingCount()) {
                deviceEntity.updateTotalWalkingCount(totalWalkingCount);
            }
        } else {
            // 초기화
            deviceEntity.resetTotalWalkingCount(totalWalkingCount, deviceBootedDt);
        }
    }

    @Transactional
    public StepVo updateWalkingCount(String deviceId, Integer totalWalkingCount, LocalDateTime deviceBootedDt) {

        DeviceEntity deviceEntity = lockDeviceRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new NotExistsDeviceException(deviceId));

        if (deviceBootedDt.equals(deviceEntity.getDeviceBootedDt())) {
            // 동기화
            if (totalWalkingCount > deviceEntity.getTotalWalkingCount()) {
                deviceEntity.updateTotalWalkingCount(totalWalkingCount);
            }
        } else {
            // 초기화
            deviceEntity.resetTotalWalkingCount(totalWalkingCount, deviceBootedDt);
        }

        return StepVo.builder()
                .totalWalkingCount(deviceEntity.getTotalWalkingCount())
                .consumeWalkingCount(deviceEntity.getConsumeWalkingCount())
                .walkingCount(deviceEntity.getWalkingCount())
                .build();
    }

    @Transactional
    public StepVo decreaseWalkingCount(String deviceId, Integer totalWalkingCount, Integer walkingCount, LocalDateTime deviceBootedDt) {

        DeviceEntity deviceEntity = lockDeviceRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new NotExistsDeviceException(deviceId));

        if (deviceBootedDt.equals(deviceEntity.getDeviceBootedDt())) {
            // 동기화
            deviceEntity.updateTotalWalkingCount(totalWalkingCount);
        } else {
            // 초기화
            deviceEntity.resetTotalWalkingCount(totalWalkingCount, deviceBootedDt);
        }

        int nowWalkingCount = deviceEntity.getNowWalkingCount();

        if (nowWalkingCount < walkingCount) {
            throw new NotEnoughWalkingCountException(walkingCount);
        }

        deviceEntity.decreaseWalkingCount(walkingCount);

        return StepVo.builder()
                .totalWalkingCount(deviceEntity.getTotalWalkingCount())
                .consumeWalkingCount(deviceEntity.getConsumeWalkingCount())
                .walkingCount(deviceEntity.getWalkingCount())
                .build();
    }
}
