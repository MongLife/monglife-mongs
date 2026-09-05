package com.monglife.mongs.application.device.port.in.service;

import com.monglife.mongs.application.device.port.in.StepUseCase;
import com.monglife.mongs.application.device.port.in.command.ExchangeCurrentWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.command.RestoreExchangedWalkingCountCommand;
import com.monglife.mongs.application.device.port.out.DeviceCachePort;
import com.monglife.mongs.application.device.port.out.DeviceEventPort;
import com.monglife.mongs.application.device.port.out.DevicePublishPort;
import com.monglife.mongs.application.device.port.out.dto.ExchangeCurrentWalkingCountDto;
import com.monglife.mongs.application.device.port.out.dto.RestoreWalkingCountDto;
import com.monglife.mongs.domain.device.model.Step;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StepService implements StepUseCase {

    private final DeviceCachePort deviceCachePort;

    private final DeviceEventPort deviceEventPort;

    private final DevicePublishPort devicePublishPort;

    /**
     * 보유 걸음 수 페이 포인트 환전
     *
     * 걸음 수 잔액은 기기가 들고 있으므로 서버는 잔액을 검증하지 않는다. 대신 계정별 일일
     * 환전 상한으로 무제한 발행만 막는다.
     *
     * DB 를 건드리지 않아 트랜잭션 경계가 없다. 대신 되돌려야 할 부수 효과가 상한 카운터
     * 하나뿐이라, 실패 지점마다 명시적으로 카운터를 복구한다.
     */
    @Override
    public Step exchangeCurrentWalkingCountUseCase(ExchangeCurrentWalkingCountCommand command) {

        // 도메인 규칙 검증이 먼저다. 카운터를 올린 뒤 검증하면 되돌릴 일만 늘어난다.
        Step step = Step.of(command.getWalkingCount());

        int todayExchangedWalkingCount = deviceCachePort.increaseTodayExchangedWalkingCountPort(
                command.getAccountId(), step.getWalkingCount());

        try {
            Step.validateDailyExchangeLimit(todayExchangedWalkingCount);

            // 페이 포인트 지급은 character-service 가 맡는다. 지급이 실패하면 롤백 이벤트가 돌아온다.
            deviceEventPort.exchangeCurrentWalkingCountEventPort(ExchangeCurrentWalkingCountDto.builder()
                    .accountId(command.getAccountId())
                    .deviceId(command.getDeviceId())
                    .mongId(command.getMongId())
                    .walkingCount(step.getWalkingCount())
                    .payPoint(step.getPayPoint())
                    .build());

        } catch (RuntimeException e) {
            // 상한을 넘겼거나 이벤트 발행이 실패했다. 카운터만 올라간 채로 두면
            // 지급도 못 받고 오늘 환전 한도만 깎인다.
            deviceCachePort.decreaseTodayExchangedWalkingCountPort(command.getAccountId(), step.getWalkingCount());
            throw e;
        }

        return step;
    }

    /**
     * 환전 실패분 걸음 수 복구 알림
     *
     * 서버에 잔액이 없으므로 되돌릴 상태가 없다. 잔액을 들고 있는 기기에게
     * "이만큼 되돌려라" 를 알리는 것이 서버가 할 수 있는 전부다.
     */
    @Override
    public void restoreExchangedWalkingCountUseCase(RestoreExchangedWalkingCountCommand command) {

        devicePublishPort.publishRestoreWalkingCountPort(RestoreWalkingCountDto.builder()
                .deviceId(command.getDeviceId())
                .restoreWalkingCount(command.getWalkingCount())
                .eventId(command.getEventId())
                .build());
    }
}
