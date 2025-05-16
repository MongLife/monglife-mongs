package com.monglife.mongs.application.member.port.in;

import com.monglife.mongs.application.member.port.exception.InvalidCreatePlayerException;
import com.monglife.mongs.application.member.port.exception.NotExistsPlayerException;
import com.monglife.mongs.application.member.port.in.command.*;
import com.monglife.mongs.application.member.port.in.service.PlayerService;
import com.monglife.mongs.application.member.port.out.MemberEventPort;
import com.monglife.mongs.application.member.port.out.MemberPersistencePort;
import com.monglife.mongs.application.member.port.out.MemberPublishPort;
import com.monglife.mongs.domain.member.exception.NotEnoughStarPointException;
import com.monglife.mongs.domain.member.model.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PlayerUseCaseTest {

    private final MemberPersistencePort memberPersistencePort;

    private final MemberPublishPort memberPublishPort;

    private final MemberEventPort memberEventPort;

    private final PlayerUseCase playerUseCase;

    public PlayerUseCaseTest() {
        this.memberPersistencePort = Mockito.mock(MemberPersistencePort.class);
        this.memberPublishPort = Mockito.mock(MemberPublishPort.class);
        this.memberEventPort = Mockito.mock(MemberEventPort.class);
        this.playerUseCase = new PlayerService(memberPersistencePort, memberPublishPort, memberEventPort);
    }

    private static final Long ACCOUNT_ID = 1L;

    @Nested
    @DisplayName("플레이어 등록 단위 테스트")
    class CreatePlayerUseCase {

        @Test
        @DisplayName("플레이어를 등록 한다.")
        void createPlayer() {
            // arrange
            Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            Mockito.when(memberPersistencePort.isExistsPlayerPort(ACCOUNT_ID)).thenReturn(false);
            Mockito.when(memberPersistencePort.createPlayerPort(Mockito.any())).thenReturn(Optional.of(player));

            // act
            CreatePlayerCommand command = CreatePlayerCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .build();

            playerUseCase.createPlayerUseCase(command);

            // assert
            Mockito.verify(memberPersistencePort).isExistsPlayerPort(command.getAccountId());
            Mockito.verify(memberPersistencePort).createPlayerPort(Mockito.any());
        }

        @Test
        @DisplayName("플레이어가 존재하는 경우 등록하지 않는다.")
        void createPlayerWhenNotExistsPlayer() {
            // arrange
            Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            Mockito.when(memberPersistencePort.isExistsPlayerPort(ACCOUNT_ID)).thenReturn(true);
            Mockito.when(memberPersistencePort.createPlayerPort(Mockito.any())).thenReturn(Optional.of(player));

            // act
            CreatePlayerCommand command = CreatePlayerCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .build();

            playerUseCase.createPlayerUseCase(command);

            // assert
            Mockito.verify(memberPersistencePort).isExistsPlayerPort(command.getAccountId());
            Mockito.verify(memberPersistencePort, Mockito.never()).createPlayerPort(Mockito.any());
        }

        @Test
        @DisplayName("플레이어 등록에 실패하는 경우 예외가 발생 한다.")
        void createPlayerFail() {
            // arrange
            Mockito.when(memberPersistencePort.isExistsPlayerPort(ACCOUNT_ID)).thenReturn(false);
            Mockito.when(memberPersistencePort.createPlayerPort(Mockito.any())).thenReturn(Optional.empty());

            // act
            CreatePlayerCommand command = CreatePlayerCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .build();

            assertThrows(InvalidCreatePlayerException.class, () -> playerUseCase.createPlayerUseCase(command));

            Mockito.verify(memberPersistencePort).isExistsPlayerPort(command.getAccountId());
            Mockito.verify(memberPersistencePort).createPlayerPort(Mockito.any());
        }
    }

    @Nested
    @DisplayName("플레이어 조회 단위 테스트")
    class GetPlayerUseCase {

        @Test
        @DisplayName("플레이어를 조회 한다.")
        void getPlayer() {
            // arrange
            Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            Mockito.when(memberPersistencePort.getPlayerPort(ACCOUNT_ID)).thenReturn(Optional.of(player));

            // act
            GetPlayerCommand command = GetPlayerCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .build();

            Player expected = playerUseCase.getPlayerUseCase(command);

            // assert
            assertEquals(player, expected);
            Mockito.verify(memberPersistencePort).getPlayerPort(command.getAccountId());
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
            Mockito.verify(memberPersistencePort).getPlayerPort(command.getAccountId());
        }
    }

    @Nested
    @DisplayName("슬롯 구매 단위 테스트")
    class BuySlotUseCase {

        @Test
        @DisplayName("추가 슬롯을 구매 한다.")
        void buySlot() {
            // arrange
            int starPoint = 100;
            Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(1)
                    .starPoint(starPoint)
                    .build();

            Mockito.when(memberPersistencePort.getPlayerPort(ACCOUNT_ID)).thenReturn(Optional.of(player));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.of(player));

            // act
            BuySlotCommand command = BuySlotCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .build();

            player = playerUseCase.buySlotUseCase(command);

            // assert
            assertEquals(2, player.getSlotCount());
            assertTrue(player.getStarPoint() < starPoint);
            Mockito.verify(memberPersistencePort).getPlayerPort(command.getAccountId());
            Mockito.verify(memberPersistencePort).savePlayerPort(player);
            Mockito.verify(memberPublishPort).publishStarPointPort(player);
            Mockito.verify(memberPublishPort).publishSlotCountPort(player);
        }

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
            Mockito.verify(memberPersistencePort).getPlayerPort(command.getAccountId());
        }

        @Test
        @DisplayName("플레이어를 수정할 때 플레이어가 존재하지 않는 경우 예외가 발생 한다.")
        void notExistsPlayerWhenSavePlayer() {
            // arrange
            int starPoint = 100;
            Player player = Player.builder()
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
            Mockito.verify(memberPersistencePort).getPlayerPort(command.getAccountId());
            Mockito.verify(memberPersistencePort).savePlayerPort(Mockito.any());
        }

        @Test
        @DisplayName("스타 포인트가 부족한 경우 예외가 발생 한다.")
        void notEnoughStarPoint() {
            // arrange
            Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            Mockito.when(memberPersistencePort.getPlayerPort(ACCOUNT_ID)).thenReturn(Optional.of(player));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.of(player));


            // act & assert
            BuySlotCommand command = BuySlotCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .build();

            assertThrows(NotEnoughStarPointException.class, () -> playerUseCase.buySlotUseCase(command));
            Mockito.verify(memberPersistencePort).getPlayerPort(command.getAccountId());
            Mockito.verify(memberPersistencePort, Mockito.never()).savePlayerPort(player);
            Mockito.verify(memberPublishPort, Mockito.never()).publishStarPointPort(player);
            Mockito.verify(memberPublishPort, Mockito.never()).publishSlotCountPort(player);
        }
    }

    @Nested
    @DisplayName("스타 포인트 환전 단위 테스트")
    class ExchangeStarPointUseCase {

        @Test
        @DisplayName("스타 포인트를 환전 한다.")
        void exchangeStarPoint() {
            // arrange
            long mongId = 1L;
            int slotCount = 1;
            int starPoint = 100;
            int payPoint = 100000;
            Player player = Player.builder()
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

            player = playerUseCase.exchangeStarPointUseCase(command);

            // assert
            assertTrue(player.getStarPoint() < starPoint);
            Mockito.verify(memberPersistencePort).getPlayerPort(command.getAccountId());
            Mockito.verify(memberPersistencePort).savePlayerPort(player);
            Mockito.verify(memberEventPort).exchangeStarPointEventPort(ACCOUNT_ID, mongId, starPoint, payPoint);
            Mockito.verify(memberPublishPort).publishStarPointPort(player);
        }

        @Test
        @DisplayName("플레이어가 존재하지 않는 경우 예외가 발생 한다.")
        void notExistsPlayer() {
            // arrange
            long mongId = 1L;
            int starPoint = 100;

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
            long mongId = 1L;
            int slotCount = 1;
            int starPoint = 100;
            Player player = Player.builder()
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
            Mockito.verify(memberPersistencePort).getPlayerPort(command.getAccountId());
            Mockito.verify(memberPersistencePort).savePlayerPort(Mockito.any());
        }

        @Test
        @DisplayName("스타 포인트가 부족한 경우 예외가 발생 한다.")
        void notEnoughStarPoint() {
            // arrange
            long mongId = 1L;
            int starPoint = 100;
            int payPoint = 1000;
            Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            Mockito.when(memberPersistencePort.getPlayerPort(ACCOUNT_ID)).thenReturn(Optional.of(player));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.of(player));

            // act & assert
            ExchangeStarPointCommand command = ExchangeStarPointCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .mongId(1L)
                    .starPoint(100)
                    .build();

            assertThrows(NotEnoughStarPointException.class, () -> playerUseCase.exchangeStarPointUseCase(command));
            Mockito.verify(memberPersistencePort).getPlayerPort(command.getAccountId());
            Mockito.verify(memberPersistencePort, Mockito.never()).savePlayerPort(player);
            Mockito.verify(memberEventPort, Mockito.never()).exchangeStarPointEventPort(ACCOUNT_ID, mongId, starPoint, payPoint);
            Mockito.verify(memberPublishPort, Mockito.never()).publishStarPointPort(player);
        }
    }

    @Nested
    @DisplayName("스타 포인트 증가 단위 테스트")
    class IncreaseStarPointUseCase {

        @Test
        @DisplayName("스타 포인트를 증가 시킨다.")
        void increaseStarPoint() {
            // arrange
            int starPoint = 100;
            Player player = Player.builder()
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

            player = playerUseCase.increaseStarPointUseCase(command);

            // assert
            assertEquals(starPoint, player.getStarPoint());
            Mockito.verify(memberPersistencePort).getPlayerPort(command.getAccountId());
            Mockito.verify(memberPersistencePort).savePlayerPort(player);
            Mockito.verify(memberPublishPort).publishStarPointPort(player);
        }

        @Test
        @DisplayName("플레이어가 존재하지 않는 경우 예외가 발생 한다.")
        void notExistsPlayer() {
            // arrange
            int starPoint = 100;

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
            int starPoint = 100;
            Player player = Player.builder()
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
            assertEquals(starPoint, player.getStarPoint());
            Mockito.verify(memberPersistencePort).getPlayerPort(command.getAccountId());
            Mockito.verify(memberPersistencePort).savePlayerPort(player);
            Mockito.verify(memberPublishPort, Mockito.never()).publishStarPointPort(player);
        }
    }
}