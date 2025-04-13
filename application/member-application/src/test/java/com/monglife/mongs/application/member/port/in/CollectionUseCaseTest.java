package com.monglife.mongs.application.member.port.in;

import com.monglife.mongs.application.member.port.in.command.CreateCollectionMapCommand;
import com.monglife.mongs.application.member.port.in.command.CreateCollectionMongCommand;
import com.monglife.mongs.application.member.port.in.command.GetCollectionMapsCommand;
import com.monglife.mongs.application.member.port.in.command.GetCollectionMongsCommand;
import com.monglife.mongs.application.member.port.in.service.CollectionService;
import com.monglife.mongs.application.member.port.out.MemberPersistencePort;
import com.monglife.mongs.domain.model.CollectionMap;
import com.monglife.mongs.domain.model.CollectionMong;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CollectionUseCaseTest {

    private final MemberPersistencePort memberPersistencePort;

    private final CollectionService collectionService;

    public CollectionUseCaseTest() {
        this.memberPersistencePort = Mockito.mock(MemberPersistencePort.class);
        this.collectionService = new CollectionService(memberPersistencePort);
    }

    private static final Long accountId = 1L;

    @Nested
    @DisplayName("컬렉션 맵 등록 단위 테스트")
    class CreateCollectionMapUseCase {

        @Test
        @DisplayName("컬렉션 맵을 등록 한다.")
        void createCollectionMap() {
            // arrange
            String mapTypeCode = "MP000";
            CollectionMap collectionMap = CollectionMap.builder()
                    .collectionMapId(1L)
                    .accountId(accountId)
                    .mapTypeCode(mapTypeCode)
                    .build();

            Mockito.when(memberPersistencePort.isExistsCollectionMap(accountId, mapTypeCode)).thenReturn(false);
            Mockito.when(memberPersistencePort.createCollectionMapPort(Mockito.any())).thenReturn(collectionMap);

            // act
            CreateCollectionMapCommand command = CreateCollectionMapCommand.builder()
                    .accountId(accountId)
                    .mapTypeCode(mapTypeCode)
                    .build();

            collectionService.createCollectionMapUseCase(command);

            // assert
            Mockito.verify(memberPersistencePort).isExistsCollectionMap(command.getAccountId(), command.getMapTypeCode());
            Mockito.verify(memberPersistencePort).createCollectionMapPort(Mockito.any());
        }

        @Test
        @DisplayName("컬렉션 맵이 존재하는 경우 등록하지 않는다.")
        void createCollectionMapWhenNotExistsCollectionMap() {
            // arrange
            String mapTypeCode = "MP000";
            CollectionMap collectionMap = CollectionMap.builder()
                    .collectionMapId(1L)
                    .accountId(accountId)
                    .mapTypeCode(mapTypeCode)
                    .build();

            Mockito.when(memberPersistencePort.isExistsCollectionMap(accountId, mapTypeCode)).thenReturn(true);
            Mockito.when(memberPersistencePort.createCollectionMapPort(Mockito.any())).thenReturn(collectionMap);

            // act
            CreateCollectionMapCommand command = CreateCollectionMapCommand.builder()
                    .accountId(accountId)
                    .mapTypeCode(mapTypeCode)
                    .build();

            collectionService.createCollectionMapUseCase(command);

            // assert
            Mockito.verify(memberPersistencePort).isExistsCollectionMap(command.getAccountId(), command.getMapTypeCode());
            Mockito.verify(memberPersistencePort, Mockito.never()).createCollectionMapPort(Mockito.any());
        }
    }

    @Nested
    @DisplayName("컬렉션 몽 등록 단위 테스트")
    class CreateCollectionMongUseCase {

        @Test
        @DisplayName("컬렉션 몽을 등록 한다.")
        void createCollectionMong() {
            // arrange
            String mongTypeCode = "CH000";
            CollectionMong collectionMong = CollectionMong.builder()
                    .collectionMongId(1L)
                    .accountId(accountId)
                    .mongTypeCode(mongTypeCode)
                    .build();

            Mockito.when(memberPersistencePort.isExistsCollectionMong(accountId, mongTypeCode)).thenReturn(false);
            Mockito.when(memberPersistencePort.createCollectionMongPort(Mockito.any())).thenReturn(collectionMong);

            // act
            CreateCollectionMongCommand command = CreateCollectionMongCommand.builder()
                    .accountId(accountId)
                    .mongTypeCode(mongTypeCode)
                    .build();

            collectionService.createCollectionMongUseCase(command);

            // assert
            Mockito.verify(memberPersistencePort).isExistsCollectionMong(command.getAccountId(), command.getMongTypeCode());
            Mockito.verify(memberPersistencePort).createCollectionMongPort(Mockito.any());
        }

        @Test
        @DisplayName("컬렉션 맵이 존재하는 경우 등록하지 않는다.")
        void createCollectionMongWhenNotExistsCollectionMong() {
            // arrange
            String mongTypeCode = "CH000";
            CollectionMong collectionMong = CollectionMong.builder()
                    .collectionMongId(1L)
                    .accountId(accountId)
                    .mongTypeCode(mongTypeCode)
                    .build();

            Mockito.when(memberPersistencePort.isExistsCollectionMong(accountId, mongTypeCode)).thenReturn(true);
            Mockito.when(memberPersistencePort.createCollectionMongPort(Mockito.any())).thenReturn(collectionMong);

            // act
            CreateCollectionMongCommand command = CreateCollectionMongCommand.builder()
                    .accountId(accountId)
                    .mongTypeCode(mongTypeCode)
                    .build();

            collectionService.createCollectionMongUseCase(command);

            // assert
            Mockito.verify(memberPersistencePort).isExistsCollectionMong(command.getAccountId(), command.getMongTypeCode());
            Mockito.verify(memberPersistencePort, Mockito.never()).createCollectionMongPort(Mockito.any());
        }
    }

    @Nested
    @DisplayName("컬렉션 맵 목록 조회 단위 테스트")
    class GetCollectionMapsUseCase {

        @Test
        @DisplayName("컬렉션 맵 목록을 조회 한다.")
        void getCollectionMaps() {
            // arrange
            List<CollectionMap> collectionMaps = List.of(
                    CollectionMap.builder()
                            .collectionMapId(1L)
                            .accountId(accountId)
                            .mapTypeCode("MP000")
                            .build(),
                    CollectionMap.builder()
                            .collectionMapId(2L)
                            .accountId(accountId)
                            .mapTypeCode("MP001")
                            .build(),
                    CollectionMap.builder()
                            .collectionMapId(3L)
                            .accountId(accountId)
                            .mapTypeCode("MP002")
                            .build());

            Mockito.when(memberPersistencePort.getCollectionMapsPort(accountId)).thenReturn(collectionMaps);

            // act
            GetCollectionMapsCommand command = GetCollectionMapsCommand.builder()
                    .accountId(accountId)
                    .build();

            List<CollectionMap> expected = collectionService.getCollectionMapsUseCase(command);

            // assert
            Mockito.verify(memberPersistencePort).getCollectionMapsPort(command.getAccountId());
            assertEquals(collectionMaps, expected);
        }
    }

    @Nested
    @DisplayName("컬렉션 몽 목록 조회 단위 테스트")
    class GetCollectionMongsUseCase {

        @Test
        @DisplayName("컬렉션 몽 목록을 조회 한다.")
        void getCollectionMongs() {
            // arrange
            List<CollectionMong> collectionMongs = List.of(
                    CollectionMong.builder()
                            .collectionMongId(1L)
                            .accountId(accountId)
                            .mongTypeCode("CH000")
                            .build(),
                    CollectionMong.builder()
                            .collectionMongId(2L)
                            .accountId(accountId)
                            .mongTypeCode("CH001")
                            .build(),
                    CollectionMong.builder()
                            .collectionMongId(3L)
                            .accountId(accountId)
                            .mongTypeCode("CH002")
                            .build());

            Mockito.when(memberPersistencePort.getCollectionMongsPort(accountId)).thenReturn(collectionMongs);

            // act
            GetCollectionMongsCommand command = GetCollectionMongsCommand.builder()
                    .accountId(accountId)
                    .build();

            List<CollectionMong> expected = collectionService.getCollectionMongsUseCase(command);

            // assert
            Mockito.verify(memberPersistencePort).getCollectionMongsPort(command.getAccountId());
            assertEquals(collectionMongs, expected);
        }
    }
}