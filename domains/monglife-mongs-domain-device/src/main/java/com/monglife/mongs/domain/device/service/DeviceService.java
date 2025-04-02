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

    /**
     * 총 걸음 수 갱신
     * @param deviceId 기기 ID
     * @param totalWalkingCount 총 걸음 수
     * @param deviceBootedDt 기기 부팅 시간
     * @return 보유 걸음 수 Vo
     */
    @Transactional
    public StepVo updateWalkingCount(String deviceId, Integer totalWalkingCount, LocalDateTime deviceBootedDt) {

        DeviceEntity deviceEntity = lockDeviceRepository.findByDeviceId(deviceId)
                .orElseGet(() -> lockDeviceRepository.save(DeviceEntity.builder()
                        .deviceId(deviceId)
                        .walkingCount(0)
                        .totalWalkingCount(totalWalkingCount)
                        .consumeWalkingCount(totalWalkingCount)
                        .deviceBootedDt(deviceBootedDt)
                        .build()));

        if (deviceBootedDt.equals(deviceEntity.getDeviceBootedDt())) {
            // 총 걸음 수 갱신
            if (totalWalkingCount > deviceEntity.getTotalWalkingCount()) {
                deviceEntity.updateTotalWalkingCount(totalWalkingCount);
            }
        } else {
            // 총 걸음 수 초기화
            deviceEntity.resetTotalWalkingCount(totalWalkingCount, deviceBootedDt);
        }

        return StepVo.builder()
                .totalWalkingCount(deviceEntity.getTotalWalkingCount())
                .consumeWalkingCount(deviceEntity.getConsumeWalkingCount())
                .walkingCount(deviceEntity.getWalkingCount())
                .build();
    }

    /**
     * 보유 걸음 수 감소
     * @param deviceId 기기 ID
     * @param totalWalkingCount 총 걸음 수
     * @param walkingCount 감소할 보유 걸음 수
     * @param deviceBootedDt 기기 부팅 시간
     * @return 보유 걸음 수 Vo
     */
    @Transactional
    public StepVo decreaseWalkingCount(String deviceId, Integer totalWalkingCount, Integer walkingCount, LocalDateTime deviceBootedDt) {

        DeviceEntity deviceEntity = lockDeviceRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new NotExistsDeviceException(deviceId));

        if (deviceBootedDt.equals(deviceEntity.getDeviceBootedDt())) {
            // 총 걸음 수 갱신
            deviceEntity.updateTotalWalkingCount(totalWalkingCount);
        } else {
            // 총 걸음 수 초기화
            deviceEntity.resetTotalWalkingCount(totalWalkingCount, deviceBootedDt);
        }

        // 감소할 걸음 수 보유 여부 확인
        if (deviceEntity.getNowWalkingCount() < walkingCount) {
            throw new NotEnoughWalkingCountException(walkingCount);
        }

        // 보유 걸음 수 감소
        deviceEntity.decreaseWalkingCount(walkingCount);

        return StepVo.builder()
                .totalWalkingCount(deviceEntity.getTotalWalkingCount())
                .consumeWalkingCount(deviceEntity.getConsumeWalkingCount())
                .walkingCount(deviceEntity.getWalkingCount())
                .build();
    }
}
