package com.monglife.mongs.domain.device.model;

import com.monglife.mongs.domain.device.exception.InvalidTotalWalkingCountException;
import com.monglife.mongs.domain.device.exception.NotEnoughCurrentWalkingCountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class StepTest {

    private static final String DEVICE_ID = "TEST-DEVICE-ID";
    private static final LocalDateTime DEVICE_BOOTED_AT = LocalDateTime.of(2025, 1, 1, 0, 0);

    @Nested
    @DisplayName("보유 걸음 수 페이 포인트 환전 단위 테스트")
    class ExchangeWalkingCountToPayPoint {

        @Test
        @DisplayName("보유 걸음 수를 감소 시키고 환전 페이 포인트를 반환 한다.")
        void exchangeWalkingCountToPayPoint() {
            // arrange
            int totalWalkingCount = 100;
            Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(totalWalkingCount)
                    .deviceBootedAt(DEVICE_BOOTED_AT)
                    .build();

            // act
            int expected = step.exchangeWalkingCountToPayPoint(totalWalkingCount);

            // assert
            assertTrue(expected > 0);
        }

        @Test
        @DisplayName("보유 걸음 수가 충분하지 않은 경우 예외가 발생 한다.")
        void exchangeWalkingCountToPayPointWhenNotEnoughCurrentWalkingCount() {
            // arrange
            Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(0)
                    .deviceBootedAt(DEVICE_BOOTED_AT)
                    .build();

            // act & assert
            assertThrows(NotEnoughCurrentWalkingCountException.class, () -> step.exchangeWalkingCountToPayPoint(Integer.MAX_VALUE));
        }
    }

    @Nested
    @DisplayName("보유 걸음 수 증가 단위 테스트")
    class IncreaseCurrentWalkingCount {

        @Test
        @DisplayName("보유 걸음 수를 증가 시킨다.")
        void increaseCurrentWalkingCount() {
            // arrange
            int walkingCount = 100;
            Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(0)
                    .deviceBootedAt(DEVICE_BOOTED_AT)
                    .build();

            // act
            step.increaseCurrentWalkingCount(walkingCount);

            // assert
            assertEquals(walkingCount, step.getCurrentWalkingCount());
        }
    }

    @Nested
    @DisplayName("보유 걸음 수 감소 단위 테스트")
    class DecreaseCurrentWalkingCount {

        @Test
        @DisplayName("보유 걸음 수를 감소 시킨다.")
        void decreaseCurrentWalkingCount() {
            // arrange
            int totalWalkingCount = 100;
            Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(totalWalkingCount)
                    .deviceBootedAt(DEVICE_BOOTED_AT)
                    .build();

            // act
            step.decreaseCurrentWalkingCount(totalWalkingCount);

            // assert
            assertEquals(0, step.getCurrentWalkingCount());
        }
    }

    @Nested
    @DisplayName("총 걸음 수 동기화 단위 테스트 ")
    class SyncTotalWalkingCount {

        @Test
        @DisplayName("부팅 시간이 동일한 경우 걸음 수를 동기화 한다.")
        void syncTotalWalkingCount() {
            // arrange
            int totalWalkingCount = 100;
            int newTotalWalkingCount = totalWalkingCount + 50;
            Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(totalWalkingCount)
                    .deviceBootedAt(DEVICE_BOOTED_AT)
                    .build();

            // act
            step.syncTotalWalkingCount(newTotalWalkingCount, DEVICE_BOOTED_AT);

            // assert
            assertEquals(newTotalWalkingCount, step.getCurrentWalkingCount());
        }

        @Test
        @DisplayName("부팅 시간이 지난 경우 총 걸음 수를 초기화 한다.")
        void resetStep() {
            // arrange
            int totalWalkingCount = 100;
            int newTotalWalkingCount = 50;
            LocalDateTime newDeviceBootedDt = DEVICE_BOOTED_AT.plusDays(1);
            Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(totalWalkingCount)
                    .deviceBootedAt(DEVICE_BOOTED_AT)
                    .build();

            // act
            step.syncTotalWalkingCount(newTotalWalkingCount, newDeviceBootedDt);

            // assert
            assertEquals(newDeviceBootedDt, step.getDeviceBootedAt());
            assertEquals(totalWalkingCount, step.getWalkingCount());
            assertEquals(newTotalWalkingCount, step.getTotalWalkingCount());
            assertEquals(totalWalkingCount + newTotalWalkingCount, step.getCurrentWalkingCount());
        }

        @Test
        @DisplayName("부팅 시간이 동일하지만 현재 총 걸음 수보다 적은 총 걸음 수인 경우 예외가 발생 한다.")
        void updateTotalWalkingCountWhenLeastTotalWalkingCount() {
            // arrange
            int newTotalWalkingCount = 0;
            Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .consumeWalkingCount(0)
                    .totalWalkingCount(Integer.MAX_VALUE)
                    .deviceBootedAt(DEVICE_BOOTED_AT)
                    .build();

            // act & assert
            assertThrows(InvalidTotalWalkingCountException.class, () -> step.syncTotalWalkingCount(newTotalWalkingCount, DEVICE_BOOTED_AT));
        }
    }
}