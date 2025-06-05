package com.monglife.mongs.application.member.port.in;

import com.monglife.mongs.application.member.port.exception.InvalidCreatePlayerException;
import com.monglife.mongs.application.member.port.exception.NotExistsPlayerException;
import com.monglife.mongs.application.member.port.in.command.*;
import com.monglife.mongs.application.member.port.in.service.PlayerService;
import com.monglife.mongs.application.member.port.out.MemberEventPort;
import com.monglife.mongs.application.member.port.out.MemberPersistencePort;
import com.monglife.mongs.application.member.port.out.MemberPublishPort;
import com.monglife.mongs.application.member.port.out.MemberReadPort;
import com.monglife.mongs.domain.member.model.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PlayerUseCaseTest {

    private final MemberPersistencePort memberPersistencePort = Mockito.mock(MemberPersistencePort.class);
    private final MemberReadPort memberReadPort = Mockito.mock(MemberReadPort.class);
    private final MemberPublishPort memberPublishPort = Mockito.mock(MemberPublishPort.class);
    private final MemberEventPort memberEventPort = Mockito.mock(MemberEventPort.class);
    private final PlayerUseCase playerUseCase = new PlayerService(memberPersistencePort, memberReadPort, memberPublishPort, memberEventPort);

    @Nested
    @DisplayName("플레이어 등록 단위 테스트")
    class CreatePlayerUseCase {

        private static final Long ACCOUNT_ID = 1L;

        @Test
        @DisplayName("플레이어를 등록 한다.")
        void createPlayer() {
            // arrange
            final Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            Mockito.when(memberReadPort.isExistsPlayerPort(ACCOUNT_ID)).thenReturn(false);
            Mockito.when(memberPersistencePort.createPlayerPort(Mockito.any())).thenReturn(Optional.of(player));

            // act & assert
            CreatePlayerCommand command = CreatePlayerCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .build();

            assertDoesNotThrow(() -> playerUseCase.createPlayerUseCase(command));
            Mockito.verify(memberPersistencePort).createPlayerPort(Mockito.any());
        }

        @Test
        @DisplayName("플레이어가 존재하는 경우 등록하지 않는다.")
        void createPlayerWhenNotExistsPlayer() {
            // arrange
            final Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            Mockito.when(memberReadPort.isExistsPlayerPort(ACCOUNT_ID)).thenReturn(true);
            Mockito.when(memberPersistencePort.createPlayerPort(Mockito.any())).thenReturn(Optional.of(player));

            // act & assert
            CreatePlayerCommand command = CreatePlayerCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .build();

            assertDoesNotThrow(() -> playerUseCase.createPlayerUseCase(command));
            Mockito.verify(memberPersistencePort, Mockito.never()).createPlayerPort(Mockito.any());
        }

        @Test
        @DisplayName("플레이어 등록에 실패하는 경우 예외가 발생 한다.")
        void createPlayerFail() {
            // arrange
            Mockito.when(memberReadPort.isExistsPlayerPort(ACCOUNT_ID)).thenReturn(false);
            Mockito.when(memberPersistencePort.createPlayerPort(Mockito.any())).thenReturn(Optional.empty());

            // act
            CreatePlayerCommand command = CreatePlayerCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .build();

            assertThrows(InvalidCreatePlayerException.class, () -> playerUseCase.createPlayerUseCase(command));
        }
    }

    @Nested
    @DisplayName("플레이어 조회 단위 테스트")
    class GetPlayerUseCase {

        private static final Long ACCOUNT_ID = 1L;

        @Test
        @DisplayName("플레이어를 조회 한다.")
        void getPlayer() {
            // arrange
            final Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            Mockito.when(memberReadPort.getPlayerPort(ACCOUNT_ID)).thenReturn(Optional.of(player));

            // act
            GetPlayerCommand command = GetPlayerCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .build();

            var expected = playerUseCase.getPlayerUseCase(command);

            // assert
            assertEquals(player, expected);
            Mockito.verify(memberReadPort).getPlayerPort(command.getAccountId());
        }

        @Test
        @DisplayName("플레이어가 존재하지 않는 경우 예외가 발생 한다.")
        void notExistsPlayer() {
            // arrange
            Mockito.when(memberPersistencePort.getPlayerPort(ACCOUNT_ID)).thenReturn(Optional.empty());

            // act & assert
            GetPlayerCommand command = GetPlayerCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .build();

            assertThrows(NotExistsPlayerException.class, () -> playerUseCase.getPlayerUseCase(command));
        }
    }

    @Nested
    @DisplayName("슬롯 구매 단위 테스트")
    class BuySlotUseCase {

        private static final Long ACCOUNT_ID = 1L;

        @Test
        @DisplayName("플레이어가 존재하지 않는 경우 예외가 발생 한다.")
        void notExistsPlayer() {
            // arrange
            Mockito.when(memberPersistencePort.getPlayerPort(ACCOUNT_ID)).thenReturn(Optional.empty());

            // act & assert
            BuySlotCommand command = BuySlotCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .build();

            assertThrows(NotExistsPlayerException.class, () -> playerUseCase.buySlotUseCase(command));
        }

        @Test
        @DisplayName("플레이어를 수정할 때 플레이어가 존재하지 않는 경우 예외가 발생 한다.")
        void notExistsPlayerWhenSavePlayer() {
            // arrange
            final int starPoint = 100;
            final Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(1)
                    .starPoint(starPoint)
                    .build();

            Mockito.when(memberPersistencePort.getPlayerPort(ACCOUNT_ID)).thenReturn(Optional.of(player));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.empty());

            // act & assert
            BuySlotCommand command = BuySlotCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .build();

            assertThrows(NotExistsPlayerException.class, () -> playerUseCase.buySlotUseCase(command));
        }
    }

    @Nested
    @DisplayName("스타 포인트 환전 단위 테스트")
    class ExchangeStarPointUseCase {

        private static final Long ACCOUNT_ID = 1L;

        @Test
        @DisplayName("스타 포인트를 환전 한다.")
        void exchangeStarPoint() {
            // arrange
            final long mongId = 1L;
            final int slotCount = 1;
            final int starPoint = 100;
            final int payPoint = 100000;
            final Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(slotCount)
                    .starPoint(starPoint)
                    .build();

            Mockito.when(memberPersistencePort.getPlayerPort(ACCOUNT_ID)).thenReturn(Optional.of(player));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.of(player));

            // act
            ExchangeStarPointCommand command = ExchangeStarPointCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .mongId(mongId)
                    .starPoint(starPoint)
                    .build();

            var expected = playerUseCase.exchangeStarPointUseCase(command);

            // assert
            Mockito.verify(memberEventPort).exchangeStarPointEventPort(ACCOUNT_ID, mongId, starPoint, payPoint);
            Mockito.verify(memberPublishPort).publishStarPointPort(expected);
        }

        @Test
        @DisplayName("플레이어가 존재하지 않는 경우 예외가 발생 한다.")
        void notExistsPlayer() {
            // arrange
            final long mongId = 1L;
            final int starPoint = 100;

            Mockito.when(memberPersistencePort.getPlayerPort(ACCOUNT_ID)).thenReturn(Optional.empty());

            // act & assert
            ExchangeStarPointCommand command = ExchangeStarPointCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .mongId(mongId)
                    .starPoint(starPoint)
                    .build();

            assertThrows(NotExistsPlayerException.class, () -> playerUseCase.exchangeStarPointUseCase(command));
            Mockito.verify(memberPersistencePort).getPlayerPort(command.getAccountId());
        }

        @Test
        @DisplayName("플레이어를 수정할 때 플레이어가 존재하지 않는 경우 예외가 발생 한다.")
        void notExistsPlayerWhenSavePlayer() {
            // arrange
            final long mongId = 1L;
            final int slotCount = 1;
            final int starPoint = 100;
            final Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(slotCount)
                    .starPoint(starPoint)
                    .build();

            Mockito.when(memberPersistencePort.getPlayerPort(ACCOUNT_ID)).thenReturn(Optional.of(player));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.empty());

            // act & assert
            ExchangeStarPointCommand command = ExchangeStarPointCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .mongId(mongId)
                    .starPoint(starPoint)
                    .build();

            assertThrows(NotExistsPlayerException.class, () -> playerUseCase.exchangeStarPointUseCase(command));
        }
    }

    @Nested
    @DisplayName("스타 포인트 증가 단위 테스트")
    class IncreaseStarPointUseCase {

        private static final Long ACCOUNT_ID = 1L;

        @Test
        @DisplayName("스타 포인트를 증가 시킨다.")
        void increaseStarPoint() {
            // arrange
            final int starPoint = 100;
            final Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            Mockito.when(memberPersistencePort.getPlayerPort(ACCOUNT_ID)).thenReturn(Optional.of(player));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.of(player));

            // act
            IncreaseStarPointCommand command = IncreaseStarPointCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .starPoint(starPoint)
                    .build();

            var expected = playerUseCase.increaseStarPointUseCase(command);

            // assert
            assertEquals(starPoint, expected.getStarPoint());
            Mockito.verify(memberPersistencePort).getPlayerPort(command.getAccountId());
            Mockito.verify(memberPersistencePort).savePlayerPort(expected);
            Mockito.verify(memberPublishPort).publishStarPointPort(expected);
        }

        @Test
        @DisplayName("플레이어가 존재하지 않는 경우 예외가 발생 한다.")
        void notExistsPlayer() {
            // arrange
            final int starPoint = 100;

            Mockito.when(memberPersistencePort.getPlayerPort(ACCOUNT_ID)).thenReturn(Optional.empty());

            // act & assert
            IncreaseStarPointCommand command = IncreaseStarPointCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .starPoint(starPoint)
                    .build();

            assertThrows(NotExistsPlayerException.class, () -> playerUseCase.increaseStarPointUseCase(command));
            Mockito.verify(memberPersistencePort).getPlayerPort(command.getAccountId());
        }

        @Test
        @DisplayName("플레이어를 수정할 때 플레이어가 존재하지 않는 경우 예외가 발생 한다.")
        void notExistsPlayerWhenSavePlayer() {
            // arrange
            final int starPoint = 100;
            final Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            Mockito.when(memberPersistencePort.getPlayerPort(ACCOUNT_ID)).thenReturn(Optional.of(player));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.empty());

            // act & assert
            IncreaseStarPointCommand command = IncreaseStarPointCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .starPoint(starPoint)
                    .build();

            assertThrows(NotExistsPlayerException.class, () -> playerUseCase.increaseStarPointUseCase(command));
            Mockito.verify(memberPersistencePort).getPlayerPort(command.getAccountId());
            Mockito.verify(memberPersistencePort).savePlayerPort(player);
            Mockito.verify(memberPublishPort, Mockito.never()).publishStarPointPort(player);
        }
    }
}