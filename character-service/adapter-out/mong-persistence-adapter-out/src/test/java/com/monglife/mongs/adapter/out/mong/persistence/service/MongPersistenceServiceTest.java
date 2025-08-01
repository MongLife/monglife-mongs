package com.monglife.mongs.adapter.out.mong.persistence.service;

import com.monglife.module.common.jpa.config.HibernateAutoConfig;
import com.monglife.module.common.jpa.config.JpaAuditingAutoConfig;
import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import com.monglife.module.common.jpa.entity.GroupCodeEntity;
import com.monglife.mongs.adapter.out.mong.persistence.config.AdapterOutMongPersistenceConfig;
import com.monglife.mongs.adapter.out.mong.persistence.config.MongDataSourceConfig;
import com.monglife.mongs.adapter.out.mong.persistence.config.MongRedisConfig;
import com.monglife.mongs.adapter.out.mong.persistence.entity.FoodEntity;
import com.monglife.mongs.adapter.out.mong.persistence.entity.InventoryEntity;
import com.monglife.mongs.adapter.out.mong.persistence.entity.MongEntity;
import com.monglife.mongs.adapter.out.mong.persistence.entity.MongTypeEntity;
import com.monglife.mongs.adapter.out.mong.persistence.repository.*;
import com.monglife.mongs.application.mong.port.out.MongPersistencePort;
import com.monglife.mongs.application.mong.port.out.vo.CreateInventoryVo;
import com.monglife.mongs.application.mong.port.out.vo.CreateMongVo;
import com.monglife.mongs.domain.mong.enums.InventoryTypeCode;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import com.monglife.mongs.domain.mong.model.Mong;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

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
class MongPersistenceServiceTest {

    private final MongPersistencePort mongPersistencePort;
    private final GroupCodeRepository groupCodeRepository;
    private final ComnCodeRepository comnCodeRepository;
    private final MongStrokeHistoryRepository mongStrokeHistoryRepository;
    private final MongTypeRepository mongTypeRepository;
    private final MongRepository mongRepository;
    private final FoodRepository foodRepository;
    private final InventoryRepository inventoryRepository;

