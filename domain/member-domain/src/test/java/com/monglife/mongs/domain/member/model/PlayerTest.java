package com.monglife.mongs.domain.member.model;

import com.monglife.mongs.domain.member.exception.NotEnoughStarPointException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private static final Long ACCOUNT_ID = 1L;

    @Nested
    @DisplayName("슬롯 구매 단위 테스트")
    class BuySlot {

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
            Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            // act & assert
            assertThrows(NotEnoughStarPointException.class, player::buySlot);
        }
    }

    @Nested
    @DisplayName("스타 포인트 환전 단위 테스트")
    class ExchangeStarPoint {

        @Test
        @DisplayName("스타 포인트를 환전 한다.")
        void exchangeStarPoint() {
            // arrange
            int slotCount = 1;
            int starPoint = 100;
            Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(slotCount)
                    .starPoint(starPoint)
                    .build();

            // act
            int payPoint = player.exchangeStarPointToPayPoint(starPoint);

            // assert
            assertEquals(0, player.getStarPoint());
            assertTrue(payPoint > 0);
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
            int starPoint = 100;
            Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
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