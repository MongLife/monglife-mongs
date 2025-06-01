package com.monglife.mongs.domain.member.model;

import com.monglife.mongs.domain.member.exception.AlreadyMaxSlotCountException;
import com.monglife.mongs.domain.member.exception.NotEnoughStarPointException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Nested
    @DisplayName("슬롯 구매 단위 테스트")
    class BuySlot {

        @Test
        @DisplayName("추가 슬롯을 구매 한다.")
        void buySlot() {
            // arrange
            final long accountId = 1L;
            final int starPoint = Integer.MAX_VALUE;
            final Player player = Player.builder()
                    .accountId(accountId)
                    .slotCount(1)
                    .starPoint(starPoint)
                    .build();

            // act
            player.buySlot();

            // assert
            assertEquals(2, player.getSlotCount());
            assertTrue(player.getStarPoint() < starPoint);
        }

        @Test
        @DisplayName("스타 포인트가 부족한 경우 예외가 발생 한다.")
        void notEnoughStarPoint() {
            // arrange
            final long accountId = 1L;
            final Player player = Player.builder()
                    .accountId(accountId)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            // act & assert
            assertThrows(NotEnoughStarPointException.class, player::buySlot);
        }

        @Test
        @DisplayName("최대 슬롯 수 이상인 경우 예외가 발생 한다.")
        void alreadyMaxSlot() {
            // arrange
            final long accountId = 1L;
            final int starPoint = Integer.MAX_VALUE;
            final Player player = Player.builder()
                    .accountId(accountId)
                    .slotCount(Integer.MAX_VALUE)
                    .starPoint(starPoint)
                    .build();

            // act & assert
            assertThrows(AlreadyMaxSlotCountException.class, player::buySlot);
        }
    }

    @Nested
    @DisplayName("스타 포인트 환전 단위 테스트")
    class ExchangeStarPoint {

        @Test
        @DisplayName("스타 포인트를 환전 한다.")
        void exchangeStarPoint() {
            // arrange
            final long accountId = 1L;
            final int slotCount = 1;
            final int starPoint = 100;
            final Player player = Player.builder()
                    .accountId(accountId)
                    .slotCount(slotCount)
                    .starPoint(starPoint)
                    .build();

            // act
            final int expected = player.exchangeStarPointToPayPoint(starPoint);

            // assert
            assertEquals(0, player.getStarPoint());
            assertTrue(expected > 0);
        }

        @Test
        @DisplayName("스타 포인트가 부족한 경우 예외가 발생 한다.")
        void notEnoughStarPoint() {
            // arrange
            final long accountId = 1L;
            final Player player = Player.builder()
                    .accountId(accountId)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            // act & assert
            assertThrows(NotEnoughStarPointException.class, () -> player.exchangeStarPointToPayPoint(Integer.MAX_VALUE));
        }
    }

    @Nested
    @DisplayName("스타 포인트 증가 단위 테스트")
    class IncreaseStarPoint {

        @Test
        @DisplayName("스타 포인트를 증가 시킨다.")
        void increaseStarPoint() {
            // arrange
            final long accountId = 1L;
            final int starPoint = 100;
            final Player player = Player.builder()
                    .accountId(accountId)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            // act
            player.increaseStarPoint(starPoint);

            // assert
            assertEquals(starPoint, player.getStarPoint());
        }
    }
}