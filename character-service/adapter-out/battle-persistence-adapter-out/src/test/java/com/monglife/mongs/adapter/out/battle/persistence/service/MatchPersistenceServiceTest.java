package com.monglife.mongs.adapter.out.battle.persistence.service;

import com.monglife.core.utils.CommonUtil;
import com.monglife.module.common.jpa.config.HibernateAutoConfig;
import com.monglife.module.common.jpa.config.JpaAuditingAutoConfig;
import com.monglife.mongs.adapter.out.battle.persistence.config.AdapterOutBattlePersistenceConfig;
import com.monglife.mongs.adapter.out.battle.persistence.config.BattleDataSourceConfig;
import com.monglife.mongs.adapter.out.battle.persistence.config.BattleRedisConfig;
import com.monglife.mongs.adapter.out.battle.persistence.entity.QueuePlayerEntity;
import com.monglife.mongs.adapter.out.battle.persistence.repository.MatchRepository;
import com.monglife.mongs.adapter.out.battle.persistence.repository.QueuePlayerRepository;
import com.monglife.mongs.application.battle.port.out.MatchPersistencePort;
import com.monglife.mongs.application.battle.port.out.vo.CreateQueuePlayerVo;
import com.monglife.mongs.domain.battle.model.QueuePlayer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {
        AdapterOutBattlePersistenceConfig.class,
        BattleDataSourceConfig.class,
        HibernateAutoConfig.class,
        JpaAuditingAutoConfig.class,
        BattleRedisConfig.class
})
class MatchPersistenceServiceTest {

    private final MatchPersistencePort matchPersistencePort;

    private final MatchRepository matchRepository;

    private final QueuePlayerRepository queuePlayerRepository;

    @Autowired
    public MatchPersistenceServiceTest(MatchPersistencePort matchPersistencePort, MatchRepository matchRepository, QueuePlayerRepository queuePlayerRepository) {
        this.matchPersistencePort = matchPersistencePort;
        this.matchRepository = matchRepository;
        this.queuePlayerRepository = queuePlayerRepository;
    }

    @Nested
    @DisplayName("매치 대기열 조회 단위 테스트")
    class GetQueuePlayerPort {

        private static final Long MONG_ID = 1L;
        private static final String DEVICE_ID = CommonUtil.randomId();
        private static final Long ACCOUNT_ID = 1L;

        @AfterEach
        void afterEach() {
            queuePlayerRepository.deleteAll();
        }

        @Test
        @DisplayName("매치 대기열을 조회 한다.")
        void getQueuePlayer() {
            // arrange
            QueuePlayerEntity queuePlayerEntity = QueuePlayerEntity.builder()
                    .mongId(MONG_ID)
                    .deviceId(DEVICE_ID)
                    .accountId(ACCOUNT_ID)
                    .createdAt(LocalDateTime.now())
                    .build();

            queuePlayerRepository.save(queuePlayerEntity);

            // act
            var expected = matchPersistencePort.getQueuePlayerPort(MONG_ID, ACCOUNT_ID, DEVICE_ID);

            // assert
            assertFalse(expected.isEmpty());
            assertEquals(MONG_ID, expected.get().getMongId());
            assertEquals(DEVICE_ID, expected.get().getDeviceId());
            assertEquals(ACCOUNT_ID, expected.get().getAccountId());
        }


        @Test
        @DisplayName("매치 대기열이 비어 있는 경우 빈 옵셔널 객체를 반환 한다.")
        void getQueuePlayerWhenNotExists() {
            // arrange
            QueuePlayerEntity queuePlayerEntity = QueuePlayerEntity.builder()
                    .mongId(0L)
                    .deviceId(CommonUtil.randomId())
                    .accountId(0L)
                    .createdAt(LocalDateTime.now())
                    .build();

            queuePlayerRepository.save(queuePlayerEntity);

            // act
            var expected = matchPersistencePort.getQueuePlayerPort(MONG_ID, ACCOUNT_ID, DEVICE_ID);

            // assert
            assertTrue(expected.isEmpty());
        }
    }

    @Nested
    @DisplayName("매치 대기열 목록 조회 단위 테스트")
    class GetQueuePlayersPort {

        @AfterEach
        void afterEach() {
            queuePlayerRepository.deleteAll();
        }

        @Test
        @DisplayName("매치 대기열 목록을 조회 한다.")
        void getQueuePlayers() {
            // arrange
            long queuePlayerCount = 10;
            int matchPlayerCount = 2;
            long expiredSeconds = 5L;
            LocalDateTime now = LocalDateTime.now();
            List<QueuePlayerEntity> queuePlayerEntities = new ArrayList<>();

            for (long index = 0; index < queuePlayerCount; index++) {
                queuePlayerEntities.add(queuePlayerRepository.save(QueuePlayerEntity.builder()
                        .mongId(index)
                        .deviceId(CommonUtil.randomId())
                        .accountId(index)
                        .createdAt(now.plusSeconds(index))
                        .build()));
            }

            // act
            var expected1 = matchPersistencePort.getQueuePlayersPort(matchPlayerCount, expiredSeconds);
            var expected2 = queuePlayerRepository.findAll();

            // assert
            assertEquals(2, expected1.size());
            assertEquals(queuePlayerEntities.get(0).getDeviceId(), expected1.get(0).getDeviceId());
            assertEquals(queuePlayerEntities.get(1).getDeviceId(), expected1.get(1).getDeviceId());
            assertEquals(queuePlayerCount - matchPlayerCount, expected2.size());
        }

