package com.monglife.mongs.adapter.out.member.persistence.service;

import com.monglife.module.common.jpa.config.HibernateAutoConfig;
import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import com.monglife.module.common.jpa.entity.GroupCodeEntity;
import com.monglife.mongs.adapter.out.member.persistence.config.AdapterOutMemberPersistenceConfig;
import com.monglife.mongs.adapter.out.member.persistence.config.MemberDataSourceConfig;
import com.monglife.mongs.adapter.out.member.persistence.entity.CollectionMapEntity;
import com.monglife.mongs.adapter.out.member.persistence.entity.CollectionMongEntity;
import com.monglife.mongs.adapter.out.member.persistence.repository.CollectionMapRepository;
import com.monglife.mongs.adapter.out.member.persistence.repository.CollectionMongRepository;
import com.monglife.mongs.adapter.out.member.persistence.repository.ComnCodeRepository;
import com.monglife.mongs.adapter.out.member.persistence.repository.GroupCodeRepository;
import com.monglife.mongs.application.member.port.out.CollectionPersistencePort;
import com.monglife.mongs.application.member.port.out.vo.CreateCollectionMapVo;
import com.monglife.mongs.application.member.port.out.vo.CreateCollectionMongVo;
import com.monglife.mongs.domain.model.CollectionMap;
import com.monglife.mongs.domain.model.CollectionMong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = { AdapterOutMemberPersistenceConfig.class, MemberDataSourceConfig.class, HibernateAutoConfig.class })
class CollectionPersistenceServiceTest {

    private final CollectionPersistencePort collectionPersistencePort;

    private final CollectionMapRepository collectionMapRepository;

    private final CollectionMongRepository collectionMongRepository;

    private final GroupCodeRepository groupCodeRepository;

    private final ComnCodeRepository comnCodeRepository;

    @Autowired
    public CollectionPersistenceServiceTest(CollectionPersistencePort collectionPersistencePort, CollectionMapRepository collectionMapRepository, CollectionMongRepository collectionMongRepository, GroupCodeRepository groupCodeRepository, ComnCodeRepository comnCodeRepository) {
        this.collectionPersistencePort = collectionPersistencePort;
        this.collectionMapRepository = collectionMapRepository;
        this.collectionMongRepository = collectionMongRepository;
        this.groupCodeRepository = groupCodeRepository;
        this.comnCodeRepository = comnCodeRepository;
    }

    private static final Long accountId = 1L;
    private static final List<ComnCodeEntity> mapTypeEntities = new ArrayList<>();
    private static final List<ComnCodeEntity> mongTypeEntities = new ArrayList<>();
    private static final GroupCodeEntity mapGroupCodeEntity = new GroupCodeEntity("MP", "맵 그룹 코드");
    private static final GroupCodeEntity mongGroupCodeEntity = new GroupCodeEntity("CH", "캐릭터 그룹 코드");
    private static final ComnCodeEntity mapType = new ComnCodeEntity("MP000", "맵0", mapGroupCodeEntity);
    private static final ComnCodeEntity mongType = new ComnCodeEntity("CH000", "캐릭터0", mongGroupCodeEntity);

    @BeforeEach
    void beforeEach() {
        // 맵 그룹 코드 등록
        groupCodeRepository.saveAndFlush(mapGroupCodeEntity);

        // 몽 그룹 코드 등록
        groupCodeRepository.saveAndFlush(mongGroupCodeEntity);

        // 맵 공통 코드 임시 리스트 초기화
        mapTypeEntities.clear();
        comnCodeRepository.saveAndFlush(mapType);
        mapTypeEntities.add(mapType);

        // 몽 공통 코드 임시 리스트 초기화
        mongTypeEntities.clear();
        comnCodeRepository.saveAndFlush(mongType);
        mongTypeEntities.add(mongType);

        // 맵 공통 코드 등록
        for (int index = 1; index < 10; index++) {
            ComnCodeEntity comnCodeEntity = new ComnCodeEntity("MP00" + index, "맵" + index, mapGroupCodeEntity);
            mapTypeEntities.add(comnCodeEntity);
            comnCodeRepository.saveAndFlush(comnCodeEntity);
        }

        // 몽 공통 코드 등록
        for (int index = 1; index < 10; index++) {
            ComnCodeEntity comnCodeEntity = new ComnCodeEntity("CH00" + index, "캐릭터" + index, mongGroupCodeEntity);
            mongTypeEntities.add(comnCodeEntity);
            comnCodeRepository.saveAndFlush(comnCodeEntity);
        }
    }

    @Nested
    @DisplayName("컬렉션 맵 등록 단위 테스트")
    class CreateCollectionMapPort {

        @Test
        @DisplayName("컬렉션 맵을 등록 한다.")
        void createCollectionMap() {
            // arrange
            CreateCollectionMapVo createCollectionMapVo = CreateCollectionMapVo.builder()
                    .accountId(accountId)
                    .mapTypeCode(mapType.getCode())
                    .build();

            // act
            Optional<CollectionMap> collectionMapOptional = collectionPersistencePort.createCollectionMapPort(createCollectionMapVo);

            // assert
            assertTrue(collectionMapOptional.isPresent());
        }

