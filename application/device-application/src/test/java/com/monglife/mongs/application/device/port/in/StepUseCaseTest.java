package com.monglife.mongs.application.device.port.in;

import com.monglife.mongs.application.device.port.exception.NotExistStepException;
import com.monglife.mongs.application.device.port.in.command.ExchangeCurrentWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.command.IncreaseCurrentWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.command.UpdateTotalWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.service.StepService;
import com.monglife.mongs.application.device.port.out.DeviceEventPort;
import com.monglife.mongs.application.device.port.out.DevicePersistencePort;
import com.monglife.mongs.application.device.port.out.DevicePublishPort;
import com.monglife.mongs.domain.exception.InvalidTotalWalkingCountException;
import com.monglife.mongs.domain.exception.NotEnoughCurrentWalkingCountException;
import com.monglife.mongs.domain.model.Step;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StepUseCaseTest {

    private final DeviceEventPort deviceEventPort;

    private final DevicePersistencePort devicePersistencePort;

    private final DevicePublishPort devicePublishPort;

    private final StepUseCase stepUseCase;

    public StepUseCaseTest() {
        this.deviceEventPort = Mockito.mock(DeviceEventPort.class);
        this.devicePublishPort = Mockito.mock(DevicePublishPort.class);
        this.devicePersistencePort = Mockito.mock(DevicePersistencePort.class);
        this.stepUseCase = new StepService(deviceEventPort, devicePersistencePort, devicePublishPort);
    }

    private static final String DEVICE_ID = "TEST-DEVICE-ID";
    private static final int TOTAL_WALKING_COUNT = 100;
    private static final LocalDateTime DEVICE_BOOTED_DT = LocalDateTime.of(2025, 1, 1, 0, 0);

    @Nested
    @DisplayName("보유 걸음 수 페이 포인트 환전 단위 테스트")
    class ExchangeCurrentWalkingCountUseCase {

        @Test
        @DisplayName("보유 걸음 수를 보유한 경우 페이 포인트로 환전 한다.")
        void enoughCurrentWalkingCount() {
            // arrange
            Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedDt(DEVICE_BOOTED_DT)
                    .build();

            Mockito.when(devicePersistencePort.getStepPort(DEVICE_ID)).thenReturn(Optional.of(step));
            Mockito.when(devicePersistencePort.saveStepPort(Mockito.any())).thenReturn(Optional.of(step));

            // act
            ExchangeCurrentWalkingCountCommand command = ExchangeCurrentWalkingCountCommand.builder()
                    .deviceId(DEVICE_ID)
                    .mongId(1L)
                    .walkingCount(TOTAL_WALKING_COUNT)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedDt(LocalDateTime.of(2025, 1, 1, 0, 0))
                    .build();

            step = stepUseCase.exchangeCurrentWalkingCountUseCase(command);

            // assert
            assertEquals(0, step.getCurrentWalkingCount());
            Mockito.verify(devicePersistencePort).getStepPort(DEVICE_ID);
            Mockito.verify(deviceEventPort).exchangeCurrentWalkingCountEventPort(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
            Mockito.verify(devicePersistencePort).saveStepPort(Mockito.any());
            Mockito.verify(devicePublishPort).publishCurrentWalkingCountPort(Mockito.any());
        }

        @Test
        @DisplayName("총 걸음 수를 동기화하고 페이 포인트로 환전 한다.")
        void updateTotalWalkingCountAndExchangeCurrentWalkingCount() {
            // arrange
            int walkingCount = 50;
            int newTotalWalkingCount = TOTAL_WALKING_COUNT + walkingCount;
            Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedDt(DEVICE_BOOTED_DT)
                    .build();

            Mockito.when(devicePersistencePort.getStepPort(DEVICE_ID)).thenReturn(Optional.of(step));
            Mockito.when(devicePersistencePort.saveStepPort(Mockito.any())).thenReturn(Optional.of(step));

            // act
            ExchangeCurrentWalkingCountCommand command = ExchangeCurrentWalkingCountCommand.builder()
                    .deviceId(DEVICE_ID)
                    .mongId(1L)
                    .walkingCount(TOTAL_WALKING_COUNT)
                    .totalWalkingCount(newTotalWalkingCount)
                    .deviceBootedDt(DEVICE_BOOTED_DT)
                    .build();

            step = stepUseCase.exchangeCurrentWalkingCountUseCase(command);

            // assert
            assertEquals(walkingCount, step.getCurrentWalkingCount());
            Mockito.verify(devicePersistencePort).getStepPort(DEVICE_ID);
            Mockito.verify(deviceEventPort).exchangeCurrentWalkingCountEventPort(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
            Mockito.verify(devicePersistencePort).saveStepPort(Mockito.any());
            Mockito.verify(devicePublishPort).publishCurrentWalkingCountPort(Mockito.any());
        }

        @Test
        @DisplayName("총 걸음 수를 초기화하고 페이 포인트로 환전 한다.")
        void resetStepAndExchangeCurrentWalkingCount() {
            // arrange
            int newTotalWalkingCount = 50;
            LocalDateTime newDeviceBootedDt = LocalDateTime.of(2025, 1, 2, 0, 0);
            Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedDt(DEVICE_BOOTED_DT)
                    .build();

            Mockito.when(devicePersistencePort.getStepPort(DEVICE_ID)).thenReturn(Optional.of(step));
            Mockito.when(devicePersistencePort.saveStepPort(Mockito.any())).thenReturn(Optional.of(step));

            // act
            ExchangeCurrentWalkingCountCommand command = ExchangeCurrentWalkingCountCommand.builder()
                    .deviceId(DEVICE_ID)
                    .mongId(1L)
                    .walkingCount(TOTAL_WALKING_COUNT)
                    .totalWalkingCount(newTotalWalkingCount)
                    .deviceBootedDt(newDeviceBootedDt)
                    .build();

            step = stepUseCase.exchangeCurrentWalkingCountUseCase(command);

            // assert
            assertEquals(newDeviceBootedDt, step.getDeviceBootedDt());
            assertEquals(newTotalWalkingCount, step.getCurrentWalkingCount());
            Mockito.verify(devicePersistencePort).getStepPort(DEVICE_ID);
            Mockito.verify(deviceEventPort).exchangeCurrentWalkingCountEventPort(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
            Mockito.verify(devicePersistencePort).saveStepPort(Mockito.any());
            Mockito.verify(devicePublishPort).publishCurrentWalkingCountPort(Mockito.any());
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
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedDt(DEVICE_BOOTED_DT)
                    .build();

            assertThrows(NotExistStepException.class, () -> stepUseCase.exchangeCurrentWalkingCountUseCase(command));
            Mockito.verify(devicePersistencePort).getStepPort(DEVICE_ID);
            Mockito.verify(deviceEventPort, Mockito.never()).exchangeCurrentWalkingCountEventPort(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
            Mockito.verify(devicePersistencePort, Mockito.never()).saveStepPort(Mockito.any());
            Mockito.verify(devicePublishPort, Mockito.never()).publishCurrentWalkingCountPort(Mockito.any());
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
                    .deviceBootedDt(DEVICE_BOOTED_DT)
                    .build();

            Mockito.when(devicePersistencePort.getStepPort(DEVICE_ID)).thenReturn(Optional.of(step));
            Mockito.when(devicePersistencePort.saveStepPort(Mockito.any())).thenReturn(Optional.empty());

            // act & assert
            ExchangeCurrentWalkingCountCommand command = ExchangeCurrentWalkingCountCommand.builder()
                    .deviceId(DEVICE_ID)
                    .mongId(1L)
                    .walkingCount(TOTAL_WALKING_COUNT)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedDt(LocalDateTime.of(2025, 1, 1, 0, 0))
                    .build();

            assertThrows(NotExistStepException.class, () -> stepUseCase.exchangeCurrentWalkingCountUseCase(command));
            Mockito.verify(deviceEventPort, Mockito.never()).exchangeCurrentWalkingCountEventPort(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        }

        @Test
        @DisplayName("보유 걸음 수가 부족한 경우 예외가 발생 한다.")
        void notEnoughCurrentWalkingCount() {
            // arrange
            final Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedDt(DEVICE_BOOTED_DT)
                    .build();

            Mockito.when(devicePersistencePort.getStepPort(DEVICE_ID)).thenReturn(Optional.of(step));

            // act & assert
            ExchangeCurrentWalkingCountCommand command = ExchangeCurrentWalkingCountCommand.builder()
                    .deviceId(DEVICE_ID)
                    .mongId(1L)
                    .walkingCount(Integer.MAX_VALUE)
                    .totalWalkingCount(200)
                    .deviceBootedDt(LocalDateTime.of(2025, 1, 1, 0, 0))
                    .build();

            assertThrows(NotEnoughCurrentWalkingCountException.class, () -> stepUseCase.exchangeCurrentWalkingCountUseCase(command));
            Mockito.verify(devicePersistencePort).getStepPort(DEVICE_ID);
            Mockito.verify(deviceEventPort, Mockito.never()).exchangeCurrentWalkingCountEventPort(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
            Mockito.verify(devicePersistencePort, Mockito.never()).saveStepPort(Mockito.any());
            Mockito.verify(devicePublishPort, Mockito.never()).publishCurrentWalkingCountPort(Mockito.any());
        }
    }

    @Nested
    @DisplayName("총 걸음 수 동기화 단위 테스트 ")
    class UpdateTotalWalkingCountUseCase {

        @Test
        @DisplayName("걸음 수가 없는 경우 새로운 걸음 수를 등록한다.")
        void createStepWhenNotExistsStep() {
            // arrange
            Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedDt(DEVICE_BOOTED_DT)
                    .build();

            Mockito.when(devicePersistencePort.getStepPort(DEVICE_ID)).thenReturn(Optional.empty());
            Mockito.when(devicePersistencePort.createStepPort(Mockito.any())).thenReturn(step);
            Mockito.when(devicePersistencePort.saveStepPort(Mockito.any())).thenReturn(Optional.of(step));

            // act & assert
            UpdateTotalWalkingCountCommand command = UpdateTotalWalkingCountCommand.builder()
                    .deviceId(DEVICE_ID)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedDt(DEVICE_BOOTED_DT)
                    .build();

            step = stepUseCase.updateTotalWalkingCountUseCase(command);

            assertEquals(TOTAL_WALKING_COUNT, step.getTotalWalkingCount());
            Mockito.verify(devicePersistencePort).getStepPort(DEVICE_ID);
            Mockito.verify(devicePersistencePort).createStepPort(Mockito.any());
            Mockito.verify(devicePersistencePort).saveStepPort(Mockito.any());
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
                    .deviceBootedDt(DEVICE_BOOTED_DT)
                    .build();

            Mockito.when(devicePersistencePort.getStepPort(DEVICE_ID)).thenReturn(Optional.of(step));
            Mockito.when(devicePersistencePort.createStepPort(Mockito.any())).thenReturn(step);
            Mockito.when(devicePersistencePort.saveStepPort(Mockito.any())).thenReturn(Optional.empty());

            // act & assert
            UpdateTotalWalkingCountCommand command = UpdateTotalWalkingCountCommand.builder()
                    .deviceId(DEVICE_ID)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedDt(DEVICE_BOOTED_DT)
                    .build();

            assertThrows(NotExistStepException.class, () -> stepUseCase.updateTotalWalkingCountUseCase(command));
            Mockito.verify(devicePersistencePort).getStepPort(DEVICE_ID);
            Mockito.verify(devicePersistencePort, Mockito.never()).createStepPort(Mockito.any());
            Mockito.verify(devicePersistencePort).saveStepPort(Mockito.any());
        }

        @Test
        @DisplayName("부팅 시간이 동일한 경우 걸음 수를 동기화 한다.")
        void updateTotalWalkingCount() {
            // arrange
            int walkingCount = 50;
            int newTotalWalkingCount = TOTAL_WALKING_COUNT + walkingCount;
            Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedDt(DEVICE_BOOTED_DT)
                    .build();

            Mockito.when(devicePersistencePort.getStepPort(DEVICE_ID)).thenReturn(Optional.of(step));
            Mockito.when(devicePersistencePort.saveStepPort(Mockito.any())).thenReturn(Optional.of(step));

            // act
            UpdateTotalWalkingCountCommand command = UpdateTotalWalkingCountCommand.builder()
                    .deviceId(DEVICE_ID)
                    .totalWalkingCount(newTotalWalkingCount)
                    .deviceBootedDt(DEVICE_BOOTED_DT)
                    .build();

            step = stepUseCase.updateTotalWalkingCountUseCase(command);

            // assert
            assertEquals(newTotalWalkingCount, step.getCurrentWalkingCount());
            Mockito.verify(devicePersistencePort).getStepPort(DEVICE_ID);
            Mockito.verify(devicePersistencePort, Mockito.never()).createStepPort(Mockito.any());
            Mockito.verify(devicePersistencePort).saveStepPort(Mockito.any());
        }

        @Test
        @DisplayName("부팅 시간이 동일하지만 현재 총 걸음 수보다 적은 총 걸음 수인 경우 예외가 발생 한다.")
        void updateTotalWalkingCountWhenLeastTotalWalkingCount() {
            // arrange
            int newTotalWalkingCount = 0;
            Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedDt(DEVICE_BOOTED_DT)
                    .build();

            Mockito.when(devicePersistencePort.getStepPort(DEVICE_ID)).thenReturn(Optional.of(step));
            Mockito.when(devicePersistencePort.saveStepPort(Mockito.any())).thenReturn(Optional.of(step));

            // act & assert
            UpdateTotalWalkingCountCommand command = UpdateTotalWalkingCountCommand.builder()
                    .deviceId(DEVICE_ID)
                    .totalWalkingCount(newTotalWalkingCount)
                    .deviceBootedDt(DEVICE_BOOTED_DT)
                    .build();

            assertThrows(InvalidTotalWalkingCountException.class, () -> stepUseCase.updateTotalWalkingCountUseCase(command));
        }

        @Test
        @DisplayName("부팅 시간이 지난 경우 총 걸음 수를 초기화 한다.")
        void resetStep() {
            // arrange
            int newTotalWalkingCount = 50;
            LocalDateTime newDeviceBootedDt = LocalDateTime.of(2025, 1, 2, 0, 0);
            Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedDt(DEVICE_BOOTED_DT)
                    .build();

            Mockito.when(devicePersistencePort.getStepPort(DEVICE_ID)).thenReturn(Optional.of(step));
            Mockito.when(devicePersistencePort.saveStepPort(Mockito.any())).thenReturn(Optional.of(step));

            // act
            UpdateTotalWalkingCountCommand command = UpdateTotalWalkingCountCommand.builder()
                    .deviceId(DEVICE_ID)
                    .totalWalkingCount(newTotalWalkingCount)
                    .deviceBootedDt(newDeviceBootedDt)
                    .build();

            step = stepUseCase.updateTotalWalkingCountUseCase(command);

            // assert
            assertEquals(newDeviceBootedDt, step.getDeviceBootedDt());
            assertEquals(TOTAL_WALKING_COUNT + newTotalWalkingCount, step.getCurrentWalkingCount());
            Mockito.verify(devicePersistencePort).getStepPort(DEVICE_ID);
            Mockito.verify(devicePersistencePort, Mockito.never()).createStepPort(Mockito.any());
            Mockito.verify(devicePersistencePort).saveStepPort(Mockito.any());
        }

    }

    @Nested
    @DisplayName("총 걸음 수 증가 단위 테스트")
    class IncreaseCurrentWalkingCountUseCase {

        @Test
        @DisplayName("총 걸음 수를 증가 시킨다.")
        void increaseCurrentWalkingCount() {
            // arrange
            int increaseWalkingCount = 1000;
            Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedDt(DEVICE_BOOTED_DT)
                    .build();

            Mockito.when(devicePersistencePort.getStepPort(DEVICE_ID)).thenReturn(Optional.of(step));
            Mockito.when(devicePersistencePort.saveStepPort(Mockito.any())).thenReturn(Optional.of(step));

            // act
            IncreaseCurrentWalkingCountCommand command = IncreaseCurrentWalkingCountCommand.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(increaseWalkingCount)
                    .build();

            step = stepUseCase.increaseCurrentWalkingCountUseCase(command);

            // assert
            assertEquals(TOTAL_WALKING_COUNT + increaseWalkingCount, step.getCurrentWalkingCount());
            Mockito.verify(devicePersistencePort).getStepPort(DEVICE_ID);
            Mockito.verify(devicePersistencePort).saveStepPort(Mockito.any());
            Mockito.verify(devicePublishPort).publishCurrentWalkingCountPort(Mockito.any());
        }

        @Test
        @DisplayName("걸음 수가 없는 경우 예외가 발생한다.")
        void notExistsStep() {
            // arrange
            final Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .deviceBootedDt(DEVICE_BOOTED_DT)
                    .build();

            Mockito.when(devicePersistencePort.getStepPort(DEVICE_ID)).thenReturn(Optional.empty());
            Mockito.when(devicePersistencePort.saveStepPort(Mockito.any())).thenReturn(Optional.of(step));

            // act & assert
            IncreaseCurrentWalkingCountCommand command = IncreaseCurrentWalkingCountCommand.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(100)
                    .build();

            assertThrows(NotExistStepException.class, () -> stepUseCase.increaseCurrentWalkingCountUseCase(command));
            Mockito.verify(devicePersistencePort).getStepPort(DEVICE_ID);
            Mockito.verify(devicePersistencePort, Mockito.never()).saveStepPort(Mockito.any());
            Mockito.verify(devicePublishPort, Mockito.never()).publishCurrentWalkingCountPort(Mockito.any());
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
                    .deviceBootedDt(DEVICE_BOOTED_DT)
                    .build();

            Mockito.when(devicePersistencePort.getStepPort(DEVICE_ID)).thenReturn(Optional.of(step));
            Mockito.when(devicePersistencePort.saveStepPort(Mockito.any())).thenReturn(Optional.empty());

            // act & assert
            IncreaseCurrentWalkingCountCommand command = IncreaseCurrentWalkingCountCommand.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(100)
                    .build();

            assertThrows(NotExistStepException.class, () -> stepUseCase.increaseCurrentWalkingCountUseCase(command));
            Mockito.verify(devicePersistencePort).getStepPort(DEVICE_ID);
            Mockito.verify(devicePersistencePort).saveStepPort(Mockito.any());
            Mockito.verify(devicePublishPort, Mockito.never()).publishCurrentWalkingCountPort(Mockito.any());
        }
    }
}