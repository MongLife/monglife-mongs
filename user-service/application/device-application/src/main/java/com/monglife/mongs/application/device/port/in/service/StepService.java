package com.monglife.mongs.application.device.port.in.service;

import com.monglife.mongs.application.device.port.exception.NotExistStepException;
import com.monglife.mongs.application.device.port.in.StepUseCase;
import com.monglife.mongs.application.device.port.in.command.ExchangeCurrentWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.command.GetStepCommand;
import com.monglife.mongs.application.device.port.in.command.IncreaseCurrentWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.command.UpdateTotalWalkingCountCommand;
import com.monglife.mongs.application.device.port.out.DeviceEventPort;
import com.monglife.mongs.application.device.port.out.DevicePersistencePort;
import com.monglife.mongs.application.device.port.out.DevicePublishPort;
import com.monglife.mongs.application.device.port.out.DeviceReadPort;
import com.monglife.mongs.application.device.port.out.dto.ExchangeCurrentWalkingCountDto;
import com.monglife.mongs.application.device.port.out.vo.CreateStepVo;
import com.monglife.mongs.domain.device.model.Step;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StepService implements StepUseCase {

    private final DeviceEventPort deviceEventPort;

    private final DeviceReadPort deviceReadPort;

    private final DevicePersistencePort devicePersistencePort;

    private final DevicePublishPort devicePublishPort;

    /**
     * 보유 걸음 수 조회
     */
    @Override
    @Transactional
    public Step getStepUseCase(GetStepCommand command) {
        return deviceReadPort.getStepPort(command.getDeviceId())
                .orElseThrow(NotExistStepException::new);
    }

    /**
     * 보유 걸음 수 환전
     */
    @Override
    @Transactional
    public Step exchangeCurrentWalkingCountUseCase(ExchangeCurrentWalkingCountCommand command) {

        Step step = devicePersistencePort.getStepPort(command.getDeviceId())
                .orElseThrow(NotExistStepException::new);

        // 보유 걸음 수 환전
        int payPoint = step.exchangeWalkingCountToPayPoint(command.getWalkingCount());

        // 걸음 수 수정
        devicePersistencePort.saveStepPort(step)
                .orElseThrow(NotExistStepException::new);

        // 보유 걸음 수 환전 이벤트 발생
        deviceEventPort.exchangeCurrentWalkingCountEventPort(ExchangeCurrentWalkingCountDto.builder()
                .accountId(command.getAccountId())
                .deviceId(command.getDeviceId())
                .mongId(command.getMongId())
                .walkingCount(command.getWalkingCount())
                .payPoint(payPoint)
                .build());

        // 보유 걸음 수 비동기 응답
        devicePublishPort.publishCurrentWalkingCountPort(step);

        return step;
    }

    /**
     * 총 걸음 수 동기화
     */
    @Override
    @Transactional
    public Step updateTotalWalkingCountUseCase(UpdateTotalWalkingCountCommand command) {

        // 걸음 수가 없는 경우 등록
        Step step = devicePersistencePort.getStepPort(command.getDeviceId())
                .orElseGet(() -> devicePersistencePort.createStepPort(CreateStepVo.builder()
                            .deviceId(command.getDeviceId())
                            .walkingCount(0)
                            .totalWalkingCount(command.getTotalWalkingCount())
                            .consumeWalkingCount(0)
                            .deviceBootedAt(command.getDeviceBootedAt())
                            .build()));

        // 걸음 수 동기화
        step.syncTotalWalkingCount(command.getTotalWalkingCount(), command.getDeviceBootedAt());

        // 걸음 수 수정
        devicePersistencePort.saveStepPort(step)
                .orElseThrow(NotExistStepException::new);

        return step;
    }

    /**
     * 총 걸음 수 증가
     */
    @Override
    @Transactional
    public Step increaseCurrentWalkingCountUseCase(IncreaseCurrentWalkingCountCommand command) {

        Step step = devicePersistencePort.getStepPort(command.getDeviceId())
                .orElseThrow(NotExistStepException::new);

        // 보유 걸음 수 증가
        step.increaseCurrentWalkingCount(command.getWalkingCount());

        // 걸음 수 수정
        devicePersistencePort.saveStepPort(step)
                .orElseThrow(NotExistStepException::new);

        // 보유 걸음 수 비동기 응답
        devicePublishPort.publishCurrentWalkingCountPort(step);

        return step;
    }
}
