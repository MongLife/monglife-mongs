package com.monglife.mongs.application.mong.port.in.service;

import com.monglife.mongs.application.mong.port.exception.NotExistsMongException;
import com.monglife.mongs.application.mong.port.exception.NotExistsTrainingTypeException;
import com.monglife.mongs.application.mong.port.in.ActivityUseCase;
import com.monglife.mongs.application.mong.port.in.command.GetTrainingTypeCommand;
import com.monglife.mongs.application.mong.port.in.command.TrainingEndCommand;
import com.monglife.mongs.application.mong.port.out.MongPersistencePort;
import com.monglife.mongs.application.mong.port.out.MongReadPort;
import com.monglife.mongs.domain.mong.model.Mong;
import com.monglife.mongs.domain.mong.model.TrainingType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ActivityServiceTest {

    private final MongPersistencePort mongPersistencePort;

    private final MongReadPort mongReadPort;

    private final ActivityUseCase activityUseCase;

    public ActivityServiceTest() {
        this.mongPersistencePort = Mockito.mock(MongPersistencePort.class);
        this.mongReadPort = Mockito.mock(MongReadPort.class);
        this.activityUseCase = new ActivityService(mongPersistencePort, mongReadPort);
    }

    @Nested
    @DisplayName("훈련 타입 목록 조회 단위 테스트")
    class GetTrainingTypesUseCase {

        @Test
        @DisplayName("훈련 타입 정보 목록을 조회 한다.")
        void getTrainingTypes() {
            // arrange
            List<TrainingType> trainingTypes = List.of(
                    new TrainingType(1L, "TEST-TRAINING-TYPE-CODE", "테스트 훈련 타입", 10, 100, 60, 1D, 2D, 3D, 4D, 5D),
                    new TrainingType(1L, "TEST-TRAINING-TYPE-CODE", "테스트 훈련 타입", 10, 100, 60, 1D, 2D, 3D, 4D, 5D)
            );

            Mockito.when(mongReadPort.getTrainingTypesPort()).thenReturn(trainingTypes);

            // act
            List<TrainingType> expected = activityUseCase.getTrainingTypesUseCase();

            // assert
            assertEquals(expected.size(), trainingTypes.size());
        }
    }

    @Nested
    @DisplayName("훈련 타입 조회 단위 테스트")
    class GetTrainingTypeUseCase {

        @Test
        @DisplayName("훈련 타입 정보를 조회 한다.")
        void getTrainingType() {
            // arrange
            String trainingTypeCode = "TEST-TRAINING-TYPE-CODE";
            TrainingType trainingType = new TrainingType(1L, trainingTypeCode, "테스트 훈련 타입", 10, 100, 60, 1D, 2D, 3D, 4D, 5D);

            Mockito.when(mongReadPort.getTrainingTypePort(trainingTypeCode)).thenReturn(Optional.of(trainingType));

            // act
            GetTrainingTypeCommand command = GetTrainingTypeCommand.builder()
                    .trainingTypeCode(trainingTypeCode)
                    .build();

            TrainingType expected = activityUseCase.getTrainingTypeUseCase(command);

            // assert
            assertEquals(trainingTypeCode, expected.getTrainingTypeCode());
        }

        @Test
        @DisplayName("훈련 타입이 없는 경우 예외가 발생 한다.")
        void getTrainingTypeWhenNotExistsTrainingType() {
            // arrange
            String trainingTypeCode = "TEST-TRAINING-TYPE-CODE";

            Mockito.when(mongReadPort.getTrainingTypePort(trainingTypeCode)).thenReturn(Optional.empty());

            // act & assert
            GetTrainingTypeCommand command = GetTrainingTypeCommand.builder()
                    .trainingTypeCode(trainingTypeCode)
                    .build();

            assertThrows(NotExistsTrainingTypeException.class, () -> activityUseCase.getTrainingTypeUseCase(command));
        }
    }

    @Nested
    @DisplayName("훈련 완료 단위 테스트")
    class TrainingEndUseCase {

        @Test
        @DisplayName("훈련을 완료하고 스코어를 달성시 보상을 받는다")
        void trainingEnd() {
            // arrange
            String trainingTypeCode = "TEST-TRAINING-TYPE-CODE";
            int score = 100;
            int payPoint = 10;
            double status = 10D;
            TrainingType trainingType = new TrainingType(1L, trainingTypeCode, "테스트 훈련 타입", payPoint, score, 60, status, -status, -status, -status, -status);

            long mongId = 1L;
            long accountId = 1L;
            double maxStatus = 100D;
            Mong mong = MongTestUtil.getFirstLevelMong(mongId, accountId, maxStatus);

            Mockito.when(mongReadPort.getTrainingTypePort(trainingTypeCode)).thenReturn(Optional.of(trainingType));
            Mockito.when(mongPersistencePort.getMongPort(mongId)).thenReturn(Optional.of(mong));
            Mockito.when(mongPersistencePort.saveMongPort(mong)).thenReturn(Optional.of(mong));

            // act
            TrainingEndCommand command = TrainingEndCommand.builder()
                    .accountId(accountId)
                    .trainingTypeCode(trainingTypeCode)
                    .mongId(mongId)
                    .score(score)
                    .build();

            Mong expected = activityUseCase.trainingEndUseCase(command);

            // assert
            assertEquals(status, expected.getExp());
            assertEquals(maxStatus - status, expected.getStrength());
            assertEquals(maxStatus - status, expected.getSatiety());
            assertEquals(maxStatus - status, expected.getFatigue());
            assertEquals(maxStatus - status, expected.getWeight());
            assertEquals(1, expected.getTrainingCount());
            assertEquals(payPoint, expected.getPayPoint());
        }

        @Test
        @DisplayName("훈련 타입이 없는 경우 예외가 발생 한다.")
        void trainingEndWhenNotExistsTrainingType() {
            // arrange
            String trainingTypeCode = "TEST-TRAINING-TYPE-CODE";

            long mongId = 1L;
            long accountId = 1L;
            double maxStatus = 100D;
            Mong mong = MongTestUtil.getEggMong(mongId, accountId, maxStatus);

            Mockito.when(mongReadPort.getTrainingTypePort(trainingTypeCode)).thenReturn(Optional.empty());
            Mockito.when(mongPersistencePort.getMongPort(mongId)).thenReturn(Optional.of(mong));
            Mockito.when(mongPersistencePort.saveMongPort(mong)).thenReturn(Optional.of(mong));

            // act & assert
            TrainingEndCommand command = TrainingEndCommand.builder()
                    .accountId(accountId)
                    .trainingTypeCode(trainingTypeCode)
                    .mongId(mongId)
                    .score(0)
                    .build();

            assertThrows(NotExistsTrainingTypeException.class, () -> activityUseCase.trainingEndUseCase(command));
        }

        @Test
        @DisplayName("몽이 없는 경우 예외가 발생 한다.")
        void trainingEndWhenNotExistsMong() {
            // arrange
            String trainingTypeCode = "TEST-TRAINING-TYPE-CODE";
            int score = 100;
            int payPoint = 10;
            double status = 10D;
            TrainingType trainingType = new TrainingType(1L, trainingTypeCode, "테스트 훈련 타입", payPoint, score, 60, status, -status, -status, -status, -status);

            long mongId = 1L;
            long accountId = 1L;

            Mockito.when(mongReadPort.getTrainingTypePort(trainingTypeCode)).thenReturn(Optional.of(trainingType));
            Mockito.when(mongPersistencePort.getMongPort(mongId)).thenReturn(Optional.empty());

            // act & assert
            TrainingEndCommand command = TrainingEndCommand.builder()
                    .accountId(accountId)
                    .trainingTypeCode(trainingTypeCode)
                    .mongId(mongId)
                    .score(0)
                    .build();

            assertThrows(NotExistsMongException.class, () -> activityUseCase.trainingEndUseCase(command));
        }
    }
}