        @Test
        @DisplayName("맵 코드가 공통 코드 테이블에 없는 경우 빈 옵셔널 객체를 반환 한다.")
        void notExistsMapTypeCode() {
            // arrange
            String mapTypeCode = "MP___";
            CreateCollectionMapVo createCollectionMapVo = CreateCollectionMapVo.builder()
                    .accountId(accountId)
                    .mapTypeCode(mapTypeCode)
                    .build();

            // act
            Optional<CollectionMap> collectionMapOptional = collectionPersistencePort.createCollectionMapPort(createCollectionMapVo);

            // assert
            assertTrue(collectionMapOptional.isEmpty());
        }
    }

    @Nested
    @DisplayName("컬렉션 몽 등록 단위 테스트")
    class CreateCollectionMongPort {

        @Test
        @DisplayName("컬렉션 몽을 등록 한다.")
        void createCollectionMong() {
            // arrange
            CreateCollectionMongVo createCollectionMongVo = CreateCollectionMongVo.builder()
                    .accountId(accountId)
                    .mongTypeCode(mongType.getCode())
                    .build();

            // act
            Optional<CollectionMong> collectionMongOptional = collectionPersistencePort.createCollectionMongPort(createCollectionMongVo);

            // assert
            assertTrue(collectionMongOptional.isPresent());
        }

        @Test
        @DisplayName("몽 코드가 공통 코드 테이블에 없는 경우 빈 옵셔널 객체를 반환 한다.")
        void notExistsMongTypeCode() {
            // arrange
            String mongTypeCode = "CH___";
            CreateCollectionMongVo createCollectionMongVo = CreateCollectionMongVo.builder()
                    .accountId(accountId)
                    .mongTypeCode(mongTypeCode)
                    .build();

            // act
            Optional<CollectionMong> collectionMongOptional = collectionPersistencePort.createCollectionMongPort(createCollectionMongVo);

            // assert
            assertTrue(collectionMongOptional.isEmpty());
        }
    }

    @Nested
    @DisplayName("컬렉션 맵 존재 여부 조회 단위 테스트")
    class IsExistsCollectionMapPort {

        @Test
        @DisplayName("컬렉션 맵이 존재하는 경우 true를 반환 한다.")
        void isExistsCollectionMap() {
            // arrange
            collectionMapRepository.saveAndFlush(CollectionMapEntity.builder()
                    .accountId(accountId)
                    .mapType(mapType)
                    .build());

            // act
            Boolean expected = collectionPersistencePort.isExistsCollectionMapPort(accountId, mapType.getCode());

            // assert
            assertTrue(expected);
        }

        @Test
        @DisplayName("컬렉션 맵이 존재하지 않는 경우 false를 반환 한다.")
        void notExistsCollectionMap() {
            // arrange
            String mapTypeCode = "MP___";

            // act
            Boolean expected = collectionPersistencePort.isExistsCollectionMapPort(accountId, mapTypeCode);

            // assert
            assertFalse(expected);
        }
    }

    @Nested
    @DisplayName("컬렉션 몽 존재 여부 조회 단위 테스트")
    class IsExistsCollectionMongPort {

        @Test
        @DisplayName("컬렉션 몽이 존재하는 경우 true를 반환 한다.")
        void isExistsCollectionMong() {
            // arrange
            collectionMongRepository.saveAndFlush(CollectionMongEntity.builder()
                    .accountId(accountId)
                    .mongType(mongType)
                    .build());

            // act
            Boolean expected = collectionPersistencePort.isExistsCollectionMongPort(accountId, mongType.getCode());

            // assert
            assertTrue(expected);
        }

        @Test
        @DisplayName("컬렉션 몽이 존재하지 않는 경우 false를 반환 한다.")
        void notExistsCollectionMong() {
            // arrange
            String mongTypeCode = "CH___";

            // act
            Boolean expected = collectionPersistencePort.isExistsCollectionMongPort(accountId, mongTypeCode);

            // assert
            assertFalse(expected);
        }
    }

    @Nested
    @DisplayName("컬렉션 맵 목록 조회 단위 테스트")
    class GetCollectionMapsPort {

        @Test
        @DisplayName("컬렉션 맵 목록을 조회 한다.")
        void getCollectionMaps() {
            // arrange
            collectionMapRepository.saveAndFlush(CollectionMapEntity.builder()
                    .accountId(accountId)
                    .mapType(mapType)
                    .build());

            // act
            List<CollectionMap> collectionMaps = collectionPersistencePort.getCollectionMapsPort(accountId);

            // assert
            for (int index = 0; index < mapTypeEntities.size(); index++) {
                CollectionMap collectionMap = collectionMaps.get(index);

                if (mapType.getCode().equals(collectionMap.getMapTypeCode())) {
                    assertTrue(collectionMap.getIsIncluded());
                } else {
                    assertFalse(collectionMap.getIsIncluded());
                }
            }
        }
    }

    @Nested
    @DisplayName("컬렉션 몽 목록 조회 단위 테스트")
    class GetCollectionMongsPort {

        @Test
        @DisplayName("컬렉션 몽 목록을 조회 한다.")
        void getCollectionMongs() {
            // arrange
            collectionMongRepository.saveAndFlush(CollectionMongEntity.builder()
                    .accountId(accountId)
                    .mongType(mongType)
                    .build());

            // act
            List<CollectionMong> collectionMongs = collectionPersistencePort.getCollectionMongsPort(accountId);

            // assert
            for (int index = 0; index < mongTypeEntities.size(); index++) {
                CollectionMong collectionMong = collectionMongs.get(index);

                if (mongType.getCode().equals(collectionMong.getMongTypeCode())) {
                    assertTrue(collectionMong.getIsIncluded());
                } else {
                    assertFalse(collectionMong.getIsIncluded());
                }
            }
        }
    }
}