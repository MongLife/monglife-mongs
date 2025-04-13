package com.monglife.mongs.application.member.port.in;

import com.monglife.mongs.application.member.port.exception.NotExistsPlayerException;
import com.monglife.mongs.application.member.port.in.command.*;
import com.monglife.mongs.application.member.port.in.service.PlayerService;
import com.monglife.mongs.application.member.port.out.MemberEventPort;
import com.monglife.mongs.application.member.port.out.MemberPersistencePort;
import com.monglife.mongs.application.member.port.out.MemberPublishPort;
import com.monglife.mongs.domain.model.Player;
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

    private final PlayerService playerService;

    public PlayerUseCaseTest() {
        this.memberPersistencePort = Mockito.mock(MemberPersistencePort.class);
        this.memberPublishPort = Mockito.mock(MemberPublishPort.class);
        this.memberEventPort = Mockito.mock(MemberEventPort.class);
        this.playerService = new PlayerService(memberPersistencePort, memberPublishPort, memberEventPort);
    }

    private static final Long accountId = 1L;

    @Nested
    @DisplayName("플레이어 등록 단위 테스트")
    class CreatePlayerUseCase {

        @Test
        @DisplayName("플레이어를 등록 한다.")
        void createPlayer() {
            // arrange
            Player player = Player.builder()
                    .accountId(accountId)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            Mockito.when(memberPersistencePort.isExistsPlayerPort(accountId)).thenReturn(false);
            Mockito.when(memberPersistencePort.createPlayerPort(Mockito.any())).thenReturn(player);

            // act
            CreatePlayerCommand command = CreatePlayerCommand.builder()
                    .accountId(accountId)
                    .build();

            playerService.createPlayerUseCase(command);

            // assert
            Mockito.verify(memberPersistencePort).isExistsPlayerPort(command.getAccountId());
            Mockito.verify(memberPersistencePort).createPlayerPort(Mockito.any());
        }

        @Test
        @DisplayName("플레이어가 존재하는 경우 등록하지 않는다.")
        void createPlayerWhenNotExistsPlayer() {
            // arrange
            Player player = Player.builder()
                    .accountId(accountId)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            Mockito.when(memberPersistencePort.isExistsPlayerPort(accountId)).thenReturn(true);
            Mockito.when(memberPersistencePort.createPlayerPort(Mockito.any())).thenReturn(player);

            // act
            CreatePlayerCommand command = CreatePlayerCommand.builder()
                    .accountId(accountId)
                    .build();

            playerService.createPlayerUseCase(command);

            // assert
            Mockito.verify(memberPersistencePort).isExistsPlayerPort(command.getAccountId());
            Mockito.verify(memberPersistencePort, Mockito.never()).createPlayerPort(Mockito.any());
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
                    .accountId(accountId)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            Mockito.when(memberPersistencePort.getPlayerPort(accountId)).thenReturn(Optional.ofNullable(player));

            // act
            GetPlayerCommand command = GetPlayerCommand.builder()
                    .accountId(accountId)
                    .build();

            Player expected = playerService.getPlayerUseCase(command);

            // assert
            Mockito.verify(memberPersistencePort).getPlayerPort(command.getAccountId());
            assertEquals(player, expected);
        }

        @Test
        @DisplayName("플레이어가 존재하지 않는 경우 예외가 발생 한다.")
        void notExistsPlayer() {
            // arrange
            Mockito.when(memberPersistencePort.getPlayerPort(accountId)).thenReturn(Optional.empty());

            // act & assert
            GetPlayerCommand command = GetPlayerCommand.builder()
                    .accountId(accountId)
                    .build();

            assertThrows(NotExistsPlayerException.class, () -> playerService.getPlayerUseCase(command));
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
                    .accountId(accountId)
                    .slotCount(1)
                    .starPoint(starPoint)
                    .build();

            Mockito.when(memberPersistencePort.getPlayerPort(accountId)).thenReturn(Optional.ofNullable(player));

            // act
            BuySlotCommand command = BuySlotCommand.builder()
                    .accountId(accountId)
                    .build();

            playerService.buySlotUseCase(command);

            // assert
            Mockito.verify(memberPersistencePort).getPlayerPort(command.getAccountId());
            Mockito.verify(memberPersistencePort).savePlayerPort(player);
            Mockito.verify(memberPublishPort).publishStarPointPort(player);
            Mockito.verify(memberPublishPort).publishSlotCountPort(player);
            assertEquals(2, player.getSlotCount());
            assertTrue(player.getStarPoint() < starPoint);
        }

        @Test
        @DisplayName("플레이어가 존재하지 않는 경우 예외가 발생 한다.")
        void notExistsPlayer() {
            // arrange
            Mockito.when(memberPersistencePort.getPlayerPort(accountId)).thenReturn(Optional.empty());

            // act & assert
            BuySlotCommand command = BuySlotCommand.builder()
                    .accountId(accountId)
                    .build();

            assertThrows(NotExistsPlayerException.class, () -> playerService.buySlotUseCase(command));
            Mockito.verify(memberPersistencePort).getPlayerPort(command.getAccountId());
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
            Player player = Player.builder()
                    .accountId(accountId)
                    .slotCount(slotCount)
                    .starPoint(starPoint)
                    .build();

            Mockito.when(memberPersistencePort.getPlayerPort(accountId)).thenReturn(Optional.ofNullable(player));

            // act
            ExchangeStarPointCommand command = ExchangeStarPointCommand.builder()
                    .accountId(accountId)
                    .mongId(mongId)
                    .starPoint(starPoint)
                    .build();

            playerService.exchangeStarPointUseCase(command);

            // assert
            Mockito.verify(memberPersistencePort).getPlayerPort(command.getAccountId());
            Mockito.verify(memberPersistencePort).savePlayerPort(player);
            Mockito.verify(memberEventPort).exchangeStarPointEventPort(player);
            Mockito.verify(memberPublishPort).publishStarPointPort(player);
            assertTrue(player.getStarPoint() < starPoint);
        }

        @Test
        @DisplayName("플레이어가 존재하지 않는 경우 예외가 발생 한다.")
        void notExistsPlayer() {
            // arrange
            long mongId = 1L;
            int starPoint = 100;

            Mockito.when(memberPersistencePort.getPlayerPort(accountId)).thenReturn(Optional.empty());

            // act & assert
            ExchangeStarPointCommand command = ExchangeStarPointCommand.builder()
                    .accountId(accountId)
                    .mongId(mongId)
                    .starPoint(starPoint)
                    .build();

            assertThrows(NotExistsPlayerException.class, () -> playerService.exchangeStarPointUseCase(command));
            Mockito.verify(memberPersistencePort).getPlayerPort(command.getAccountId());
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
                    .accountId(accountId)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            Mockito.when(memberPersistencePort.getPlayerPort(accountId)).thenReturn(Optional.ofNullable(player));

            // act
            IncreaseStarPointCommand command = IncreaseStarPointCommand.builder()
                    .accountId(accountId)
                    .starPoint(starPoint)
                    .build();

            playerService.increaseStarPointUseCase(command);

            // assert
            Mockito.verify(memberPersistencePort).getPlayerPort(command.getAccountId());
            Mockito.verify(memberPersistencePort).savePlayerPort(player);
            Mockito.verify(memberPublishPort).publishStarPointPort(player);
            assertEquals(starPoint, player.getStarPoint());
        }

        @Test
        @DisplayName("플레이어가 존재하지 않는 경우 예외가 발생 한다.")
        void notExistsPlayer() {
            // arrange
            int starPoint = 100;

            Mockito.when(memberPersistencePort.getPlayerPort(accountId)).thenReturn(Optional.empty());

            // act & assert
            IncreaseStarPointCommand command = IncreaseStarPointCommand.builder()
                    .accountId(accountId)
                    .starPoint(starPoint)
                    .build();

            assertThrows(NotExistsPlayerException.class, () -> playerService.increaseStarPointUseCase(command));
            Mockito.verify(memberPersistencePort).getPlayerPort(command.getAccountId());
        }
    }
}