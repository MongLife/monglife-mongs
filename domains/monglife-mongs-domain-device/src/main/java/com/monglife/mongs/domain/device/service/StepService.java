package com.monglife.mongs.domain.device.service;

import com.monglife.mongs.domain.device.entity.StepEntity;
import com.monglife.mongs.domain.device.exception.NotEnoughWalkingCountException;
import com.monglife.mongs.domain.device.exception.NotExistsStepException;
import com.monglife.mongs.domain.device.repository.LockStepRepository;
import com.monglife.mongs.domain.device.vo.StepVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StepService {

    private final LockStepRepository lockStepRepository;


    @Transactional
    public StepVo updateWalkingCount(String deviceId, Integer totalWalkingCount, LocalDateTime deviceBootedDt) {

        Optional<StepEntity> optionalStepEntity = lockStepRepository.findByDeviceId(deviceId);

        StepEntity stepEntity;
        if (optionalStepEntity.isPresent()) {

            stepEntity = optionalStepEntity.get();

            if (deviceBootedDt.equals(stepEntity.getDeviceBootedDt())) {
                // 동기화
                if (totalWalkingCount > stepEntity.getTotalWalkingCount()) {
                    stepEntity.updateTotalWalkingCount(totalWalkingCount);
                }
            } else {
                // 초기화
                stepEntity.resetTotalWalkingCount(totalWalkingCount, deviceBootedDt);
            }

        } else {
            stepEntity = lockStepRepository.save(StepEntity.builder()
                    .deviceId(deviceId)
                    .walkingCount(0)
                    .totalWalkingCount(totalWalkingCount)
                    .consumeWalkingCount(totalWalkingCount)
                    .deviceBootedDt(deviceBootedDt)
                    .build());
        }

        return StepVo.builder()
                .totalWalkingCount(stepEntity.getTotalWalkingCount())
                .consumeWalkingCount(stepEntity.getConsumeWalkingCount())
                .walkingCount(stepEntity.getWalkingCount())
                .build();
    }

    @Transactional
    public StepVo decreaseWalkingCount(String deviceId, Integer totalWalkingCount, Integer walkingCount, LocalDateTime deviceBootedDt) {

        StepEntity stepEntity = lockStepRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new NotExistsStepException(deviceId));

        if (deviceBootedDt.equals(stepEntity.getDeviceBootedDt())) {
            // 동기화
            stepEntity.updateTotalWalkingCount(totalWalkingCount);
        } else {
            // 초기화
            stepEntity.resetTotalWalkingCount(totalWalkingCount, deviceBootedDt);
        }

        int nowWalkingCount = stepEntity.getNowWalkingCount();

        if (nowWalkingCount < walkingCount) {
            throw new NotEnoughWalkingCountException(walkingCount);
        }

        stepEntity.decreaseWalkingCount(walkingCount);

        return StepVo.builder()
                .totalWalkingCount(stepEntity.getTotalWalkingCount())
                .consumeWalkingCount(stepEntity.getConsumeWalkingCount())
                .walkingCount(stepEntity.getWalkingCount())
                .build();
    }
}
