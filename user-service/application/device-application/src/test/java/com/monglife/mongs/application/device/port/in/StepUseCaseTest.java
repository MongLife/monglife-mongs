package com.monglife.mongs.application.device.port.in;

import com.monglife.mongs.application.device.port.in.command.ExchangeCurrentWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.command.RestoreExchangedWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.service.StepService;
import com.monglife.mongs.application.device.port.out.DeviceCachePort;
import com.monglife.mongs.application.device.port.out.DeviceEventPort;
import com.monglife.mongs.application.device.port.out.DevicePublishPort;
import com.monglife.mongs.application.device.port.out.dto.ExchangeCurrentWalkingCountDto;
import com.monglife.mongs.application.device.port.out.dto.RestoreWalkingCountDto;
import com.monglife.mongs.domain.device.exception.ExceedDailyExchangeWalkingCountException;
import com.monglife.mongs.domain.device.exception.InvalidExchangeWalkingCountException;
import com.monglife.mongs.domain.device.model.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StepUseCaseTest {

    private static final Long ACCOUNT_ID = 1L;
    private static final String DEVICE_ID = "TEST-DEVICE-ID";
    private static final Long MONG_ID = 1L;
    private static final int WALKING_COUNT = 1_000;

    private DeviceCachePort deviceCachePort;
    private DeviceEventPort deviceEventPort;
    private DevicePublishPort devicePublishPort;
    private StepUseCase stepUseCase;

    @BeforeEach
    void beforeEach() {
        deviceCachePort = Mockito.mock(DeviceCachePort.class);
        deviceEventPort = Mockito.mock(DeviceEventPort.class);
        devicePublishPort = Mockito.mock(DevicePublishPort.class);
        stepUseCase = new StepService(deviceCachePort, deviceEventPort, devicePublishPort);
    }

    private ExchangeCurrentWalkingCountCommand command() {
        return ExchangeCurrentWalkingCountCommand.builder()
                .accountId(ACCOUNT_ID)
                .deviceId(DEVICE_ID)
                .mongId(MONG_ID)
                .walkingCount(WALKING_COUNT)
                .build();
    }

    @Nested
    @DisplayName("보유 걸음 수 페이 포인트 환전 단위 테스트")
    class ExchangeCurrentWalkingCountUseCase {

        @Test
        @DisplayName("환전 이벤트를 발행하고 지급할 페이 포인트를 반환 한다.")
        void exchange() {
            // arrange
            Mockito.when(deviceCachePort.increaseTodayExchangedWalkingCountPort(ACCOUNT_ID, WALKING_COUNT))
                    .thenReturn(WALKING_COUNT);

            // act
            Step step = stepUseCase.exchangeCurrentWalkingCountUseCase(command());

            // assert
            assertEquals(100, step.getPayPoint());

            ArgumentCaptor<ExchangeCurrentWalkingCountDto> captor =
                    ArgumentCaptor.forClass(ExchangeCurrentWalkingCountDto.class);
            Mockito.verify(deviceEventPort).exchangeCurrentWalkingCountEventPort(captor.capture());
            assertEquals(MONG_ID, captor.getValue().getMongId());
            assertEquals(WALKING_COUNT, captor.getValue().getWalkingCount());
            assertEquals(100, captor.getValue().getPayPoint());

            // 정상 경로에서는 기기에 아무것도 발행하지 않는다. 지급은 character-service 가 맡는다.
            Mockito.verifyNoInteractions(devicePublishPort);
        }

        @Test
        @DisplayName("걸음 수가 올바르지 않으면 상한 카운터를 건드리기 전에 실패 한다.")
        void invalidWalkingCount() {
            // arrange
            ExchangeCurrentWalkingCountCommand invalid = ExchangeCurrentWalkingCountCommand.builder()
                    .accountId(ACCOUNT_ID).deviceId(DEVICE_ID).mongId(MONG_ID).walkingCount(0).build();

            // act & assert
            assertThrows(InvalidExchangeWalkingCountException.class,
                    () -> stepUseCase.exchangeCurrentWalkingCountUseCase(invalid));

            Mockito.verifyNoInteractions(deviceCachePort);
            Mockito.verifyNoInteractions(deviceEventPort);
        }

        @Test
        @DisplayName("일일 상한을 넘으면 카운터를 되돌리고 이벤트를 발행하지 않는다.")
        void exceedDailyLimit() {
            // arrange
            Mockito.when(deviceCachePort.increaseTodayExchangedWalkingCountPort(ACCOUNT_ID, WALKING_COUNT))
                    .thenReturn(Step.DAILY_EXCHANGE_LIMIT_WALKING_COUNT + 1);

            // act & assert
            assertThrows(ExceedDailyExchangeWalkingCountException.class,
                    () -> stepUseCase.exchangeCurrentWalkingCountUseCase(command()));

            Mockito.verify(deviceCachePort).decreaseTodayExchangedWalkingCountPort(ACCOUNT_ID, WALKING_COUNT);
            Mockito.verifyNoInteractions(deviceEventPort);
        }

        @Test
        @DisplayName("이벤트 발행이 실패하면 상한 카운터를 되돌린다.")
        void restoreCounterWhenEventFails() {
            // 되돌리지 않으면 지급도 못 받고 오늘 환전 한도만 깎인다.
            // arrange
            Mockito.when(deviceCachePort.increaseTodayExchangedWalkingCountPort(ACCOUNT_ID, WALKING_COUNT))
                    .thenReturn(WALKING_COUNT);
            Mockito.doThrow(new IllegalStateException("kafka down"))
                    .when(deviceEventPort).exchangeCurrentWalkingCountEventPort(Mockito.any());

            // act & assert
            assertThrows(IllegalStateException.class,
                    () -> stepUseCase.exchangeCurrentWalkingCountUseCase(command()));

            Mockito.verify(deviceCachePort).decreaseTodayExchangedWalkingCountPort(ACCOUNT_ID, WALKING_COUNT);
        }

        @Test
        @DisplayName("상한을 집계할 수 없으면 검사를 건너뛰고 환전을 진행 한다.")
        void failOpenWhenCacheUnavailable() {
            // arrange - Redis 장애 시 어댑터가 0 을 돌려준다
            Mockito.when(deviceCachePort.increaseTodayExchangedWalkingCountPort(ACCOUNT_ID, WALKING_COUNT))
                    .thenReturn(0);

            // act
            stepUseCase.exchangeCurrentWalkingCountUseCase(command());

            // assert
            Mockito.verify(deviceEventPort).exchangeCurrentWalkingCountEventPort(Mockito.any());
        }
    }

    @Nested
    @DisplayName("환전 실패분 걸음 수 복구 알림 단위 테스트")
    class RestoreExchangedWalkingCountUseCase {

        @Test
        @DisplayName("되돌릴 걸음 수와 중복 방지 키를 기기로 발행 한다.")
        void publishRestore() {
            // arrange
            final String eventId = "TEST-TRANSACTION-ID";

            // act
            stepUseCase.restoreExchangedWalkingCountUseCase(RestoreExchangedWalkingCountCommand.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(WALKING_COUNT)
                    .eventId(eventId)
                    .build());

            // assert
            ArgumentCaptor<RestoreWalkingCountDto> captor = ArgumentCaptor.forClass(RestoreWalkingCountDto.class);
            Mockito.verify(devicePublishPort).publishRestoreWalkingCountPort(captor.capture());
            assertEquals(DEVICE_ID, captor.getValue().getDeviceId());
            assertEquals(WALKING_COUNT, captor.getValue().getRestoreWalkingCount());
            assertEquals(eventId, captor.getValue().getEventId());
        }
    }
}