    @Autowired
    public MongPersistenceServiceTest(MongPersistencePort mongPersistencePort, GroupCodeRepository groupCodeRepository, ComnCodeRepository comnCodeRepository, MongStrokeHistoryRepository mongStrokeHistoryRepository, MongTypeRepository mongTypeRepository, MongRepository mongRepository, FoodRepository foodRepository, InventoryRepository inventoryRepository) {
        this.mongPersistencePort = mongPersistencePort;
        this.groupCodeRepository = groupCodeRepository;
        this.comnCodeRepository = comnCodeRepository;
        this.mongStrokeHistoryRepository = mongStrokeHistoryRepository;
        this.mongTypeRepository = mongTypeRepository;
        this.mongRepository = mongRepository;
        this.foodRepository = foodRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Nested
    @DisplayName("몽 쓰다 듬기 이력 등록 단위 테스트")
    class CreateMongStrokeHistoryPort {

        private static final Long MONG_ID = 1L;

        @AfterEach
        void afterEach() {
            mongStrokeHistoryRepository.findByMongId(MONG_ID).ifPresent(mongStrokeHistoryEntity -> {
                mongStrokeHistoryRepository.deleteById(mongStrokeHistoryEntity.getMongStrokeHistoryId());
            });
        }

        @Test
        @DisplayName("몽 쓰다 듬기 이력을 등록 한다.")
        void createMongStrokeHistory() {
            // arrange
            final long mongId = 1L;
            final long expirationSeconds = 5L;

            // act
            var expected1 = mongPersistencePort.createMongStrokeHistoryPort(mongId, expirationSeconds);
            var expected2 = mongStrokeHistoryRepository.findByMongId(mongId);

            // assert
            assertFalse(expected1.isEmpty());
            assertFalse(expected2.isEmpty());
        }

        @Test
        @DisplayName("몽 쓰다 듬기 이력을 등록 후 3초 뒤 사라진다.")
        void createMongStrokeHistoryDeleteAfterFewSeconds() {
            // arrange
            final long mongId = 1L;
            final long expirationSeconds = 3L;

            // act
            var expected = mongPersistencePort.createMongStrokeHistoryPort(mongId, expirationSeconds);

            // assert
            assertFalse(expected.isEmpty());

            Awaitility.await()
                    .atMost(Duration.ofSeconds(expirationSeconds * 2))
                    .untilAsserted(() -> assertTrue(mongStrokeHistoryRepository.findByMongId(mongId).isEmpty()));
        }
    }

    @Nested
    @DisplayName("몽 음식 섭취 이력 등록 단위 테스트")
    class CreateMongFeedFoodHistoryPort {
        // TODO: 단위 테스트 작성
    }

    @Nested
    @DisplayName("몽 음식 섭취 이력 등록 단위 테스트")
    class CreateMongFeedSnackHistoryPort {
        // TODO: 단위 테스트 작성
    }

    @Nested
    @DisplayName("몽 등록 단위 테스트")
    class CreateMongPort {

        private static final Long ACCOUNT_ID = 1L;
        private static final String NAME = "TEST-MONG-NAME";
        private static final LocalTime SLEEP_AT = LocalTime.of(22, 0);
        private static final LocalTime WAKEUP_AT = LocalTime.of(8, 0);
        private static final GroupCodeEntity GROUP_CODE_ENTITY = new GroupCodeEntity("TEST-GROUP-CODE", "테스트 그룹 코드");
        private static final ComnCodeEntity COMN_CODE_ENTITY = new ComnCodeEntity("TEST-MONG-TYPE-CODE", "테스트 몽 타입 코드", GROUP_CODE_ENTITY);
        private static final MongTypeEntity MONG_TYPE_ENTITY = new MongTypeEntity(null, COMN_CODE_ENTITY, 1, 100D, 100D, "TEST-GROUP-TYPE");

        @Test
        @DisplayName("몽을 등록 한다.")
        void createMong() {
            // arrange
            groupCodeRepository.saveAndFlush(GROUP_CODE_ENTITY);
            comnCodeRepository.saveAndFlush(COMN_CODE_ENTITY);
            mongTypeRepository.saveAndFlush(MONG_TYPE_ENTITY);

            final CreateMongVo createMongVo = CreateMongVo.builder()
                    .accountId(ACCOUNT_ID)
                    .name(NAME)
                    .statusCode(MongStatusCode.NORMAL)
                    .stateCode(MongStateCode.NORMAL)
                    .sleepAt(SLEEP_AT)
                    .wakeupAt(WAKEUP_AT)
                    .payPoint(0)
                    .isSleep(false)
                    .strength(MONG_TYPE_ENTITY.toDomain().getMaxStatus())
                    .satiety(MONG_TYPE_ENTITY.toDomain().getMaxStatus())
                    .healthy(MONG_TYPE_ENTITY.toDomain().getMaxStatus())
                    .fatigue(MONG_TYPE_ENTITY.toDomain().getMaxStatus())
                    .exp(0D)
                    .weight(0D)
                    .evolutionReward(0D)
                    .evolutionPenalty(0D)
                    .strokeCount(0)
                    .trainingCount(0)
                    .poopCount(0)
                    .randomDrawTicketCount(0)
                    .mongType(MONG_TYPE_ENTITY.toDomain())
                    .build();

            // act
            var expected1 = mongPersistencePort.createMongPort(createMongVo);
            var expected2 = mongRepository.findById(expected1.isEmpty() ? -1L : expected1.get().getMongId());

            // assert
            assertFalse(expected1.isEmpty());
            assertEquals(ACCOUNT_ID, expected1.get().getAccountId());
            assertEquals(NAME, expected1.get().getName());
            assertEquals(MONG_TYPE_ENTITY.getComn().getCode(), expected1.get().getMongCode());
            assertEquals(MONG_TYPE_ENTITY.getComn().getName(), expected1.get().getMongName());
            assertEquals(MongStatusCode.NORMAL, expected1.get().getStatusCode());
            assertEquals(MongStateCode.NORMAL, expected1.get().getStateCode());
            assertEquals(MONG_TYPE_ENTITY.getLevel(), expected1.get().getLevel());
            assertEquals(MONG_TYPE_ENTITY.getMaxStatus(), expected1.get().getMaxStatus());
            assertEquals(SLEEP_AT, expected1.get().getSleepAt());
            assertEquals(WAKEUP_AT, expected1.get().getWakeupAt());
            assertEquals(0, expected1.get().getPayPoint());
            assertEquals(false, expected1.get().getIsSleep());
            assertEquals(MONG_TYPE_ENTITY.getMaxStatus(), expected1.get().getStrength());
            assertEquals(MONG_TYPE_ENTITY.getMaxStatus(), expected1.get().getSatiety());
            assertEquals(MONG_TYPE_ENTITY.getMaxStatus(), expected1.get().getHealthy());
            assertEquals(MONG_TYPE_ENTITY.getMaxStatus(), expected1.get().getFatigue());
            assertEquals(0D, expected1.get().getExp());
            assertEquals(0D, expected1.get().getWeight());
            assertEquals(0D, expected1.get().getEvolutionReward());
            assertEquals(0D, expected1.get().getEvolutionPenalty());
            assertEquals(0, expected1.get().getStrokeCount());
            assertEquals(0, expected1.get().getTrainingCount());
            assertEquals(0, expected1.get().getPoopCount());
            assertEquals(0, expected1.get().getRandomDrawTicketCount());
            assertNotNull(expected1.get().getCreatedAt());

            assertFalse(expected2.isEmpty());
            assertEquals(ACCOUNT_ID, expected2.get().getAccountId());
            assertEquals(NAME, expected2.get().getName());
            assertEquals(SLEEP_AT, expected2.get().getSleepAt());
            assertEquals(WAKEUP_AT, expected2.get().getWakeupAt());
            assertEquals(0, expected2.get().getPayPoint());
            assertEquals(MONG_TYPE_ENTITY, expected2.get().getMongType());
            assertEquals(MongStateCode.NORMAL, expected2.get().getStateCode());
            assertEquals(false, expected2.get().getIsSleep());
            assertEquals(MONG_TYPE_ENTITY.getMaxStatus(), expected2.get().getMaxStatus());
            assertEquals(MongStatusCode.NORMAL, expected2.get().getStatusCode());
            assertEquals(0D, expected2.get().getWeight());
            assertEquals(0, expected2.get().getPoopCount());
            assertEquals(0D, expected2.get().getExp());
            assertEquals(MONG_TYPE_ENTITY.getMaxStatus(), expected2.get().getStrength());
            assertEquals(MONG_TYPE_ENTITY.getMaxStatus(), expected2.get().getSatiety());
            assertEquals(MONG_TYPE_ENTITY.getMaxStatus(), expected2.get().getHealthy());
            assertEquals(MONG_TYPE_ENTITY.getMaxStatus(), expected2.get().getFatigue());
            assertEquals(0, expected2.get().getTrainingCount());
            assertEquals(0, expected2.get().getStrokeCount());
            assertEquals(0, expected2.get().getRandomDrawTicketCount());
            assertEquals(0D, expected2.get().getEvolutionReward());
            assertEquals(0D, expected2.get().getEvolutionPenalty());
        }

        @Test
        @DisplayName("몽을 등록할 때, 몽 타입 코드가 존재하지 않는 경우 빈 옵셔널 객체를 반환 한다.")
        void createMongWhenNotExistsMongCode() {
            // arrange
            final CreateMongVo createMongVo = CreateMongVo.builder()
                    .accountId(ACCOUNT_ID)
                    .name(NAME)
                    .statusCode(MongStatusCode.NORMAL)
                    .stateCode(MongStateCode.NORMAL)
                    .sleepAt(SLEEP_AT)
                    .wakeupAt(WAKEUP_AT)
                    .payPoint(0)
                    .isSleep(false)
                    .strength(MONG_TYPE_ENTITY.toDomain().getMaxStatus())
                    .satiety(MONG_TYPE_ENTITY.toDomain().getMaxStatus())
                    .healthy(MONG_TYPE_ENTITY.toDomain().getMaxStatus())
                    .fatigue(MONG_TYPE_ENTITY.toDomain().getMaxStatus())
                    .exp(0D)
                    .weight(0D)
                    .evolutionReward(0D)
                    .evolutionPenalty(0D)
                    .strokeCount(0)
                    .trainingCount(0)
                    .poopCount(0)
                    .randomDrawTicketCount(0)
                    .mongType(MONG_TYPE_ENTITY.toDomain())
                    .build();

            // act
            var expected1 = mongPersistencePort.createMongPort(createMongVo);

            // assert
            assertTrue(expected1.isEmpty());
        }
    }

    @Nested
    @DisplayName("몽 조회 단위 테스트")
    class GetMongPort {

        private static final Long ACCOUNT_ID = 1L;
        private static final String NAME = "TEST-MONG-NAME";
        private static final LocalTime SLEEP_AT = LocalTime.of(22, 0);
        private static final LocalTime WAKEUP_AT = LocalTime.of(8, 0);
        private static final GroupCodeEntity GROUP_CODE_ENTITY = new GroupCodeEntity("TEST-GROUP-CODE", "테스트 그룹 코드");
        private static final ComnCodeEntity COMN_CODE_ENTITY = new ComnCodeEntity("TEST-MONG-TYPE-CODE", "테스트 몽 타입 코드", GROUP_CODE_ENTITY);
        private static final MongTypeEntity MONG_TYPE_ENTITY = new MongTypeEntity(null, COMN_CODE_ENTITY, 1, 100D, 100D, "TEST-GROUP-TYPE");

        @Test
        @DisplayName("몽을 조회 한다.")
        void getMong() {
            // arrange
            groupCodeRepository.saveAndFlush(GROUP_CODE_ENTITY);
            comnCodeRepository.saveAndFlush(COMN_CODE_ENTITY);
            mongTypeRepository.saveAndFlush(MONG_TYPE_ENTITY);

            final MongEntity mongEntity = mongRepository.save(MongEntity.builder()
                    .accountId(ACCOUNT_ID)
                    .name(NAME)
                    .sleepAt(SLEEP_AT)
                    .wakeupAt(WAKEUP_AT)
                    .payPoint(0)
                    .mongType(MONG_TYPE_ENTITY)
                    .stateCode(MongStateCode.NORMAL)
                    .isSleep(false)
                    .maxStatus(MONG_TYPE_ENTITY.getMaxStatus())
                    .statusCode(MongStatusCode.NORMAL)
                    .weight(0D)
                    .poopCount(0)
                    .exp(0D)
                    .strength(MONG_TYPE_ENTITY.getMaxStatus())
                    .satiety(MONG_TYPE_ENTITY.getMaxStatus())
                    .healthy(MONG_TYPE_ENTITY.getMaxStatus())
                    .fatigue(MONG_TYPE_ENTITY.getMaxStatus())
                    .trainingCount(0)
                    .strokeCount(0)
                    .evolutionReward(0D)
                    .evolutionPenalty(0D)
                    .randomDrawTicketCount(0)
                    .build());

            // act
            var expected1 = mongPersistencePort.getMongPort(mongEntity.getMongId());
            var expected2 = mongPersistencePort.getMongPort(mongEntity.getMongId());

            // assert
            assertFalse(expected1.isEmpty());
            assertFalse(expected2.isEmpty());
            assertEquals(mongEntity.getMongId(), expected1.get().getMongId());
            assertEquals(mongEntity.getMongId(), expected2.get().getMongId());
        }

        @Test
        @DisplayName("몽이 없는 경우 빈 옵셔널 객체를 반환 한다.")
        void getMongWhenNotExistsMong() {
            // act
            var expected = mongPersistencePort.getMongPort(0L);

            // assert
            assertTrue(expected.isEmpty());
        }
    }

    @Nested
    @DisplayName("몽 동기화 단위 테스트")
    class SaveMongPort {

        private static final Long ACCOUNT_ID = 1L;
        private static final String NAME = "TEST-MONG-NAME";
        private static final LocalTime SLEEP_AT = LocalTime.of(22, 0);
        private static final LocalTime WAKEUP_AT = LocalTime.of(8, 0);
        private static final GroupCodeEntity GROUP_CODE_ENTITY = new GroupCodeEntity("TEST-GROUP-CODE", "테스트 그룹 코드");
        private static final ComnCodeEntity COMN_CODE_ENTITY = new ComnCodeEntity("TEST-MONG-TYPE-CODE", "테스트 몽 타입 코드", GROUP_CODE_ENTITY);
        private static final MongTypeEntity MONG_TYPE_ENTITY = new MongTypeEntity(null, COMN_CODE_ENTITY, 1, 100D, 100D, "TEST-GROUP-TYPE");


        @Test
        @DisplayName("몽을 동기화 한다.")
        void saveMong() {
            // arrange
            groupCodeRepository.saveAndFlush(GROUP_CODE_ENTITY);
            comnCodeRepository.saveAndFlush(COMN_CODE_ENTITY);
            mongTypeRepository.saveAndFlush(MONG_TYPE_ENTITY);

            final MongEntity mongEntity = mongRepository.save(MongEntity.builder()
                    .accountId(ACCOUNT_ID)
                    .name(NAME)
                    .sleepAt(SLEEP_AT)
                    .wakeupAt(WAKEUP_AT)
                    .payPoint(0)
                    .mongType(MONG_TYPE_ENTITY)
                    .stateCode(MongStateCode.NORMAL)
                    .isSleep(false)
                    .maxStatus(MONG_TYPE_ENTITY.getMaxStatus())
                    .statusCode(MongStatusCode.NORMAL)
                    .weight(0D)
                    .poopCount(4)
                    .exp(0D)
                    .strength(MONG_TYPE_ENTITY.getMaxStatus())
                    .satiety(MONG_TYPE_ENTITY.getMaxStatus())
                    .healthy(MONG_TYPE_ENTITY.getMaxStatus())
                    .fatigue(MONG_TYPE_ENTITY.getMaxStatus())
                    .trainingCount(0)
                    .strokeCount(0)
                    .evolutionReward(0D)
                    .evolutionPenalty(0D)
                    .randomDrawTicketCount(0)
                    .build());

            final Mong mong = MongEntity.builder()
                    .mongId(mongEntity.getMongId())
                    .accountId(ACCOUNT_ID)
                    .name(NAME)
                    .sleepAt(SLEEP_AT)
                    .wakeupAt(WAKEUP_AT)
                    .payPoint(0)
                    .mongType(MONG_TYPE_ENTITY)
                    .stateCode(MongStateCode.NORMAL)
                    .isSleep(true)
                    .maxStatus(MONG_TYPE_ENTITY.getMaxStatus())
                    .statusCode(MongStatusCode.NORMAL)
                    .weight(0D)
                    .poopCount(0)
                    .exp(0D)
                    .strength(MONG_TYPE_ENTITY.getMaxStatus())
                    .satiety(MONG_TYPE_ENTITY.getMaxStatus())
                    .healthy(MONG_TYPE_ENTITY.getMaxStatus())
                    .fatigue(MONG_TYPE_ENTITY.getMaxStatus())
                    .trainingCount(0)
                    .strokeCount(0)
                    .evolutionReward(0D)
                    .evolutionPenalty(0D)
                    .randomDrawTicketCount(0)
                    .build()
                    .toDomain();

            // act
            var expected = mongPersistencePort.saveMongPort(mong);

            // assert
            assertFalse(expected.isEmpty());
            assertTrue(expected.get().getIsSleep());
            assertEquals(0, expected.get().getPoopCount());
        }
    }

    @Nested
    @DisplayName("몽 삭제 단위 테스트")
    class DeleteMongPort {

        private static final Long ACCOUNT_ID = 1L;
        private static final String NAME = "TEST-MONG-NAME";
        private static final LocalTime SLEEP_AT = LocalTime.of(22, 0);
        private static final LocalTime WAKEUP_AT = LocalTime.of(8, 0);
        private static final GroupCodeEntity GROUP_CODE_ENTITY = new GroupCodeEntity("TEST-GROUP-CODE", "테스트 그룹 코드");
        private static final ComnCodeEntity COMN_CODE_ENTITY = new ComnCodeEntity("TEST-MONG-TYPE-CODE", "테스트 몽 타입 코드", GROUP_CODE_ENTITY);
        private static final MongTypeEntity MONG_TYPE_ENTITY = new MongTypeEntity(null, COMN_CODE_ENTITY, 1, 100D, 100D, "TEST-GROUP-TYPE");

        @Test
        @DisplayName("몽을 삭제 한다.")
        void deleteMong() {
            // arrange
            groupCodeRepository.saveAndFlush(GROUP_CODE_ENTITY);
            comnCodeRepository.saveAndFlush(COMN_CODE_ENTITY);
            mongTypeRepository.saveAndFlush(MONG_TYPE_ENTITY);

            final MongEntity mongEntity = mongRepository.save(MongEntity.builder()
                    .accountId(ACCOUNT_ID)
                    .name(NAME)
                    .sleepAt(SLEEP_AT)
                    .wakeupAt(WAKEUP_AT)
                    .payPoint(0)
                    .mongType(MONG_TYPE_ENTITY)
                    .stateCode(MongStateCode.NORMAL)
                    .isSleep(false)
                    .maxStatus(MONG_TYPE_ENTITY.getMaxStatus())
                    .statusCode(MongStatusCode.NORMAL)
                    .weight(0D)
                    .poopCount(4)
                    .exp(0D)
                    .strength(MONG_TYPE_ENTITY.getMaxStatus())
                    .satiety(MONG_TYPE_ENTITY.getMaxStatus())
                    .healthy(MONG_TYPE_ENTITY.getMaxStatus())
                    .fatigue(MONG_TYPE_ENTITY.getMaxStatus())
                    .trainingCount(0)
                    .strokeCount(0)
                    .evolutionReward(0D)
                    .evolutionPenalty(0D)
                    .randomDrawTicketCount(0)
                    .build());

            final Mong mong = MongEntity.builder()
                    .mongId(mongEntity.getMongId())
                    .accountId(ACCOUNT_ID)
                    .name(NAME)
                    .sleepAt(SLEEP_AT)
                    .wakeupAt(WAKEUP_AT)
                    .payPoint(0)
                    .mongType(MONG_TYPE_ENTITY)
                    .stateCode(MongStateCode.NORMAL)
                    .isSleep(true)
                    .maxStatus(MONG_TYPE_ENTITY.getMaxStatus())
                    .statusCode(MongStatusCode.NORMAL)
                    .weight(0D)
                    .poopCount(0)
                    .exp(0D)
                    .strength(MONG_TYPE_ENTITY.getMaxStatus())
                    .satiety(MONG_TYPE_ENTITY.getMaxStatus())
                    .healthy(MONG_TYPE_ENTITY.getMaxStatus())
                    .fatigue(MONG_TYPE_ENTITY.getMaxStatus())
                    .trainingCount(0)
                    .strokeCount(0)
                    .evolutionReward(0D)
                    .evolutionPenalty(0D)
                    .randomDrawTicketCount(0)
                    .build()
                    .toDomain();

            // act
            var expected1 = mongPersistencePort.deleteMongPort(mong);
            var expected2 = mongRepository.findById(mongEntity.getMongId());

            // assert
            assertFalse(expected1.isEmpty());
            assertTrue(expected2.isEmpty());
        }

    }

    @Nested
    @DisplayName("인벤 아이템 등록 단위 테스트")
    class CreateInventoryPort {

        private static final Long MONG_ID = 1L;
        private static final GroupCodeEntity GROUP_CODE_ENTITY = new GroupCodeEntity("TEST-GROUP-CODE", "테스트 그룹 코드");
        private static final ComnCodeEntity COMN_CODE_ENTITY = new ComnCodeEntity("TEST-FOOD-TYPE-CODE", "테스트 음식 타입 코드", GROUP_CODE_ENTITY);

        @Test
        @DisplayName("인벤 아이템을 등록 한다.")
        void createInventoryItem() {
            // arrange
            groupCodeRepository.saveAndFlush(GROUP_CODE_ENTITY);
            comnCodeRepository.saveAndFlush(COMN_CODE_ENTITY);

            final CreateInventoryVo createInventoryVo = CreateInventoryVo.builder()
                    .mongId(MONG_ID)
                    .inventoryCode(COMN_CODE_ENTITY.getCode())
                    .inventoryTypeCode(InventoryTypeCode.FOOD)
                    .build();

            // act
            var expected = mongPersistencePort.createInventoryPort(createInventoryVo);

            // assert
            assertFalse(expected.isEmpty());
            assertEquals(COMN_CODE_ENTITY.getCode(), expected.get().getInventoryCode());
            assertEquals(COMN_CODE_ENTITY.getName(), expected.get().getInventoryName());
        }

    }

    @Nested
    @DisplayName("인벤 아이템 삭제 단위 테스트")
    class DeleteInventoryPort {

        private static final Long MONG_ID = 1L;
        private static final GroupCodeEntity GROUP_CODE_ENTITY = new GroupCodeEntity("TEST-GROUP-CODE", "테스트 그룹 코드");
        private static final ComnCodeEntity COMN_CODE_ENTITY = new ComnCodeEntity("TEST-FOOD-TYPE-CODE", "테스트 음식 타입 코드", GROUP_CODE_ENTITY);
        private static final FoodEntity FOOD_TYPE_ENTITY = new FoodEntity(null, COMN_CODE_ENTITY, 100, 100D, 100D, 100D, 100D, 100D, 5);

        @Test
        @DisplayName("인벤 아이템을 삭제 한다.")
        void deleteInventoryItem() {
            // arrange
            groupCodeRepository.saveAndFlush(GROUP_CODE_ENTITY);
            comnCodeRepository.saveAndFlush(COMN_CODE_ENTITY);
            foodRepository.saveAndFlush(FOOD_TYPE_ENTITY);

            final InventoryEntity inventoryEntity = inventoryRepository.saveAndFlush(InventoryEntity.builder()
                    .mongId(MONG_ID)
                    .comn(COMN_CODE_ENTITY)
                    .inventoryTypeCode(InventoryTypeCode.FOOD)
                    .build());

            // act
            var expected1 = mongPersistencePort.deleteInventoryPort(inventoryEntity.getInventoryId());
            var expected2 = mongRepository.findById(inventoryEntity.getInventoryId());

            // assert
            assertFalse(expected1.isEmpty());
            assertTrue(expected2.isEmpty());
        }
    }

    @Nested
    @DisplayName("인벤 아이템 조회 단위 테스트")
    class GetInventoryPort {

        private static final Long MONG_ID = 1L;
        private static final GroupCodeEntity GROUP_CODE_ENTITY = new GroupCodeEntity("TEST-GROUP-CODE", "테스트 그룹 코드");
        private static final ComnCodeEntity COMN_CODE_ENTITY = new ComnCodeEntity("TEST-FOOD-TYPE-CODE", "테스트 음식 타입 코드", GROUP_CODE_ENTITY);
        private static final FoodEntity FOOD_TYPE_ENTITY = new FoodEntity(null, COMN_CODE_ENTITY, 100, 100D, 100D, 100D, 100D, 100D, 5);

        @Test
        @DisplayName("인벤 아이템을 조회 한다.")
        void getInventoryItem() {
            // arrange
            groupCodeRepository.saveAndFlush(GROUP_CODE_ENTITY);
            comnCodeRepository.saveAndFlush(COMN_CODE_ENTITY);
            foodRepository.saveAndFlush(FOOD_TYPE_ENTITY);

            final InventoryEntity inventoryEntity = inventoryRepository.saveAndFlush(InventoryEntity.builder()
                    .mongId(MONG_ID)
                    .comn(COMN_CODE_ENTITY)
                    .inventoryTypeCode(InventoryTypeCode.FOOD)
                    .build());

            // act
            var expected = mongPersistencePort.getInventoryPort(inventoryEntity.getInventoryId());

            // assert
            assertFalse(expected.isEmpty());
            assertEquals(COMN_CODE_ENTITY.getCode(), expected.get().getInventoryCode());
            assertEquals(COMN_CODE_ENTITY.getName(), expected.get().getInventoryName());
        }

    }
}