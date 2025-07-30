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
import com.monglife.mongs.application.member.port.out.CollectionReadPort;
import com.monglife.mongs.application.member.port.out.vo.CreateCollectionMapVo;
import com.monglife.mongs.application.member.port.out.vo.CreateCollectionMongVo;
import com.monglife.mongs.domain.member.model.CollectionMap;
import com.monglife.mongs.domain.member.model.CollectionMong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {
        AdapterOutMemberPersistenceConfig.class,
        MemberDataSourceConfig.class,
        HibernateAutoConfig.class
})
class CollectionPersistenceServiceTest {

    private final CollectionPersistencePort collectionPersistencePort;
    private final CollectionReadPort collectionReadPort;
    private final CollectionMapRepository collectionMapRepository;
    private final CollectionMongRepository collectionMongRepository;
    private final GroupCodeRepository groupCodeRepository;
    private final ComnCodeRepository comnCodeRepository;

    @Autowired
    public CollectionPersistenceServiceTest(CollectionPersistencePort collectionPersistencePort, CollectionReadPort collectionReadPort, CollectionMapRepository collectionMapRepository, CollectionMongRepository collectionMongRepository, GroupCodeRepository groupCodeRepository, ComnCodeRepository comnCodeRepository) {
        this.collectionPersistencePort = collectionPersistencePort;
        this.collectionReadPort = collectionReadPort;
        this.collectionMapRepository = collectionMapRepository;
        this.collectionMongRepository = collectionMongRepository;
        this.groupCodeRepository = groupCodeRepository;
        this.comnCodeRepository = comnCodeRepository;
    }

    private static final Long ACCOUNT_ID = 1L;
    private static final List<ComnCodeEntity> MAP_TYPE_ENTITIES = new ArrayList<>();
    private static final List<ComnCodeEntity> MONG_TYPE_ENTITIES = new ArrayList<>();
    private static final GroupCodeEntity MAP_GROUP_CODE_ENTITY = new GroupCodeEntity("MP", "맵 그룹 코드");
    private static final GroupCodeEntity MONG_GROUP_CODE_ENTITY = new GroupCodeEntity("CH", "캐릭터 그룹 코드");
    private static final ComnCodeEntity MAP_TYPE = new ComnCodeEntity("MP000", "맵0", MAP_GROUP_CODE_ENTITY);
    private static final ComnCodeEntity MONG_TYPE = new ComnCodeEntity("CH000", "캐릭터0", MONG_GROUP_CODE_ENTITY);

    @BeforeEach
    void beforeEach() {
        // 맵 그룹 코드 등록
        groupCodeRepository.saveAndFlush(MAP_GROUP_CODE_ENTITY);

        // 몽 그룹 코드 등록
        groupCodeRepository.saveAndFlush(MONG_GROUP_CODE_ENTITY);

        // 맵 공통 코드 임시 리스트 초기화
        MAP_TYPE_ENTITIES.clear();
        comnCodeRepository.saveAndFlush(MAP_TYPE);
        MAP_TYPE_ENTITIES.add(MAP_TYPE);

        // 몽 공통 코드 임시 리스트 초기화
        MONG_TYPE_ENTITIES.clear();
        comnCodeRepository.saveAndFlush(MONG_TYPE);
        MONG_TYPE_ENTITIES.add(MONG_TYPE);

        // 맵 공통 코드 등록
        for (int index = 1; index < 10; index++) {
            ComnCodeEntity comnCodeEntity = new ComnCodeEntity("MP00" + index, "맵" + index, MAP_GROUP_CODE_ENTITY);
            MAP_TYPE_ENTITIES.add(comnCodeEntity);
            comnCodeRepository.saveAndFlush(comnCodeEntity);
        }

        // 몽 공통 코드 등록
        for (int index = 1; index < 10; index++) {
            ComnCodeEntity comnCodeEntity = new ComnCodeEntity("CH00" + index, "캐릭터" + index, MONG_GROUP_CODE_ENTITY);
            MONG_TYPE_ENTITIES.add(comnCodeEntity);
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
            final CreateCollectionMapVo createCollectionMapVo = CreateCollectionMapVo.builder()
                    .accountId(ACCOUNT_ID)
                    .mapCode(MAP_TYPE.getCode())
                    .build();

            // act
            var expected = collectionPersistencePort.createCollectionMapPort(createCollectionMapVo);

            // assert
            assertTrue(expected.isPresent());
        }

        @Test
        @DisplayName("맵 코드가 공통 코드 테이블에 없는 경우 빈 옵셔널 객체를 반환 한다.")
        void notExistsMapCode() {
            // arrange
            final String mapCode = "MP___";
            final CreateCollectionMapVo createCollectionMapVo = CreateCollectionMapVo.builder()
                    .accountId(ACCOUNT_ID)
                    .mapCode(mapCode)
                    .build();

            // act
            var expected = collectionPersistencePort.createCollectionMapPort(createCollectionMapVo);

            // assert
            assertTrue(expected.isEmpty());
        }
    }

