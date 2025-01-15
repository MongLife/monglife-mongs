package com.monglife.mongs.app.user.player.service;

import com.monglife.mongs.app.user.player.vo.PlayerStepVo;
import com.monglife.mongs.client.manager.service.ManagementService;
import com.monglife.mongs.domain.device.service.DeviceService;
import com.monglife.mongs.domain.device.vo.StepVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PlayerDeviceService {

    private final DeviceService deviceService;

    private final ManagementService managementService;

    /**
     * 기기 정보 등록
     * @param deviceId 기기 ID
     * @param totalWalkingCount 총 걸음 수
     * @param deviceBootedDt 기기 부팅 시간
     */
    @Transactional
    public void createDevice(String deviceId, Integer totalWalkingCount, LocalDateTime deviceBootedDt, String fcmToken) {
        deviceService.createDevice(deviceId, totalWalkingCount, deviceBootedDt, fcmToken);
    }

    /**
     * 총 걸음 수 갱신
     * @param deviceId 기기 ID
     * @param totalWalkingCount 총 걸음 수
     * @param deviceBootedDt 기기 부팅 시간
     * @return 플레이어 총 걸음 수 Vo
     */
    @Transactional
    public PlayerStepVo updateWalkingCount(String deviceId, Integer totalWalkingCount, LocalDateTime deviceBootedDt) {

        StepVo stepVo = deviceService.updateWalkingCount(deviceId, totalWalkingCount, deviceBootedDt);

        return PlayerStepVo.builder()
                .totalWalkingCount(stepVo.getTotalWalkingCount())
                .consumeWalkingCount(stepVo.getConsumeWalkingCount())
                .walkingCount(stepVo.getWalkingCount())
                .build();
    }

    /**
     * 걸음 수 환전
     * @param deviceId 기기 ID
     * @param mongId 몽 ID
     * @param totalWalkingCount 총 걸음 수 (걸음 수 갱신을 위함)
     * @param walkingCount 차감할 걸음 수
     * @param deviceBootedDt 기기 부팅 시간
     * @return 차감 후 잔여 걸음 수 Vo
     */
    @Transactional
    public PlayerStepVo exchangeWalkingCount(String deviceId, Long mongId, Integer totalWalkingCount, Integer walkingCount, LocalDateTime deviceBootedDt) {

        StepVo stepVo = deviceService.decreaseWalkingCount(deviceId, totalWalkingCount, walkingCount, deviceBootedDt);

        // 100 걸음 당 10 페이 포인트 적립
        Integer payPoint = walkingCount / 100 * 10;

        managementService.chargePayPoint(mongId, payPoint);

        return PlayerStepVo.builder()
                .totalWalkingCount(stepVo.getTotalWalkingCount())
                .consumeWalkingCount(stepVo.getConsumeWalkingCount())
                .walkingCount(stepVo.getWalkingCount())
                .build();
    }
}
