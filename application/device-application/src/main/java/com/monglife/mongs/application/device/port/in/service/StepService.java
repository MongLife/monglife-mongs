package com.monglife.mongs.application.device.port.in.service;

import com.monglife.mongs.application.device.port.in.StepUseCase;
import com.monglife.mongs.application.device.port.in.command.ExchangeCurrentWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.command.IncreaseCurrentWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.command.UpdateTotalWalkingCountCommand;
import com.monglife.mongs.application.device.port.out.DeviceEventPort;
import com.monglife.mongs.application.device.port.out.DevicePersistencePort;
import com.monglife.mongs.application.device.port.out.DevicePublishPort;
import com.monglife.mongs.application.device.port.out.vo.CreateStepVo;
import com.monglife.mongs.domain.model.Step;
import com.monglife.mongs.application.device.port.exception.NotEnoughCurrentWalkingCountException;
import com.monglife.mongs.application.device.port.exception.NotExistStepException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StepService implements StepUseCase {

    private final DeviceEventPort deviceEventPort;

    private final DevicePersistencePort devicePersistencePort;

    private final DevicePublishPort devicePublishPort;

    /**
     * 보유 걸음 수 환전
     */
    @Override
    @Transactional
    public void exchangeCurrentWalkingCountUseCase(ExchangeCurrentWalkingCountCommand command) {

        Step step = devicePersistencePort.getStepPort(command.getDeviceId())
                .orElseThrow(NotExistStepException::new);

        if (step.getDeviceBootedDt().equals(command.getDeviceBootedDt())) {
            // 기기 부팅 시간이 변경 되지 않은 경우, 걸음 수 동기화
            step.updateTotalWalkingCount(command.getTotalWalkingCount());
        } else {
            // 기기 부팅 시간이 변경 된 경우, 걸음 수 초기화
            step.reset(command.getTotalWalkingCount(), command.getDeviceBootedDt());
        }

        // 보유 걸음 수가 부족한 경우 예외 발생
        if (step.getCurrentWalkingCount() < command.getWalkingCount()) {
            throw new NotEnoughCurrentWalkingCountException();
        }

        // 보유 걸음 수 감소
        step.decreaseCurrentWalkingCount(command.getWalkingCount());

        // 보유 걸음 수 환전 이벤트 발생
        deviceEventPort.exchangeCurrentWalkingCountEventPort(step);

        // 걸음 수 수정
        devicePersistencePort.saveStepPort(step)
                .orElseThrow(NotExistStepException::new);

        // 보유 걸음 수 비동기 응답
        devicePublishPort.publishCurrentWalkingCountPort(step);
    }

    /**
     * 총 걸음 수 동기화
     */
    @Override
    @Transactional
    public void updateTotalWalkingCountUseCase(UpdateTotalWalkingCountCommand command) {

        // 걸음 수가 없는 경우 등록
        Step step = devicePersistencePort.getStepPort(command.getDeviceId())
                .orElseGet(() -> devicePersistencePort.createStepPort(CreateStepVo.builder()
                            .deviceId(command.getDeviceId())
                            .walkingCount(0)
                            .totalWalkingCount(command.getTotalWalkingCount())
                            .consumeWalkingCount(0)
                            .deviceBootedDt(command.getDeviceBootedDt())
                            .build()));

        if (step.getDeviceBootedDt().equals(command.getDeviceBootedDt())) {
            // 기기 부팅 시간이 변경 되지 않은 경우, 걸음 수 동기화
            step.updateTotalWalkingCount(command.getTotalWalkingCount());
        } else {
            // 기기 부팅 시간이 변경 된 경우, 걸음 수 초기화
            step.reset(command.getTotalWalkingCount(), command.getDeviceBootedDt());
        }

        // 걸음 수 수정
        devicePersistencePort.saveStepPort(step)
                .orElseThrow(NotExistStepException::new);;
    }

    /**
     * 총 걸음 수 증가
     */
    @Override
    @Transactional
    public void increaseCurrentWalkingCountUseCase(IncreaseCurrentWalkingCountCommand command) {

        Step step = devicePersistencePort.getStepPort(command.getDeviceId())
                .orElseThrow(NotExistStepException::new);

        // 보유 걸음 수 증가
        step.increaseCurrentWalkingCount(command.getWalkingCount());

        // 걸음 수 수정
        devicePersistencePort.saveStepPort(step)
                .orElseThrow(NotExistStepException::new);;

        // 보유 걸음 수 비동기 응답
        devicePublishPort.publishCurrentWalkingCountPort(step);
    }
}
