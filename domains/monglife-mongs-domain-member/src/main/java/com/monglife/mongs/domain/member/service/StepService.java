package com.monglife.mongs.domain.member.service;

import com.monglife.mongs.domain.member.entity.StepEntity;
import com.monglife.mongs.domain.member.exception.NotEnoughWalkingCountException;
import com.monglife.mongs.domain.member.exception.NotExistsMongStepException;
import com.monglife.mongs.domain.member.repository.StepRepository;
import com.monglife.mongs.domain.member.vo.StepVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StepService {

    private final StepRepository stepRepository;


    @Transactional
    public StepVo updateWalkingCount(String deviceId, Integer totalWalkingCount, LocalDateTime deviceBootedDt) {

        Optional<StepEntity> optionalStepEntity = stepRepository.findByDeviceId(deviceId);

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
            stepEntity = stepRepository.save(StepEntity.builder()
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

        StepEntity stepEntity = stepRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new NotExistsMongStepException(deviceId));

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
