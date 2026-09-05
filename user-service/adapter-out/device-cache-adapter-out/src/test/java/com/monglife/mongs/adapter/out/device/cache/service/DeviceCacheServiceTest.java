package com.monglife.mongs.adapter.out.device.cache.service;

import com.monglife.mongs.application.device.port.out.DeviceCachePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DeviceCacheServiceTest {

    private static final Long ACCOUNT_ID = 1L;

    private StringRedisTemplate deviceRedisTemplate;
    private ValueOperations<String, String> valueOperations;
    private DeviceCachePort deviceCachePort;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void beforeEach() {
        deviceRedisTemplate = Mockito.mock(StringRedisTemplate.class);
        valueOperations = Mockito.mock(ValueOperations.class);
        Mockito.when(deviceRedisTemplate.opsForValue()).thenReturn(valueOperations);
        deviceCachePort = new DeviceCacheService(deviceRedisTemplate);
    }

    @Nested
    @DisplayName("오늘 누적 환전 걸음 수 증가 단위 테스트")
    class IncreaseTodayExchangedWalkingCount {

        @Test
        @DisplayName("누적된 값을 반환 한다.")
        void returnAccumulated() {
            // arrange
            Mockito.when(valueOperations.increment(Mockito.anyString(), Mockito.eq(1_000L))).thenReturn(4_000L);

            // act
            int total = deviceCachePort.increaseTodayExchangedWalkingCountPort(ACCOUNT_ID, 1_000);

            // assert
            assertThat(total).isEqualTo(4_000);
        }

        @Test
        @DisplayName("만료 시간을 건다.")
        void setsExpire() {
            // arrange
            Mockito.when(valueOperations.increment(Mockito.anyString(), Mockito.eq(1_000L))).thenReturn(1_000L);

            // act
            deviceCachePort.increaseTodayExchangedWalkingCountPort(ACCOUNT_ID, 1_000);

            // assert
            ArgumentCaptor<Duration> ttl = ArgumentCaptor.forClass(Duration.class);
            Mockito.verify(deviceRedisTemplate).expire(Mockito.anyString(), ttl.capture());
            assertThat(ttl.getValue()).isEqualTo(Duration.ofDays(2));
        }

        @Test
        @DisplayName("이미 있던 키에도 만료 시간을 매번 다시 건다.")
        void alwaysRefreshExpire() {
            // 첫 증가에만 걸면 INCRBY 직후 프로세스가 죽었을 때 만료 없는 키가 영구히 남는다.
            // 키 이름에 날짜가 들어가므로 매번 갱신해도 어제 키는 그대로 만료된다.
            // arrange - 누적값이 증가분보다 크다 = 오늘 이미 환전한 적이 있다
            Mockito.when(valueOperations.increment(Mockito.anyString(), Mockito.eq(1_000L))).thenReturn(5_000L);

            // act
            deviceCachePort.increaseTodayExchangedWalkingCountPort(ACCOUNT_ID, 1_000);

            // assert
            Mockito.verify(deviceRedisTemplate).expire(Mockito.anyString(), Mockito.eq(Duration.ofDays(2)));
        }

        @Test
        @DisplayName("Redis 가 실패해도 예외를 던지지 않고 0 을 반환 한다.")
        void failOpen() {
            // arrange
            Mockito.when(valueOperations.increment(Mockito.anyString(), Mockito.anyLong()))
                    .thenThrow(new RuntimeException("redis down"));

            // act & assert - 상한 검사를 건너뛰고 환전이 진행되어야 한다
            int total = assertDoesNotThrow(() -> deviceCachePort.increaseTodayExchangedWalkingCountPort(ACCOUNT_ID, 1_000));
            assertThat(total).isZero();
        }

        @Test
        @DisplayName("응답이 없으면 0 을 반환 한다.")
        void nullResponse() {
            // arrange
            Mockito.when(valueOperations.increment(Mockito.anyString(), Mockito.anyLong())).thenReturn(null);

            // act & assert
            assertThat(deviceCachePort.increaseTodayExchangedWalkingCountPort(ACCOUNT_ID, 1_000)).isZero();
        }
    }

    @Nested
    @DisplayName("오늘 누적 환전 걸음 수 감소 단위 테스트")
    class DecreaseTodayExchangedWalkingCount {

        @Test
        @DisplayName("증가분만큼 되돌린다.")
        void decrement() {
            // act
            deviceCachePort.decreaseTodayExchangedWalkingCountPort(ACCOUNT_ID, 1_000);

            // assert
            Mockito.verify(valueOperations).decrement(Mockito.anyString(), Mockito.eq(1_000L));
        }

        @Test
        @DisplayName("Redis 가 실패해도 예외를 던지지 않는다.")
        void failOpen() {
            // arrange
            Mockito.when(valueOperations.decrement(Mockito.anyString(), Mockito.anyLong()))
                    .thenThrow(new RuntimeException("redis down"));

            // act & assert
            assertDoesNotThrow(() -> deviceCachePort.decreaseTodayExchangedWalkingCountPort(ACCOUNT_ID, 1_000));
        }
    }
}
