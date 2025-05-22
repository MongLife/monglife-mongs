package com.monglife.mongs.adapter.out.mong.persistence.service;

import com.monglife.core.utils.CommonUtil;
import com.monglife.module.common.jpa.config.HibernateAutoConfig;
import com.monglife.module.common.jpa.config.JpaAuditingAutoConfig;
import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import com.monglife.module.common.jpa.entity.GroupCodeEntity;
import com.monglife.mongs.adapter.out.mong.persistence.config.AdapterOutMongPersistenceConfig;
import com.monglife.mongs.adapter.out.mong.persistence.config.MongDataSourceConfig;
import com.monglife.mongs.adapter.out.mong.persistence.config.MongRedisConfig;
import com.monglife.mongs.adapter.out.mong.persistence.entity.MongStrokeHistoryEntity;
import com.monglife.mongs.adapter.out.mong.persistence.entity.MongTypeEntity;
import com.monglife.mongs.adapter.out.mong.persistence.repository.*;
import com.monglife.mongs.application.mong.port.out.MongReadPort;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {
        AdapterOutMongPersistenceConfig.class,
        MongDataSourceConfig.class,
        HibernateAutoConfig.class,
        JpaAuditingAutoConfig.class,
        MongRedisConfig.class
})
class MongReadServiceTest {

    private final MongReadPort mongReadPort;

    private final GroupCodeRepository groupCodeRepository;

    private final ComnCodeRepository comnCodeRepository;

    private final MongStrokeHistoryRepository mongStrokeHistoryRepository;

    private final MongTypeRepository mongTypeRepository;

    private final MongRepository mongRepository;

    @Autowired
    public MongReadServiceTest(MongReadPort mongReadPort, GroupCodeRepository groupCodeRepository, ComnCodeRepository comnCodeRepository, MongStrokeHistoryRepository mongStrokeHistoryRepository, MongTypeRepository mongTypeRepository, MongRepository mongRepository) {
        this.mongReadPort = mongReadPort;
        this.groupCodeRepository = groupCodeRepository;
        this.comnCodeRepository = comnCodeRepository;
        this.mongStrokeHistoryRepository = mongStrokeHistoryRepository;
        this.mongTypeRepository = mongTypeRepository;
        this.mongRepository = mongRepository;
    }

    @Nested
    @DisplayName("몽 쓰다 듬기 대기 잔여 시간 조회 단위 테스트")
    class GetMongStrokeExpirationSecondsPort {

        private static final String MONG_STROKE_HISTORY_ID = CommonUtil.randomId();
        private static final Long MONG_ID = 1L;
        private static final Long EXPIRATION = 180L;

        @BeforeEach
        void beforeEach() {
            // arrange
            mongStrokeHistoryRepository.save(MongStrokeHistoryEntity.builder()
                    .mongStrokeHistoryId(MONG_STROKE_HISTORY_ID)
                    .mongId(MONG_ID)
                    .strokeAt(LocalDateTime.now())
                    .expiration(EXPIRATION)
                    .build());
        }

        @AfterEach
        void afterEach() {
            mongStrokeHistoryRepository.deleteById(MONG_STROKE_HISTORY_ID);
        }

        @Test
        @DisplayName("몽 쓰다 듬기 대기 잔여 시간을 조회 한다.")
        void getMongStrokeExpirationSeconds() {
            // act
            long expected = mongReadPort.getMongStrokeExpirationSecondsPort(MONG_ID);

            // assert
            assertTrue(0 < expected);
            assertTrue(EXPIRATION >= expected);
        }
    }

    @Nested
    @DisplayName("몽 타입 목록 조회 단위 테스트")
    class GetMongTypesPort {

        private static final GroupCodeEntity GROUP_CODE_ENTITY = new GroupCodeEntity("TEST-GROUP-CODE", "테스트 그룹 코드");
        private static final List<ComnCodeEntity> COMN_CODE_ENTITIES = List.of(
                new ComnCodeEntity("TEST-MONG-TYPE-CODE-0", "테스트 몽 타입 코드 0", GROUP_CODE_ENTITY),
                new ComnCodeEntity("TEST-MONG-TYPE-CODE-1", "테스트 몽 타입 코드 1", GROUP_CODE_ENTITY),
                new ComnCodeEntity("TEST-MONG-TYPE-CODE-2", "테스트 몽 타입 코드 2", GROUP_CODE_ENTITY),
                new ComnCodeEntity("TEST-MONG-TYPE-CODE-3", "테스트 몽 타입 코드 3", GROUP_CODE_ENTITY),
                new ComnCodeEntity("TEST-MONG-TYPE-CODE-4", "테스트 몽 타입 코드 4", GROUP_CODE_ENTITY),
                new ComnCodeEntity("TEST-MONG-TYPE-CODE-5", "테스트 몽 타입 코드 5", GROUP_CODE_ENTITY),
                new ComnCodeEntity("TEST-MONG-TYPE-CODE-6", "테스트 몽 타입 코드 6", GROUP_CODE_ENTITY),
                new ComnCodeEntity("TEST-MONG-TYPE-CODE-7", "테스트 몽 타입 코드 7", GROUP_CODE_ENTITY)
        );
        private static final List<String> GROUP_TYPES = List.of(
                "TEST-MONG-GROUP-TYPE-0",
                "TEST-MONG-GROUP-TYPE-0",
                "TEST-MONG-GROUP-TYPE-1",
                "TEST-MONG-GROUP-TYPE-1",
                "TEST-MONG-GROUP-TYPE-2",
                "TEST-MONG-GROUP-TYPE-2",
                "TEST-MONG-GROUP-TYPE-3",
                "TEST-MONG-GROUP-TYPE-3"
        );
        private static final List<String> NEXT_GROUP_TYPES = List.of(
                "TEST-MONG-GROUP-TYPE-1",
                "TEST-MONG-GROUP-TYPE-1",
                "TEST-MONG-GROUP-TYPE-2",
                "TEST-MONG-GROUP-TYPE-2",
                "TEST-MONG-GROUP-TYPE-3",
                "TEST-MONG-GROUP-TYPE-3",
                "",
                ""
        );
        private static final List<Integer> LEVELS = List.of(0, 0, 1, 1, 2, 2, 3, 3);
        private static final List<Double> EVOLUTION_SCORES = List.of(0D, 0D, 50D, 100D, 150D, 200D, 250D, 300D);