    @Nested
    @DisplayName("컬렉션 몽 등록 단위 테스트")
    class CreateCollectionMongPort {

        @Test
        @DisplayName("컬렉션 몽을 등록 한다.")
        void createCollectionMong() {
            // arrange
            final CreateCollectionMongVo createCollectionMongVo = CreateCollectionMongVo.builder()
                    .accountId(ACCOUNT_ID)
                    .mongCode(MONG_TYPE.getCode())
                    .build();

            // act
            var expected = collectionPersistencePort.createCollectionMongPort(createCollectionMongVo);

            // assert
            assertTrue(expected.isPresent());
        }

        @Test
        @DisplayName("몽 코드가 공통 코드 테이블에 없는 경우 빈 옵셔널 객체를 반환 한다.")
        void notExistsMongCode() {
            // arrange
            final String mongCode = "CH___";
            final CreateCollectionMongVo createCollectionMongVo = CreateCollectionMongVo.builder()
                    .accountId(ACCOUNT_ID)
                    .mongCode(mongCode)
                    .build();

            // act
            var expected = collectionPersistencePort.createCollectionMongPort(createCollectionMongVo);

            // assert
            assertTrue(expected.isEmpty());
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
                    .accountId(ACCOUNT_ID)
                    .comn(MAP_TYPE)
                    .build());

            // act
            var expected = collectionReadPort.isExistsCollectionMapPort(ACCOUNT_ID, MAP_TYPE.getCode());

            // assert
            assertTrue(expected);
        }

        @Test
        @DisplayName("컬렉션 맵이 존재하지 않는 경우 false를 반환 한다.")
        void notExistsCollectionMap() {
            // arrange
            final String mapCode = "MP___";

            // act
            var expected = collectionReadPort.isExistsCollectionMapPort(ACCOUNT_ID, mapCode);

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
                    .accountId(ACCOUNT_ID)
                    .comn(MONG_TYPE)
                    .build());

            // act
            var expected = collectionReadPort.isExistsCollectionMongPort(ACCOUNT_ID, MONG_TYPE.getCode());

            // assert
            assertTrue(expected);
        }

        @Test
        @DisplayName("컬렉션 몽이 존재하지 않는 경우 false를 반환 한다.")
        void notExistsCollectionMong() {
            // arrange
            final String mongCode = "CH___";

            // act
            var expected = collectionReadPort.isExistsCollectionMongPort(ACCOUNT_ID, mongCode);

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
                    .accountId(ACCOUNT_ID)
                    .comn(MAP_TYPE)
                    .build());

            // act
            var expected = collectionReadPort.getCollectionMapsPort(ACCOUNT_ID);

            // assert
            for (int index = 0; index < MAP_TYPE_ENTITIES.size(); index++) {
                CollectionMap collectionMap = expected.get(index);

                if (MAP_TYPE.getCode().equals(collectionMap.getMapCode())) {
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
                    .accountId(ACCOUNT_ID)
                    .comn(MONG_TYPE)
                    .build());

            // act
            var expected = collectionReadPort.getCollectionMongsPort(ACCOUNT_ID);

            // assert
            for (int index = 0; index < MONG_TYPE_ENTITIES.size(); index++) {
                CollectionMong collectionMong = expected.get(index);

                if (MONG_TYPE.getCode().equals(collectionMong.getMongCode())) {
                    assertTrue(collectionMong.getIsIncluded());
                } else {
                    assertFalse(collectionMong.getIsIncluded());
                }
            }
        }
    }
}