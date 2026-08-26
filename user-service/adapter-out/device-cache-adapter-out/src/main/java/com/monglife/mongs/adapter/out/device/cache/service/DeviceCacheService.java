package com.monglife.mongs.adapter.out.device.cache.service;

import com.monglife.mongs.application.device.port.out.DeviceCachePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class DeviceCacheService implements DeviceCachePort {

    private static final String KEY_PREFIX = "step:exchange:";

    /**
     * 서비스 기준 시간대. 서버가 어느 지역에 뜨든 사용자가 체감하는 "오늘"이 같아야 한다.
     */
    private static final ZoneId SERVICE_ZONE = ZoneId.of("Asia/Seoul");

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 자정 경계를 직접 계산하지 않고 넉넉한 TTL 로 스스로 사라지게 둔다.
     */
    private static final Duration KEY_TTL = Duration.ofDays(2);

    private final StringRedisTemplate deviceRedisTemplate;

    public DeviceCacheService(@Qualifier("deviceRedisTemplate") StringRedisTemplate deviceRedisTemplate) {
        this.deviceRedisTemplate = deviceRedisTemplate;
    }

    /**
     * 오늘 누적 환전 걸음 수 증가
     *
     * Redis 가 죽어도 환전을 막지 않는다(fail-open). 일일 상한은 어뷰징 억제 장치이지
     * 기능의 전제가 아니므로, 캐시 장애로 정상 사용자의 환전까지 막는 쪽이 더 나쁘다.
     */
    @Override
    public int increaseTodayExchangedWalkingCountPort(Long accountId, Integer walkingCount) {

        String key = generateKey(accountId);

        try {
            Long total = deviceRedisTemplate.opsForValue().increment(key, walkingCount);

            if (total == null) {
                return 0;
            }

            // 방금 만들어진 키에만 만료를 건다. 매번 걸면 계속 걷는 사용자의 키가 만료되지 않는다.
            if (total.equals(walkingCount.longValue())) {
                deviceRedisTemplate.expire(key, KEY_TTL);
            }

            return total > Integer.MAX_VALUE ? Integer.MAX_VALUE : total.intValue();

        } catch (Exception e) {
            log.error("일일 환전 상한 집계 실패 - 상한 검사를 건너뛴다. key={}", key, e);
            return 0;
        }
    }

    /**
     * 오늘 누적 환전 걸음 수 감소
     */
    @Override
    public void decreaseTodayExchangedWalkingCountPort(Long accountId, Integer walkingCount) {

        String key = generateKey(accountId);

        try {
            deviceRedisTemplate.opsForValue().decrement(key, walkingCount);
        } catch (Exception e) {
            log.error("일일 환전 상한 되돌리기 실패. key={}, walkingCount={}", key, walkingCount, e);
        }
    }

    private String generateKey(Long accountId) {
        return KEY_PREFIX + accountId + ":" + LocalDate.now(SERVICE_ZONE).format(DATE_FORMAT);
    }
}
