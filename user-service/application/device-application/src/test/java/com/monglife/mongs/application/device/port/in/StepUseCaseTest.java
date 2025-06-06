package com.monglife.mongs.application.device.port.in;

import com.monglife.mongs.application.device.port.exception.NotExistStepException;
import com.monglife.mongs.application.device.port.in.command.ExchangeCurrentWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.command.IncreaseCurrentWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.command.UpdateTotalWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.service.StepService;
import com.monglife.mongs.application.device.port.out.DeviceEventPort;
import com.monglife.mongs.application.device.port.out.DevicePersistencePort;
import com.monglife.mongs.application.device.port.out.DevicePublishPort;
import com.monglife.mongs.domain.device.model.Step;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StepUseCaseTest {

    private final DeviceEventPort deviceEventPort = Mockito.mock(DeviceEventPort.class);
    private final DevicePublishPort devicePublishPort = Mockito.mock(DevicePublishPort.class);
    private final DevicePersistencePort devicePersistencePort = Mockito.mock(DevicePersistencePort.class);
    private final StepUseCase stepUseCase = new StepService(deviceEventPort, devicePersistencePort, devicePublishPort);

    @Nested
    @DisplayName("보유 걸음 수 페이 포인트 환전 단위 테스트")
    class ExchangeCurrentWalkingCountUseCase {

        private static final String DEVICE_ID = "TEST-DEVICE-ID";
        private static final int TOTAL_WALKING_COUNT = 100;
        private static final LocalDateTime DEVICE_BOOTED_AT = LocalDateTime.of(2025, 1, 1, 0, 0);

        @Test
        @DisplayName("총 걸음 수를 동기화하고 보유 걸음 수를 보유한 경우, 페이 포인트로 환전 후 보유 걸음 수 비동기 응답을 전송 한다.")
        void enoughCurrentWalkingCount() {
            // arrange
            final Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedAt(DEVICE_BOOTED_AT)
                    .build();

            Mockito.when(devicePersistencePort.getStepPort(DEVICE_ID)).thenReturn(Optional.of(step));
            Mockito.when(devicePersistencePort.saveStepPort(Mockito.any())).thenReturn(Optional.of(step));

            // act
            ExchangeCurrentWalkingCountCommand command = ExchangeCurrentWalkingCountCommand.builder()
                    .deviceId(DEVICE_ID)
                    .mongId(1L)
                    .walkingCount(TOTAL_WALKING_COUNT)
                    .build();

            stepUseCase.exchangeCurrentWalkingCountUseCase(command);

            // assert
            Mockito.verify(devicePublishPort).publishCurrentWalkingCountPort(Mockito.any());
            Mockito.verify(deviceEventPort).exchangeCurrentWalkingCountEventPort(Mockito.any());
        }

        @Test
        @DisplayName("걸음 수가 없는 경우 예외가 발생 한다.")
        void notExistsStep() {
            // arrange
            Mockito.when(devicePersistencePort.getStepPort(DEVICE_ID)).thenReturn(Optional.empty());

            // act & assert
            ExchangeCurrentWalkingCountCommand command = ExchangeCurrentWalkingCountCommand.builder()
                    .deviceId(DEVICE_ID)
                    .mongId(1L)
                    .walkingCount(0)
                    .build();

            assertThrows(NotExistStepException.class, () -> stepUseCase.exchangeCurrentWalkingCountUseCase(command));
        }

        @Test
        @DisplayName("걸음 수를 수정할 때 걸음 수가 없는 경우 예외가 발생 한다.")
        void notExistsStepWhenSaveStep() {
            // arrange
            final Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedAt(DEVICE_BOOTED_AT)
                    .build();

            Mockito.when(devicePersistencePort.getStepPort(DEVICE_ID)).thenReturn(Optional.of(step));
            Mockito.when(devicePersistencePort.saveStepPort(Mockito.any())).thenReturn(Optional.empty());

            // act & assert
            ExchangeCurrentWalkingCountCommand command = ExchangeCurrentWalkingCountCommand.builder()
                    .deviceId(DEVICE_ID)
                    .mongId(1L)
                    .walkingCount(TOTAL_WALKING_COUNT)
                    .build();

            assertThrows(NotExistStepException.class, () -> stepUseCase.exchangeCurrentWalkingCountUseCase(command));
        }
    }

    @Nested
    @DisplayName("총 걸음 수 동기화 단위 테스트 ")
    class UpdateTotalWalkingCountUseCase {

        private static final String DEVICE_ID = "TEST-DEVICE-ID";
        private static final int TOTAL_WALKING_COUNT = 100;
        private static final LocalDateTime DEVICE_BOOTED_AT = LocalDateTime.of(2025, 1, 1, 0, 0);

        @Test
        @DisplayName("걸음 수가 없는 경우 새로운 걸음 수를 등록한다.")
        void createStepWhenNotExistsStep() {
            // arrange
            final Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedAt(DEVICE_BOOTED_AT)
                    .build();

            Mockito.when(devicePersistencePort.getStepPort(DEVICE_ID)).thenReturn(Optional.empty());
            Mockito.when(devicePersistencePort.createStepPort(Mockito.any())).thenReturn(step);
            Mockito.when(devicePersistencePort.saveStepPort(Mockito.any())).thenReturn(Optional.of(step));

            // act & assert
            UpdateTotalWalkingCountCommand command = UpdateTotalWalkingCountCommand.builder()
                    .deviceId(DEVICE_ID)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedAt(DEVICE_BOOTED_AT)
                    .build();

            assertDoesNotThrow(() -> stepUseCase.updateTotalWalkingCountUseCase(command));
        }

        @Test
        @DisplayName("걸음 수를 수정할 때 걸음 수가 없는 경우 예외가 발생 한다.")
        void notExistsStepWhenSaveStep() {
            // arrange
            final Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedAt(DEVICE_BOOTED_AT)
                    .build();

            Mockito.when(devicePersistencePort.getStepPort(DEVICE_ID)).thenReturn(Optional.of(step));
            Mockito.when(devicePersistencePort.createStepPort(Mockito.any())).thenReturn(step);
            Mockito.when(devicePersistencePort.saveStepPort(Mockito.any())).thenReturn(Optional.empty());

            // act & assert
            UpdateTotalWalkingCountCommand command = UpdateTotalWalkingCountCommand.builder()
                    .deviceId(DEVICE_ID)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedAt(DEVICE_BOOTED_AT)
                    .build();

            assertThrows(NotExistStepException.class, () -> stepUseCase.updateTotalWalkingCountUseCase(command));
        }
    }

    @Nested
    @DisplayName("총 걸음 수 증가 단위 테스트")
    class IncreaseCurrentWalkingCountUseCase {

        private static final String DEVICE_ID = "TEST-DEVICE-ID";
        private static final int TOTAL_WALKING_COUNT = 100;
        private static final LocalDateTime DEVICE_BOOTED_AT = LocalDateTime.of(2025, 1, 1, 0, 0);

        @Test
        @DisplayName("걸음 수가 없는 경우 예외가 발생한다.")
        void notExistsStep() {
            // arrange
            final Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedAt(DEVICE_BOOTED_AT)
                    .build();

            Mockito.when(devicePersistencePort.getStepPort(DEVICE_ID)).thenReturn(Optional.empty());
            Mockito.when(devicePersistencePort.saveStepPort(Mockito.any())).thenReturn(Optional.of(step));

            // act & assert
            IncreaseCurrentWalkingCountCommand command = IncreaseCurrentWalkingCountCommand.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(100)
                    .build();

            assertThrows(NotExistStepException.class, () -> stepUseCase.increaseCurrentWalkingCountUseCase(command));
        }

        @Test
        @DisplayName("걸음 수를 수정할 때 걸음 수가 없는 경우 예외가 발생한다.")
        void notExistsStepWhenSaveStep() {
            // arrange
            final Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedAt(DEVICE_BOOTED_AT)
                    .build();

            Mockito.when(devicePersistencePort.getStepPort(DEVICE_ID)).thenReturn(Optional.of(step));
            Mockito.when(devicePersistencePort.saveStepPort(Mockito.any())).thenReturn(Optional.empty());

            // act & assert
            IncreaseCurrentWalkingCountCommand command = IncreaseCurrentWalkingCountCommand.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(100)
                    .build();

            assertThrows(NotExistStepException.class, () -> stepUseCase.increaseCurrentWalkingCountUseCase(command));
        }
    }
}