        @Test
        @DisplayName("대기 시간이 지난 플레이어는 한명이라도 목록에 포함 시킨다.")
        void getQueuePlayersWhenIsAfterExpiredSeconds() {
            // arrange
            long mongId = 1L;
            String deviceId = CommonUtil.randomId();
            long accountId = 1L;
            int matchPlayerCount = 2;
            long expiredSeconds = 5L;
            LocalDateTime now = LocalDateTime.now();

            QueuePlayerEntity queuePlayerEntity = QueuePlayerEntity.builder()
                    .mongId(mongId)
                    .deviceId(deviceId)
                    .accountId(accountId)
                    .createdAt(now.minusSeconds(expiredSeconds))
                    .build();

            queuePlayerRepository.save(queuePlayerEntity);

            // act
            var expected1 = matchPersistencePort.getQueuePlayersPort(matchPlayerCount, expiredSeconds);
            var expected2 = queuePlayerRepository.findAll();

            // assert
            assertEquals(1, expected1.size());
            assertEquals(queuePlayerEntity.getDeviceId(), expected1.get(0).getDeviceId());
            assertEquals(0, expected2.size());
        }
    }

    @Nested
    @DisplayName("매치 대기열 등록 단위 테스트")
    class CreateQueuePlayerPort {

        @AfterEach
        void afterEach() {
            queuePlayerRepository.deleteAll();
        }

        @Test
        @DisplayName("매치 대기열을 등록 한다.")
        void createQueuePlayer() {
            // arrange
            long mongId = 1L;
            String deviceId = CommonUtil.randomId();
            long accountId = 1L;

            // act
            CreateQueuePlayerVo createQueuePlayerVo = CreateQueuePlayerVo.builder()
                    .mongId(mongId)
                    .deviceId(deviceId)
                    .accountId(accountId)
                    .build();

            var expected = matchPersistencePort.createQueuePlayerPort(createQueuePlayerVo);

            // assert
            assertFalse(expected.isEmpty());
            assertEquals(mongId, expected.get().getMongId());
            assertEquals(deviceId, expected.get().getDeviceId());
            assertEquals(accountId, expected.get().getAccountId());
        }
    }

    @Nested
    @DisplayName("매치 대기열 삭제 단위 테스트")
    class DeleteQueuePlayerPort {

        @AfterEach
        void afterEach() {
            queuePlayerRepository.deleteAll();
        }

        @Test
        @DisplayName("매치 대기열을 삭제 한다.")
        void deleteQueuePlayer() {
            // arrange
            long mongId = 1L;
            String deviceId = CommonUtil.randomId();
            long accountId = 1L;
            LocalDateTime now = LocalDateTime.now();
            QueuePlayer queuePlayer = QueuePlayer.builder()
                    .mongId(mongId)
                    .deviceId(deviceId)
                    .accountId(accountId)
                    .build();

            queuePlayerRepository.save(QueuePlayerEntity.builder()
                    .mongId(mongId)
                    .deviceId(deviceId)
                    .accountId(accountId)
                    .createdAt(now)
                    .build());

            queuePlayerRepository.save(QueuePlayerEntity.builder()
                    .mongId(2L)
                    .deviceId(CommonUtil.randomId())
                    .accountId(2L)
                    .createdAt(now.plusSeconds(5))
                    .build());

            // act
            var expected1 = queuePlayerRepository.findAll();
            var expected2 = matchPersistencePort.deleteQueuePlayerPort(queuePlayer);
            var expected3 = queuePlayerRepository.findAll();

            // assert
            assertEquals(2, expected1.size());
            assertFalse(expected2.isEmpty());
            assertEquals(mongId, expected2.get().getMongId());
            assertEquals(deviceId, expected2.get().getDeviceId());
            assertEquals(accountId, expected2.get().getAccountId());
            assertEquals(1, expected3.size());
        }

        @Test
        @DisplayName("삭제할 대기열이 없는 경우 빈 옵셔널 객체를 반환 한다.")
        void deleteQueuePlayerWhenNotExists() {
            // arrange
            long mongId = 1L;
            String deviceId = CommonUtil.randomId();
            long accountId = 1L;
            LocalDateTime now = LocalDateTime.now();
            QueuePlayer queuePlayer = QueuePlayer.builder()
                    .mongId(mongId)
                    .deviceId(deviceId)
                    .accountId(accountId)
                    .build();

            queuePlayerRepository.save(QueuePlayerEntity.builder()
                    .mongId(2L)
                    .deviceId(CommonUtil.randomId())
                    .accountId(2L)
                    .createdAt(now)
                    .build());

            // act
            var expected1 = queuePlayerRepository.findAll();
            var expected2 = matchPersistencePort.deleteQueuePlayerPort(queuePlayer);
            var expected3 = queuePlayerRepository.findAll();

            // assert
            assertEquals(1, expected1.size());
            assertTrue(expected2.isEmpty());
            assertEquals(1, expected3.size());
        }
    }
}