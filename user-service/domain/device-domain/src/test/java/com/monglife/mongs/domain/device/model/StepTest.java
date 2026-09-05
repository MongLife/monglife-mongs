package com.monglife.mongs.domain.device.model;

import com.monglife.mongs.domain.device.exception.ExceedDailyExchangeWalkingCountException;
import com.monglife.mongs.domain.device.exception.InvalidExchangeWalkingCountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StepTest {

    @Nested
    @DisplayName("걸음 수 환전 단위 테스트")
    class Of {

        @Test
        @DisplayName("10 걸음 당 1 페이 포인트로 환산 한다.")
        void toPayPoint() {
            // act
            Step step = Step.of(1_000);

            // assert
            assertEquals(1_000, step.getWalkingCount());
            assertEquals(100, step.getPayPoint());
        }

        @Test
        @DisplayName("나누어 떨어지지 않는 걸음 수는 올림 한다.")
        void ceil() {
            // 10 으로 나누어 떨어지지 않는 값은 사용자에게 유리한 쪽으로 올린다.
            assertEquals(1, Step.of(1).getPayPoint());
            assertEquals(2, Step.of(15).getPayPoint());
            assertEquals(1_000, Step.of(9_999).getPayPoint());
        }

        @Test
        @DisplayName("0 이하의 걸음 수는 환전할 수 없다.")
        void invalidWalkingCount() {
            assertThrows(InvalidExchangeWalkingCountException.class, () -> Step.of(0));
            assertThrows(InvalidExchangeWalkingCountException.class, () -> Step.of(-1_000));
        }

        @Test
        @DisplayName("걸음 수가 없으면 환전할 수 없다.")
        void nullWalkingCount() {
            assertThrows(InvalidExchangeWalkingCountException.class, () -> Step.of(null));
        }
    }

    @Nested
    @DisplayName("일일 환전 상한 단위 테스트")
    class ValidateDailyExchangeLimit {

        @Test
        @DisplayName("상한 이하는 통과 한다.")
        void underLimit() {
            assertDoesNotThrow(() -> Step.validateDailyExchangeLimit(Step.DAILY_EXCHANGE_LIMIT_WALKING_COUNT));
        }

        @Test
        @DisplayName("상한을 넘으면 예외가 발생 한다.")
        void overLimit() {
            assertThrows(ExceedDailyExchangeWalkingCountException.class,
                    () -> Step.validateDailyExchangeLimit(Step.DAILY_EXCHANGE_LIMIT_WALKING_COUNT + 1));
        }

        @Test
        @DisplayName("집계할 수 없어 0 이 들어오면 통과 한다.")
        void failOpen() {
            // Redis 장애 시 어댑터가 0 을 돌려준다. 상한 검사를 건너뛰고 환전이 진행되어야 한다.
            assertDoesNotThrow(() -> Step.validateDailyExchangeLimit(0));
        }
    }
}