        @BeforeEach
        void beforeEach() {
            // arrange
            groupCodeRepository.saveAndFlush(GROUP_CODE_ENTITY);

            List<MongTypeEntity> mongTypeEntities = new ArrayList<>();

            for (int index = 0;  index < COMN_CODE_ENTITIES.size(); index++) {
                mongTypeEntities.add(MongTypeEntity.builder()
                        .comn(comnCodeRepository.saveAndFlush(COMN_CODE_ENTITIES.get(index)))
                        .level(LEVELS.get(index))
                        .evolutionScore(EVOLUTION_SCORES.get(index))
                        .maxStatus(Double.MAX_VALUE)
                        .groupType(GROUP_TYPES.get(index))
                        .nextGroupType(NEXT_GROUP_TYPES.get(index))
                        .build());
            }

            mongTypeRepository.saveAllAndFlush(mongTypeEntities);
        }

        @Test
        @DisplayName("몽 타입 레벨을 기준으로 몽 타입 목록을 조회 한다.")
        void getMongTypes() {
            // arrange
            int level = 0;

            // act
            var expected = mongReadPort.getMongTypesPort(level);

            // assert
            assertEquals(2, expected.size());
            assertEquals(level, expected.get(0).getLevel());
            assertEquals(level, expected.get(1).getLevel());
        }

        @Test
        @DisplayName("다음 레벨 몽 타입 목록을 조회 한다.")
        void getNextLevelMongTypes() {
            // arrange
            double evolutionScore = 50D;
            String mongTypeCode = "TEST-MONG-TYPE-CODE-0";

            // act
            var expected = mongReadPort.getNextLevelMongTypesPort(evolutionScore, mongTypeCode);

            // assert
            assertEquals(1, expected.size());
            assertTrue(evolutionScore >= expected.get(0).getEvolutionScore());
        }

        @Test
        @DisplayName("다음 레벨 몽 타입 목록을 진화 점수 기준으로 오름차순 정렬 조회 한다.")
        void getNextLevelMongTypesWhenMutilMongTypes() {
            // arrange
            double evolutionScore = 100D;
            String mongTypeCode = "TEST-MONG-TYPE-CODE-0";

            // act
            var expected = mongReadPort.getNextLevelMongTypesPort(evolutionScore, mongTypeCode);

            // assert
            assertEquals(2, expected.size());
            assertTrue(evolutionScore >= expected.get(0).getEvolutionScore());
            assertTrue(evolutionScore >= expected.get(1).getEvolutionScore());
            assertEquals(COMN_CODE_ENTITIES.get(2).getCode(), expected.get(0).getMongTypeCode());
            assertEquals(COMN_CODE_ENTITIES.get(3).getCode(), expected.get(1).getMongTypeCode());
            assertEquals(COMN_CODE_ENTITIES.get(2).getName(), expected.get(0).getMongTypeName());
            assertEquals(COMN_CODE_ENTITIES.get(3).getName(), expected.get(1).getMongTypeName());
        }
    }

    @Nested
    @DisplayName("몽 조회 단위 테스트")
    class GetMongPort {
        // TODO: 단위 테스트 작성
    }

    @Nested
    @DisplayName("몽 목록 조회 단위 테스트")
    class GetMongsPort {
        // TODO: 단위 테스트 작성
    }

    @Nested
    @DisplayName("인벤 아이템 목록 조회 단위 테스트")
    class GetInventoryItemsPort {
        // TODO: 단위 테스트 작성
    }

    @Nested
    @DisplayName("음식 조회 단위 테스트")
    class GetFoodPort {
        // TODO: 단위 테스트 작성
    }

    @Nested
    @DisplayName("음식 목록 조회 단위 테스트")
    class GetFoodsPort {
        // TODO: 단위 테스트 작성
    }

    @Nested
    @DisplayName("간식 조회 단위 테스트")
    class GetSnackPort {
        // TODO: 단위 테스트 작성
    }

    @Nested
    @DisplayName("간식 목록 조회 단위 테스트")
    class GetSnacksPort {
        // TODO: 단위 테스트 작성
    }

    @Nested
    @DisplayName("훈련 타입 목록 조회 단위 테스트")
    class GetTrainingTypesPort {
        // TODO: 단위 테스트 작성
    }

    @Nested
    @DisplayName("훈련 타입 조회 단위 테스트")
    class GetTrainingTypePort {
        // TODO: 단위 테스트 작성
    }

    @Nested
    @DisplayName("랜덤 뽑기 아이템 목록 조회 단위 테스트")
    class GetRandomDrawItemsPort {
        // TODO: 단위 테스트 작성
    }
}