package com.monglife.mongs.application.battle.port.in.service;

import com.monglife.core.utils.CommonUtil;
import com.monglife.mongs.application.battle.port.exception.NotExistsQueuePlayerException;
import com.monglife.mongs.application.battle.port.in.QueueUseCase;
import com.monglife.mongs.application.battle.port.in.command.CreateQueuePlayerCommand;
import com.monglife.mongs.application.battle.port.in.command.DeleteQueuePlayerCommand;
import com.monglife.mongs.application.battle.port.in.command.MatchingQueuePlayersCommand;
import com.monglife.mongs.application.battle.port.out.MatchPersistencePort;
import com.monglife.mongs.application.battle.port.out.MongPersistencePort;
import com.monglife.mongs.application.battle.port.out.MongReadPort;
import com.monglife.mongs.application.battle.port.out.QueuePublishPort;
import com.monglife.mongs.application.battle.port.out.vo.CreateMatchVo;
import com.monglife.mongs.domain.battle.model.Match;
import com.monglife.mongs.domain.battle.model.MatchPlayer;
import com.monglife.mongs.domain.battle.model.QueuePlayer;
import com.monglife.mongs.domain.mong.model.Mong;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class QueueServiceTest {

    private final MatchPersistencePort matchPersistencePort = Mockito.mock(MatchPersistencePort.class);
    private final MongPersistencePort mongPersistencePort = Mockito.mock(MongPersistencePort.class);
    private final MongReadPort mongReadPort = Mockito.mock(MongReadPort.class);
    private final QueuePublishPort queuePublishPort = Mockito.mock(QueuePublishPort.class);
    private final QueueUseCase queueUseCase = new QueueService(matchPersistencePort, mongPersistencePort, mongReadPort, queuePublishPort);

    @Nested
    @DisplayName("매치 대기열 생성 단위 테스트")
    class CreateQueuePlayerUseCase {

        @Test
        @DisplayName("매치 대기열을 생성 한다.")
        void createQueuePlayer() {
            // arrange
            long mongId = 1L;
            long accountId = 1L;
            String deviceId = CommonUtil.randomId();
            QueuePlayer queuePlayer = new QueuePlayer(accountId, deviceId, mongId);

            Mockito.when(matchPersistencePort.createQueuePlayerPort(Mockito.any())).thenReturn(Optional.of(queuePlayer));
            Mockito.when(mongPersistencePort.getMongPort(queuePlayer.getMongId())).thenReturn(Optional.of(Mong.builder()
                    .mongId(queuePlayer.getMongId())
                    .accountId(queuePlayer.getAccountId())
                    .mongCode("TEST-MONG_TYPE-CODE")
                    .mongName("TEST-MONG_TYPE-NAME")
                    .name("TEST-MONG-NAME")
                    .isSleep(Boolean.FALSE)
                    .strength(100D)
                    .satiety(100D)
                    .healthy(100D)
                    .fatigue(100D)
                    .exp(0D)
                    .weight(100D)
                    .payPoint(Match.getBettingPayPoint())
                    .build()));

            // act
            CreateQueuePlayerCommand command = CreateQueuePlayerCommand.builder()
                    .mongId(mongId)
                    .accountId(accountId)
                    .deviceId(deviceId)
                    .build();

            var expected = queueUseCase.createQueuePlayerUseCase(command);

            // assert
            assertEquals(mongId, expected.getMongId());
            assertEquals(accountId, expected.getAccountId());
            assertEquals(deviceId, expected.getDeviceId());
        }
    }

    @Nested
    @DisplayName("매치 대기열 삭제 단위 테스트")
    class DeleteQueuePlayerUseCase {

        @Test
        @DisplayName("매치 대기열을 삭제 한다.")
        void deleteQueuePlayer() {
            // arrange
            long mongId = 1L;
            long accountId = 1L;
            String deviceId = CommonUtil.randomId();
            QueuePlayer queuePlayer = new QueuePlayer(accountId, deviceId, mongId);

            Mockito.when(matchPersistencePort.getQueuePlayerPort(mongId, accountId, deviceId)).thenReturn(Optional.of(queuePlayer));
            Mockito.when(matchPersistencePort.deleteQueuePlayerPort(queuePlayer)).thenReturn(Optional.of(queuePlayer));
            Mockito.when(mongPersistencePort.getMongPort(queuePlayer.getMongId())).thenReturn(Optional.of(Mong.builder()
                    .mongId(queuePlayer.getMongId())
                    .accountId(queuePlayer.getAccountId())
                    .mongCode("TEST-MONG_TYPE-CODE")
                    .mongName("TEST-MONG_TYPE-NAME")
                    .name("TEST-MONG-NAME")
                    .isSleep(Boolean.FALSE)
                    .strength(100D)
                    .satiety(100D)
                    .healthy(100D)
                    .fatigue(100D)
                    .exp(0D)
                    .weight(100D)
                    .payPoint(Match.getBettingPayPoint())
                    .build()));

            // act
            DeleteQueuePlayerCommand command = DeleteQueuePlayerCommand.builder()
                    .mongId(mongId)
                    .accountId(accountId)
                    .deviceId(deviceId)
                    .build();

            var expected = queueUseCase.deleteQueuePlayerUseCase(command);

            // assert
            assertEquals(mongId, expected.getMongId());
            assertEquals(accountId, expected.getAccountId());
            assertEquals(deviceId, expected.getDeviceId());
        }
    }

    @Nested
    @DisplayName("매치 대기열 기준 플레이어 매칭 단위 테스트")
    class MatchingQueuePlayersUseCase {

        @Test
        @DisplayName("매치 대기열 기준 플레이어 간 매칭을 진행하고 매칭 성공 비동기 응답을 전송 한다.")
        void matchingQueuePlayers() {
            // arrange
            int matchPlayerCount = 2;
            List<QueuePlayer> queuePlayers = List.of(
                    new QueuePlayer(1L, CommonUtil.randomId(), 1L),
                    new QueuePlayer(2L, CommonUtil.randomId(), 2L)
            );

            Match match = Match.builder()
                    .matchId(1L)
                    .round(0)
                    .build();

            Mockito.when(matchPersistencePort.getQueuePlayersPort(matchPlayerCount, QueuePlayer.getExpiredSeconds())).thenReturn(queuePlayers);
            Mockito.when(matchPersistencePort.createMatchPort(Mockito.any())).thenReturn(Optional.of(match));
            for (QueuePlayer queuePlayer : queuePlayers) {
                Mockito.when(mongReadPort.getMongPort(queuePlayer.getMongId())).thenReturn(Optional.of(Mong.builder()
                        .mongId(queuePlayer.getMongId())
                        .accountId(queuePlayer.getAccountId())
                        .mongCode("TEST-MONG_TYPE-CODE")
                        .mongName("TEST-MONG_TYPE-NAME")
                        .name("TEST-MONG-NAME")
                        .isSleep(Boolean.FALSE)
                        .strength(100D)
                        .satiety(100D)
                        .healthy(100D)
                        .fatigue(100D)
                        .exp(0D)
                        .weight(100D)
                        .payPoint(Match.getBettingPayPoint())
                        .build()));
            }

            // act
            MatchingQueuePlayersCommand command = MatchingQueuePlayersCommand.builder()
                    .matchPlayerCount(matchPlayerCount)
                    .build();

            queueUseCase.matchingQueuePlayersUseCase(command);

            // assert
            ArgumentCaptor<CreateMatchVo> captor = ArgumentCaptor.forClass(CreateMatchVo.class);

            Mockito.verify(matchPersistencePort).createMatchPort(captor.capture());
            Mockito.verify(queuePublishPort).publishMatchingQueuePlayerPort(Mockito.any());
            for (MatchPlayer matchPlayer : captor.getValue().getMatchPlayers()) {
                assertFalse(matchPlayer.getIsBot());
            }
        }

        @Test
        @DisplayName("매치 대기열 기준 봇 플레이어와의 매칭을 진행 한다.")
        void matchingQueuePlayersWithBot() {
            // arrange
            int matchPlayerCount = 2;
            List<QueuePlayer> queuePlayers = List.of(new QueuePlayer(1L, CommonUtil.randomId(), 1L));

            Match match = Match.builder()
                    .matchId(1L)
                    .round(0)
                    .build();

            Mockito.when(matchPersistencePort.getQueuePlayersPort(matchPlayerCount, QueuePlayer.getExpiredSeconds())).thenReturn(queuePlayers);
            Mockito.when(matchPersistencePort.createMatchPort(Mockito.any())).thenReturn(Optional.of(match));
            for (QueuePlayer queuePlayer : queuePlayers) {
                Mockito.when(mongReadPort.getMongPort(queuePlayer.getMongId())).thenReturn(Optional.of(Mong.builder()
                        .mongId(queuePlayer.getMongId())
                        .accountId(queuePlayer.getAccountId())
                        .mongCode("TEST-MONG_TYPE-CODE")
                        .mongName("TEST-MONG_TYPE-NAME")
                        .name("TEST-MONG-NAME")
                        .isSleep(Boolean.FALSE)
                        .strength(100D)
                        .satiety(100D)
                        .healthy(100D)
                        .fatigue(100D)
                        .exp(0D)
                        .weight(100D)
                        .payPoint(Match.getBettingPayPoint())
                        .build()));
            }

            // act
            MatchingQueuePlayersCommand command = MatchingQueuePlayersCommand.builder()
                    .matchPlayerCount(matchPlayerCount)
                    .build();

            queueUseCase.matchingQueuePlayersUseCase(command);

            // assert
            ArgumentCaptor<CreateMatchVo> captor = ArgumentCaptor.forClass(CreateMatchVo.class);

            Mockito.verify(matchPersistencePort).createMatchPort(captor.capture());
            Mockito.verify(queuePublishPort).publishMatchingQueuePlayerPort(Mockito.any());
            assertFalse(captor.getValue().getMatchPlayers().get(0).getIsBot());
            assertTrue(captor.getValue().getMatchPlayers().get(1).getIsBot());
        }

        @Test
        @DisplayName("매치 대기열이 없는 경우 예외가 발생 한다.")
        void matchingQueuePlayersWhenNotExistsQueuePlayer() {
            // arrange
            int matchPlayerCount = 2;
            List<QueuePlayer> queuePlayers = List.of();

            Mockito.when(matchPersistencePort.getQueuePlayersPort(matchPlayerCount, QueuePlayer.getExpiredSeconds())).thenReturn(queuePlayers);

            // act & assert
            MatchingQueuePlayersCommand command = MatchingQueuePlayersCommand.builder()
                    .matchPlayerCount(matchPlayerCount)
                    .build();

            assertThrows(NotExistsQueuePlayerException.class, () -> queueUseCase.matchingQueuePlayersUseCase(command));
        }

        @Test
        @DisplayName("몽이 없는 경우 매치 대기열을 재등록 한다.")
        void matchingQueuePlayersWhenNotExistsMong() {
            // arrange
            int matchPlayerCount = 2;
            List<QueuePlayer> queuePlayers = List.of(
                    new QueuePlayer(1L, CommonUtil.randomId(), 1L),
                    new QueuePlayer(2L, CommonUtil.randomId(), 2L)
            );

            Match match = Match.builder()
                    .matchId(1L)
                    .round(0)
                    .build();

            Mockito.when(matchPersistencePort.getQueuePlayersPort(matchPlayerCount, QueuePlayer.getExpiredSeconds())).thenReturn(queuePlayers);
            Mockito.when(matchPersistencePort.createMatchPort(Mockito.any())).thenReturn(Optional.of(match));
            Mockito.when(mongReadPort.getMongPort(queuePlayers.get(0).getMongId())).thenReturn(Optional.empty());
            Mockito.when(mongReadPort.getMongPort(queuePlayers.get(1).getMongId())).thenReturn(Optional.of(Mong.builder()
                    .mongId(queuePlayers.get(1).getMongId())
                    .accountId(queuePlayers.get(1).getAccountId())
                    .mongCode("TEST-MONG_TYPE-CODE")
                    .mongName("TEST-MONG_TYPE-NAME")
                    .name("TEST-MONG-NAME")
                    .isSleep(Boolean.FALSE)
                    .strength(100D)
                    .satiety(100D)
                    .healthy(100D)
                    .fatigue(100D)
                    .exp(0D)
                    .weight(100D)
                    .payPoint(Match.getBettingPayPoint())
                    .build()));
            Mockito.when(matchPersistencePort.createQueuePlayerPort(Mockito.any())).thenReturn(Optional.of(queuePlayers.get(1)));

            // act & assert
            MatchingQueuePlayersCommand command = MatchingQueuePlayersCommand.builder()
                    .matchPlayerCount(matchPlayerCount)
                    .build();

            assertDoesNotThrow(() -> queueUseCase.matchingQueuePlayersUseCase(command));
        }

        @Test
        @DisplayName("매치 대기열 재등록에 실패하는 경우 대기열 등록 실패 응답을 비동기로 전송 한다.")
        void matchingQueuePlayersWhenReCreateQueuePlayerFail() {
            // arrange
            int matchPlayerCount = 2;
            List<QueuePlayer> queuePlayers = List.of(
                    new QueuePlayer(1L, CommonUtil.randomId(), 1L),
                    new QueuePlayer(2L, CommonUtil.randomId(), 2L)
            );

            Match match = Match.builder()
                    .matchId(1L)
                    .round(0)
                    .build();

            Mockito.when(matchPersistencePort.getQueuePlayersPort(matchPlayerCount, QueuePlayer.getExpiredSeconds())).thenReturn(queuePlayers);
            Mockito.when(mongPersistencePort.getMongPort(queuePlayers.get(0).getMongId())).thenReturn(Optional.empty());
            Mockito.when(mongReadPort.getMongPort(queuePlayers.get(1).getMongId())).thenReturn(Optional.of(Mong.builder()
                    .mongId(queuePlayers.get(1).getMongId())
                    .accountId(queuePlayers.get(1).getAccountId())
                    .mongCode("TEST-MONG_TYPE-CODE")
                    .mongName("TEST-MONG_TYPE-NAME")
                    .name("TEST-MONG-NAME")
                    .isSleep(Boolean.FALSE)
                    .strength(100D)
                    .satiety(100D)
                    .healthy(100D)
                    .fatigue(100D)
                    .exp(0D)
                    .weight(100D)
                    .payPoint(Match.getBettingPayPoint())
                    .build()));
            Mockito.when(matchPersistencePort.createQueuePlayerPort(Mockito.any())).thenReturn(Optional.empty());
            Mockito.when(matchPersistencePort.createMatchPort(Mockito.any())).thenReturn(Optional.of(match));

            // act
            MatchingQueuePlayersCommand command = MatchingQueuePlayersCommand.builder()
                    .matchPlayerCount(matchPlayerCount)
                    .build();

            queueUseCase.matchingQueuePlayersUseCase(command);

            // assert
            Mockito.verify(queuePublishPort).publishMatchingQueuePlayerFailPort(queuePlayers.get(0));
            Mockito.verify(queuePublishPort).publishMatchingQueuePlayerFailPort(queuePlayers.get(1));
        }
    }
}