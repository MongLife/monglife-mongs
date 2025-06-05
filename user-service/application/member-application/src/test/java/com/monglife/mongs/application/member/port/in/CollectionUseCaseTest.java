package com.monglife.mongs.application.member.port.in;

import com.monglife.mongs.application.member.port.exception.InvalidCreateCollectionMapException;
import com.monglife.mongs.application.member.port.exception.InvalidCreateCollectionMongException;
import com.monglife.mongs.application.member.port.in.command.CreateCollectionMapCommand;
import com.monglife.mongs.application.member.port.in.command.CreateCollectionMongCommand;
import com.monglife.mongs.application.member.port.in.command.GetCollectionMapsCommand;
import com.monglife.mongs.application.member.port.in.command.GetCollectionMongsCommand;
import com.monglife.mongs.application.member.port.in.service.CollectionService;
import com.monglife.mongs.application.member.port.out.CollectionPersistencePort;
import com.monglife.mongs.application.member.port.out.CollectionReadPort;
import com.monglife.mongs.domain.member.model.CollectionMap;
import com.monglife.mongs.domain.member.model.CollectionMong;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CollectionUseCaseTest {

    private final CollectionPersistencePort collectionPersistencePort = Mockito.mock(CollectionPersistencePort.class);
    private final CollectionReadPort collectionReadPort = Mockito.mock(CollectionReadPort.class);
    private final CollectionUseCase collectionUseCase = new CollectionService(collectionPersistencePort, collectionReadPort);

    @Nested
    @DisplayName("컬렉션 맵 등록 단위 테스트")
    class CreateCollectionMapUseCase {

        private static final Long ACCOUNT_ID = 1L;

        @Test
        @DisplayName("컬렉션 맵을 등록 한다.")
        void createCollectionMap() {
            // arrange
            final String mapCode = "MP000";
            final CollectionMap collectionMap = CollectionMap.builder()
                    .collectionMapId(1L)
                    .accountId(ACCOUNT_ID)
                    .mapCode(mapCode)
                    .build();

            Mockito.when(collectionReadPort.isExistsCollectionMapPort(ACCOUNT_ID, mapCode)).thenReturn(false);
            Mockito.when(collectionPersistencePort.createCollectionMapPort(Mockito.any())).thenReturn(Optional.of(collectionMap));

            // act & assert
            CreateCollectionMapCommand command = CreateCollectionMapCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .mapCode(mapCode)
                    .build();

            assertDoesNotThrow(() -> collectionUseCase.createCollectionMapUseCase(command));
            Mockito.verify(collectionPersistencePort).createCollectionMapPort(Mockito.any());
        }

        @Test
        @DisplayName("컬렉션 맵이 존재하는 경우 등록하지 않는다.")
        void createCollectionMapWhenNotExistsCollectionMap() {
            // arrange
            final String mapCode = "MP000";
            final CollectionMap collectionMap = CollectionMap.builder()
                    .collectionMapId(1L)
                    .accountId(ACCOUNT_ID)
                    .mapCode(mapCode)
                    .build();

            Mockito.when(collectionReadPort.isExistsCollectionMapPort(ACCOUNT_ID, mapCode)).thenReturn(true);
            Mockito.when(collectionPersistencePort.createCollectionMapPort(Mockito.any())).thenReturn(Optional.of(collectionMap));

            // act & assert
            CreateCollectionMapCommand command = CreateCollectionMapCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .mapCode(mapCode)
                    .build();

            assertDoesNotThrow(() -> collectionUseCase.createCollectionMapUseCase(command));
            Mockito.verify(collectionPersistencePort, Mockito.never()).createCollectionMapPort(Mockito.any());
        }

        @Test
        @DisplayName("컬렉션 맵 등록에 실패하는 경우 예외가 발생 한다.")
        void createCollectionMapWhenNotExistsMapCode() {
            // arrange
            final String mapCode = "MP___";

            Mockito.when(collectionReadPort.isExistsCollectionMapPort(ACCOUNT_ID, mapCode)).thenReturn(false);
            Mockito.when(collectionPersistencePort.createCollectionMapPort(Mockito.any())).thenReturn(Optional.empty());

            // act & assert
            CreateCollectionMapCommand command = CreateCollectionMapCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .mapCode(mapCode)
                    .build();

            assertThrows(InvalidCreateCollectionMapException.class, () -> collectionUseCase.createCollectionMapUseCase(command));
            Mockito.verify(collectionPersistencePort).createCollectionMapPort(Mockito.any());
        }
    }

    @Nested
    @DisplayName("컬렉션 몽 등록 단위 테스트")
    class CreateCollectionMongUseCase {

        private static final Long ACCOUNT_ID = 1L;

        @Test
        @DisplayName("컬렉션 몽을 등록 한다.")
        void createCollectionMong() {
            // arrange
            final String mongCode = "CH000";
            final CollectionMong collectionMong = CollectionMong.builder()
                    .collectionMongId(1L)
                    .accountId(ACCOUNT_ID)
                    .mongCode(mongCode)
                    .build();

            Mockito.when(collectionReadPort.isExistsCollectionMongPort(ACCOUNT_ID, mongCode)).thenReturn(false);
            Mockito.when(collectionPersistencePort.createCollectionMongPort(Mockito.any())).thenReturn(Optional.of(collectionMong));

            // act & assert
            CreateCollectionMongCommand command = CreateCollectionMongCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .mongCode(mongCode)
                    .build();

            assertDoesNotThrow(() -> collectionUseCase.createCollectionMongUseCase(command));
            Mockito.verify(collectionPersistencePort).createCollectionMongPort(Mockito.any());
        }

        @Test
        @DisplayName("컬렉션 몽이 존재하는 경우 등록하지 않는다.")
        void createCollectionMongWhenNotExistsCollectionMong() {
            // arrange
            final String mongCode = "CH000";
            final CollectionMong collectionMong = CollectionMong.builder()
                    .collectionMongId(1L)
                    .accountId(ACCOUNT_ID)
                    .mongCode(mongCode)
                    .build();

            Mockito.when(collectionReadPort.isExistsCollectionMongPort(ACCOUNT_ID, mongCode)).thenReturn(true);
            Mockito.when(collectionPersistencePort.createCollectionMongPort(Mockito.any())).thenReturn(Optional.of(collectionMong));

            // act & assert
            CreateCollectionMongCommand command = CreateCollectionMongCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .mongCode(mongCode)
                    .build();

            assertDoesNotThrow(() -> collectionUseCase.createCollectionMongUseCase(command));
            Mockito.verify(collectionPersistencePort, Mockito.never()).createCollectionMongPort(Mockito.any());
        }

        @Test
        @DisplayName("컬렉션 몽 등록에 실패하는 경우 예외가 발생 한다.")
        void createCollectionMongWhenNotExistsMongCode() {
            // arrange
            final String mongCode = "CH___";

            Mockito.when(collectionReadPort.isExistsCollectionMongPort(ACCOUNT_ID, mongCode)).thenReturn(false);
            Mockito.when(collectionPersistencePort.createCollectionMongPort(Mockito.any())).thenReturn(Optional.empty());

            // act & assert
            CreateCollectionMongCommand command = CreateCollectionMongCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .mongCode(mongCode)
                    .build();

            assertThrows(InvalidCreateCollectionMongException.class, () -> collectionUseCase.createCollectionMongUseCase(command));
            Mockito.verify(collectionPersistencePort).createCollectionMongPort(Mockito.any());
        }
    }

    @Nested
    @DisplayName("컬렉션 맵 목록 조회 단위 테스트")
    class GetCollectionMapsUseCase {

        private static final Long ACCOUNT_ID = 1L;

        @Test
        @DisplayName("컬렉션 맵 목록을 조회 한다.")
        void getCollectionMaps() {
            // arrange
            final List<CollectionMap> collectionMaps = List.of(
                    CollectionMap.builder()
                            .collectionMapId(1L)
                            .accountId(ACCOUNT_ID)
                            .mapCode("MP000")
                            .build(),
                    CollectionMap.builder()
                            .collectionMapId(2L)
                            .accountId(ACCOUNT_ID)
                            .mapCode("MP001")
                            .build(),
                    CollectionMap.builder()
                            .collectionMapId(3L)
                            .accountId(ACCOUNT_ID)
                            .mapCode("MP002")
                            .build());

            Mockito.when(collectionReadPort.getCollectionMapsPort(ACCOUNT_ID)).thenReturn(collectionMaps);

            // act
            GetCollectionMapsCommand command = GetCollectionMapsCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .build();

            List<CollectionMap> expected = collectionUseCase.getCollectionMapsUseCase(command);

            // assert
            assertEquals(collectionMaps, expected);
            Mockito.verify(collectionReadPort).getCollectionMapsPort(command.getAccountId());
        }
    }

    @Nested
    @DisplayName("컬렉션 몽 목록 조회 단위 테스트")
    class GetCollectionMongsUseCase {

        private static final Long ACCOUNT_ID = 1L;

        @Test
        @DisplayName("컬렉션 몽 목록을 조회 한다.")
        void getCollectionMongs() {
            // arrange
            final List<CollectionMong> collectionMongs = List.of(
                    CollectionMong.builder()
                            .collectionMongId(1L)
                            .accountId(ACCOUNT_ID)
                            .mongCode("CH000")
                            .build(),
                    CollectionMong.builder()
                            .collectionMongId(2L)
                            .accountId(ACCOUNT_ID)
                            .mongCode("CH001")
                            .build(),
                    CollectionMong.builder()
                            .collectionMongId(3L)
                            .accountId(ACCOUNT_ID)
                            .mongCode("CH002")
                            .build());

            Mockito.when(collectionReadPort.getCollectionMongsPort(ACCOUNT_ID)).thenReturn(collectionMongs);

            // act
            GetCollectionMongsCommand command = GetCollectionMongsCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .build();

            List<CollectionMong> expected = collectionUseCase.getCollectionMongsUseCase(command);

            // assert
            assertEquals(collectionMongs, expected);
            Mockito.verify(collectionReadPort).getCollectionMongsPort(command.getAccountId());
        }
    }
}