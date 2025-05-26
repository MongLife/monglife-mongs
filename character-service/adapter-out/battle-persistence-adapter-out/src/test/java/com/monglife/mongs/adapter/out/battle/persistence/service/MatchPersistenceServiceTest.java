package com.monglife.mongs.adapter.out.battle.persistence.service;

import com.monglife.core.utils.CommonUtil;
import com.monglife.module.common.jpa.config.HibernateAutoConfig;
import com.monglife.module.common.jpa.config.JpaAuditingAutoConfig;
import com.monglife.mongs.adapter.out.battle.persistence.config.AdapterOutBattlePersistenceConfig;
import com.monglife.mongs.adapter.out.battle.persistence.config.BattleDataSourceConfig;
import com.monglife.mongs.adapter.out.battle.persistence.config.BattleRedisConfig;
import com.monglife.mongs.adapter.out.battle.persistence.entity.MatchEntity;
import com.monglife.mongs.adapter.out.battle.persistence.entity.MatchPickEntity;
import com.monglife.mongs.adapter.out.battle.persistence.entity.MatchPlayerEntity;
import com.monglife.mongs.adapter.out.battle.persistence.entity.QueuePlayerEntity;
import com.monglife.mongs.adapter.out.battle.persistence.repository.MatchRepository;
import com.monglife.mongs.adapter.out.battle.persistence.repository.QueuePlayerRepository;
import com.monglife.mongs.application.battle.port.out.MatchPersistencePort;
import com.monglife.mongs.application.battle.port.out.vo.CreateMatchVo;
import com.monglife.mongs.application.battle.port.out.vo.CreateQueuePlayerVo;
import com.monglife.mongs.domain.battle.enums.MatchPickCode;
import com.monglife.mongs.domain.battle.enums.MatchStateCode;
import com.monglife.mongs.domain.battle.model.Match;
import com.monglife.mongs.domain.battle.model.MatchPick;
import com.monglife.mongs.domain.battle.model.MatchPlayer;
import com.monglife.mongs.domain.battle.model.QueuePlayer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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

    @Nested
    @DisplayName("매치 조회 단위 테스트")
    class GetMatchPort {

        @Test
        @DisplayName("매치 정보를 조회 한다.")
        void getMatch() {
            // arrange
            List<MatchPlayerEntity> matchPlayerEntities = List.of(
                    MatchPlayerEntity.builder()
                            .playerId(CommonUtil.randomId())
                            .deviceId(CommonUtil.randomId())
                            .accountId(1L)
                            .mongId(1L)
                            .mongTypeCode("MONG-TYPE-CODE 1")
                            .mongTypeName("MONG-TYPE-NAME 1")
                            .mongName("MONG-NAME 1")
                            .attack(100D)
                            .heal(100D)
                            .defence(100D)
                            .isBot(false)
                            .hp(500D)
                            .isEnter(true)
                            .enteredAt(LocalDateTime.now())
                            .build(),
                    MatchPlayerEntity.builder()
                            .playerId(CommonUtil.randomId())
                            .deviceId(CommonUtil.randomId())
                            .accountId(2L)
                            .mongId(2L)
                            .mongTypeCode("MONG-TYPE-CODE 2")
                            .mongTypeName("MONG-TYPE-NAME 2")
                            .mongName("MONG-NAME 2")
                            .attack(200D)
                            .heal(200D)
                            .defence(200D)
                            .isBot(false)
                            .hp(1000D)
                            .isEnter(true)
                            .enteredAt(LocalDateTime.now())
                            .build());

            MatchEntity matchEntity = MatchEntity.builder()
                    .maxRound(10)
                    .matchPlayers(matchPlayerEntities)
                    .matchPicks(Collections.emptyList())
                    .round(1)
                    .stateCode(MatchStateCode.PROCESS)
                    .build();

            long matchId = matchRepository.save(matchEntity).getMatchId();

            // act
            var expected1 = matchPersistencePort.getMatchPort(matchId);
            var expected2 = expected1.map(match -> match.getMatchPlayers().get(0)).orElse(null);

            // assert
            assertFalse(expected1.isEmpty());
            assertNotNull(expected2);
            assertEquals(matchEntity.getMatchId(), expected1.get().getMatchId());
            assertEquals(matchEntity.getMaxRound(), expected1.get().getMaxRound());
            assertEquals(matchEntity.getMatchPicks().size(), expected1.get().getMatchPicks().size());
            assertEquals(matchEntity.getStateCode(), expected1.get().getMatchStateCode());
            assertEquals(matchEntity.getRound(), expected1.get().getRound());
            assertEquals(2, expected1.get().getMatchPlayers().size());
            assertEquals(matchEntity.getMatchPlayers().get(0).getPlayerId() , expected2.getPlayerId());
            assertEquals(matchEntity.getMatchPlayers().get(0).getDeviceId() , expected2.getDeviceId());
            assertEquals(matchEntity.getMatchPlayers().get(0).getAccountId() , expected2.getAccountId());
            assertEquals(matchEntity.getMatchPlayers().get(0).getMongId() , expected2.getMongId());
            assertEquals(matchEntity.getMatchPlayers().get(0).getMongTypeCode() , expected2.getMongTypeCode());
            assertEquals(matchEntity.getMatchPlayers().get(0).getMongTypeName() , expected2.getMongTypeName());
            assertEquals(matchEntity.getMatchPlayers().get(0).getMongName() , expected2.getMongName());
            assertEquals(matchEntity.getMatchPlayers().get(0).getAttack() , expected2.getAttack());
            assertEquals(matchEntity.getMatchPlayers().get(0).getHeal() , expected2.getHeal());
            assertEquals(matchEntity.getMatchPlayers().get(0).getDefence() , expected2.getDefence());
            assertEquals(matchEntity.getMatchPlayers().get(0).getIsBot() , expected2.getIsBot());
            assertEquals(matchEntity.getMatchPlayers().get(0).getHp() , expected2.getHp());
            assertEquals(matchEntity.getMatchPlayers().get(0).getIsEnter() , expected2.getIsEnter());
            assertEquals(matchEntity.getMatchPlayers().get(0).getEnteredAt() , expected2.getEnteredAt());
            assertEquals(matchEntity.getMatchPlayers().get(0).getExitedAt() , expected2.getExitedAt());
        }
    }

    @Nested
    @DisplayName("매치 등록 단위 테스트")
    class CreateMatchPort {

        @Test
        @DisplayName("매치 정보를 등록 한다.")
        void createMatch() {
            // arrange
            List<MatchPlayer> matchPlayers = List.of(
                    MatchPlayer.builder()
                            .playerId(CommonUtil.randomId())
                            .deviceId(CommonUtil.randomId())
                            .accountId(1L)
                            .mongId(1L)
                            .mongTypeCode("MONG-TYPE-CODE 1")
                            .mongTypeName("MONG-TYPE-NAME 1")
                            .mongName("MONG-NAME 1")
                            .attack(100D)
                            .heal(100D)
                            .defence(100D)
                            .isBot(false)
                            .hp(500D)
                            .isEnter(true)
                            .enteredAt(LocalDateTime.now())
                            .build(),
                    MatchPlayer.builder()
                            .playerId(CommonUtil.randomId())
                            .deviceId(CommonUtil.randomId())
                            .accountId(2L)
                            .mongId(2L)
                            .mongTypeCode("MONG-TYPE-CODE 2")
                            .mongTypeName("MONG-TYPE-NAME 2")
                            .mongName("MONG-NAME 2")
                            .attack(200D)
                            .heal(200D)
                            .defence(200D)
                            .isBot(false)
                            .hp(1000D)
                            .isEnter(true)
                            .enteredAt(LocalDateTime.now())
                            .build());

            CreateMatchVo createMatchVo = CreateMatchVo.builder()
                    .matchPlayers(matchPlayers)
                    .build();

            // act
            var expected1 = matchPersistencePort.createMatchPort(createMatchVo);
            var expected2 = expected1.map(match -> match.getMatchPlayers().get(0)).orElse(null);

            // assert
            assertFalse(expected1.isEmpty());
            assertNotNull(expected2);
            assertNotNull(expected1.get().getMatchId());
            assertEquals(createMatchVo.getRound(), expected1.get().getRound());
            assertEquals(createMatchVo.getMatchStateCode(), expected1.get().getMatchStateCode());
            assertEquals(createMatchVo.getMatchPlayers().size(), expected1.get().getMatchPlayers().size());
            assertEquals(createMatchVo.getMatchPlayers().get(0).getPlayerId() , expected2.getPlayerId());
            assertEquals(createMatchVo.getMatchPlayers().get(0).getDeviceId() , expected2.getDeviceId());
            assertEquals(createMatchVo.getMatchPlayers().get(0).getAccountId() , expected2.getAccountId());
            assertEquals(createMatchVo.getMatchPlayers().get(0).getMongId() , expected2.getMongId());
            assertEquals(createMatchVo.getMatchPlayers().get(0).getMongTypeCode() , expected2.getMongTypeCode());
            assertEquals(createMatchVo.getMatchPlayers().get(0).getMongTypeName() , expected2.getMongTypeName());
            assertEquals(createMatchVo.getMatchPlayers().get(0).getMongName() , expected2.getMongName());
            assertEquals(createMatchVo.getMatchPlayers().get(0).getAttack() , expected2.getAttack());
            assertEquals(createMatchVo.getMatchPlayers().get(0).getHeal() , expected2.getHeal());
            assertEquals(createMatchVo.getMatchPlayers().get(0).getDefence() , expected2.getDefence());
            assertEquals(createMatchVo.getMatchPlayers().get(0).getIsBot() , expected2.getIsBot());
            assertEquals(createMatchVo.getMatchPlayers().get(0).getHp() , expected2.getHp());
            assertEquals(createMatchVo.getMatchPlayers().get(0).getIsEnter() , expected2.getIsEnter());
            assertEquals(createMatchVo.getMatchPlayers().get(0).getEnteredAt() , expected2.getEnteredAt());
            assertEquals(createMatchVo.getMatchPlayers().get(0).getExitedAt() , expected2.getExitedAt());
        }
    }

    @Nested
    @DisplayName("매치 동기화 단위 테스트")
    class SaveMatchPort {

        @Test
        @DisplayName("매치 정보를 동기화 한다.")
        void saveMatch() {
            // arrange
            String playerId1 = CommonUtil.randomId();
            double attack1 = 100D;
            String playerId2 = CommonUtil.randomId();
            double attack2 = 100D;
            List<MatchPlayerEntity> matchPlayerEntities = new ArrayList<>(List.of(
                    MatchPlayerEntity.builder()
                            .playerId(playerId1)
                            .deviceId(CommonUtil.randomId())
                            .accountId(1L)
                            .mongId(1L)
                            .mongTypeCode("MONG-TYPE-CODE 1")
                            .mongTypeName("MONG-TYPE-NAME 1")
                            .mongName("MONG-NAME 1")
                            .attack(attack1)
                            .heal(100D)
                            .defence(100D)
                            .isBot(false)
                            .hp(5000D)
                            .isEnter(true)
                            .enteredAt(LocalDateTime.now())
                            .build(),
                    MatchPlayerEntity.builder()
                            .playerId(playerId2)
                            .deviceId(CommonUtil.randomId())
                            .accountId(2L)
                            .mongId(2L)
                            .mongTypeCode("MONG-TYPE-CODE 2")
                            .mongTypeName("MONG-TYPE-NAME 2")
                            .mongName("MONG-NAME 2")
                            .attack(attack2)
                            .heal(200D)
                            .defence(200D)
                            .isBot(false)
                            .hp(5000D)
                            .isEnter(true)
                            .enteredAt(LocalDateTime.now())
                            .build()));

            List<MatchPickEntity> matchPickEntities = new ArrayList<>(List.of(
                    MatchPickEntity.builder()
                            .playerId(playerId2)
                            .targetPlayerId(playerId1)
                            .round(1)
                            .pickCode(MatchPickCode.MATCH_PICK_ATTACK)
                            .value(attack2)
                            .build()));

            MatchEntity matchEntity = matchRepository.saveAndFlush(MatchEntity.builder()
                    .maxRound(10)
                    .matchPlayers(matchPlayerEntities)
                    .matchPicks(matchPickEntities)
                    .round(1)
                    .stateCode(MatchStateCode.PROCESS)
                    .build());

            Match saveMatch = matchEntity.toDomain();

            saveMatch.pickMatchPlayer(MatchPick.builder()
                    .matchPlayer(saveMatch.getMatchPlayer(playerId1))
                    .targetMatchPlayer(saveMatch.getMatchPlayer(playerId2))
                    .round(1)
                    .matchPickCode(MatchPickCode.MATCH_PICK_ATTACK)
                    .value(attack1)
                    .build());

            // act
            var expected1 = matchPersistencePort.saveMatchPort(saveMatch);
            var expected2 = expected1.map(match -> match.getMatchPlayers().get(0)).orElse(null);

            // assert
            assertFalse(expected1.isEmpty());
            assertNotNull(expected2);
            assertEquals(matchEntity.getMatchId(), expected1.get().getMatchId());
            assertEquals(matchEntity.getMaxRound(), expected1.get().getMaxRound());
            assertEquals(0, expected1.get().getMatchPicks().size());
            assertEquals(matchEntity.getStateCode(), expected1.get().getMatchStateCode());
            assertEquals(2, expected1.get().getRound());
            assertEquals(2, expected1.get().getMatchPlayers().size());
            assertEquals(matchEntity.getMatchPlayers().get(0).getPlayerId(), expected2.getPlayerId());
            assertEquals(matchEntity.getMatchPlayers().get(0).getDeviceId(), expected2.getDeviceId());
            assertEquals(matchEntity.getMatchPlayers().get(0).getAccountId(), expected2.getAccountId());
            assertEquals(matchEntity.getMatchPlayers().get(0).getMongId(), expected2.getMongId());
            assertEquals(matchEntity.getMatchPlayers().get(0).getMongTypeCode(), expected2.getMongTypeCode());
            assertEquals(matchEntity.getMatchPlayers().get(0).getMongTypeName(), expected2.getMongTypeName());
            assertEquals(matchEntity.getMatchPlayers().get(0).getMongName(), expected2.getMongName());
            assertEquals(matchEntity.getMatchPlayers().get(0).getAttack(), expected2.getAttack());
            assertEquals(matchEntity.getMatchPlayers().get(0).getHeal(), expected2.getHeal());
            assertEquals(matchEntity.getMatchPlayers().get(0).getDefence(), expected2.getDefence());
            assertEquals(matchEntity.getMatchPlayers().get(0).getIsBot(), expected2.getIsBot());
            assertTrue(expected2.getHp() < 5000D);
            assertEquals(matchEntity.getMatchPlayers().get(0).getIsEnter(), expected2.getIsEnter());
            assertEquals(matchEntity.getMatchPlayers().get(0).getEnteredAt(), expected2.getEnteredAt());
            assertEquals(matchEntity.getMatchPlayers().get(0).getExitedAt(), expected2.getExitedAt());
        }
    }
}