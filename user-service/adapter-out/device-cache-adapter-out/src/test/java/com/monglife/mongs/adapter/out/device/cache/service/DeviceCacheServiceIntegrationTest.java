package com.monglife.mongs.adapter.out.device.cache.service;

import com.monglife.mongs.adapter.out.device.cache.config.AdapterOutDeviceCacheConfig;
import com.monglife.mongs.adapter.out.device.cache.utils.RedisTestContainer;
import com.monglife.mongs.application.device.port.out.DeviceCachePort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 실제 Redis 를 상대로 하는 통합 테스트.
 *
 * 짝인 DeviceCacheServiceTest 는 목으로 남겨 둔다. 거기서는 Redis 장애 같은 분기를 보고,
 * 여기서는 목으로 확인할 수 없는 것 — 명령이 서버에서 실제로 어떻게 처리되는지 — 만 본다.
 * 특히 만료는 목으로 보면 "expire 를 불렀다"까지가 끝이라 서버에 걸렸는지 알 수 없다.
 */
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = { AdapterOutDeviceCacheConfig.class })
class DeviceCacheServiceIntegrationTest extends RedisTestContainer {

    private static final Long ACCOUNT_ID = 1L;

    /** 운영 코드와 같은 규칙. 키가 바뀌면 이 테스트가 먼저 깨진다. */
    private static final String KEY = "step:exchange:" + ACCOUNT_ID + ":"
            + LocalDate.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

    private static final Duration KEY_TTL = Duration.ofDays(2);

    private final DeviceCachePort deviceCachePort;
    private final StringRedisTemplate deviceRedisTemplate;

    @Autowired
    public DeviceCacheServiceIntegrationTest(DeviceCachePort deviceCachePort,
                                             @Qualifier("deviceRedisTemplate") StringRedisTemplate deviceRedisTemplate) {
        this.deviceCachePort = deviceCachePort;
        this.deviceRedisTemplate = deviceRedisTemplate;
    }

    @AfterEach
    void afterEach() {
        // 컨테이너는 모듈 안에서 공유한다. 자기가 만든 키는 자기가 지운다.
        deviceRedisTemplate.delete(KEY);
    }

    @Nested
    @DisplayName("오늘 누적 환전 걸음 수 증가 통합 테스트")
    class IncreaseTodayExchangedWalkingCount {

        @Test
        @DisplayName("여러 번 증가시키면 누적 된다.")
        void accumulate() {
            // act
            int first = deviceCachePort.increaseTodayExchangedWalkingCountPort(ACCOUNT_ID, 1_000);
            int second = deviceCachePort.increaseTodayExchangedWalkingCountPort(ACCOUNT_ID, 2_500);

            // assert
            assertThat(first).isEqualTo(1_000);
            assertThat(second).isEqualTo(3_500);
            assertThat(deviceRedisTemplate.opsForValue().get(KEY)).isEqualTo("3500");
        }

        @Test
        @DisplayName("운영 코드와 같은 규칙의 키에 저장 한다.")
        void keyFormat() {
            // act
            deviceCachePort.increaseTodayExchangedWalkingCountPort(ACCOUNT_ID, 1_000);

            // assert - 날짜는 서버 시간대가 아니라 Asia/Seoul 기준이어야 한다
            assertThat(deviceRedisTemplate.hasKey(KEY)).isTrue();
        }

        @Test
        @DisplayName("만료 시간이 실제로 걸린다.")
        void setsExpire() {
            // act
            deviceCachePort.increaseTodayExchangedWalkingCountPort(ACCOUNT_ID, 1_000);

            // assert - 서버가 들고 있는 잔여 TTL 을 직접 본다
            Long ttl = deviceRedisTemplate.getExpire(KEY, TimeUnit.SECONDS);
            assertThat(ttl).isNotNull()
                    .isGreaterThan(KEY_TTL.toSeconds() - 60)
                    .isLessThanOrEqualTo(KEY_TTL.toSeconds());
        }

        @Test
        @DisplayName("이미 있던 키에도 만료 시간을 다시 건다.")
        void alwaysRefreshExpire() {
            // arrange - 만료 없이 키부터 만들어 둔다. INCRBY 직후 프로세스가 죽어 만료가
            //           빠진 키가 남은 상황이다.
            deviceRedisTemplate.opsForValue().set(KEY, "1000");
            assertThat(deviceRedisTemplate.getExpire(KEY, TimeUnit.SECONDS)).isEqualTo(-1L);

            // act
            int total = deviceCachePort.increaseTodayExchangedWalkingCountPort(ACCOUNT_ID, 1_000);

            // assert - 누적은 이어 가고, 빠져 있던 만료는 이번에 걸린다
            assertThat(total).isEqualTo(2_000);
            assertThat(deviceRedisTemplate.getExpire(KEY, TimeUnit.SECONDS))
                    .isGreaterThan(KEY_TTL.toSeconds() - 60);
        }
    }

    @Nested
    @DisplayName("오늘 누적 환전 걸음 수 감소 통합 테스트")
    class DecreaseTodayExchangedWalkingCount {

        @Test
        @DisplayName("증가분만큼 정확히 되돌린다.")
        void rollback() {
            // arrange
            deviceCachePort.increaseTodayExchangedWalkingCountPort(ACCOUNT_ID, 3_000);

            // act - 상한을 넘겨 환전을 거절했을 때의 보상 경로다
            deviceCachePort.decreaseTodayExchangedWalkingCountPort(ACCOUNT_ID, 1_000);

            // assert
            assertThat(deviceRedisTemplate.opsForValue().get(KEY)).isEqualTo("2000");
        }
    }
}
