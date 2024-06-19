package com.mongs.play.domain.match.service;

import com.mongs.play.core.error.domain.MatchErrorCode;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.match.vo.MatchWaitVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final String KEY = "match";
    private final RedisTemplate<String, MatchWaitVo> redisTemplate;

    public MatchWaitVo addMatch(String deviceId, Long mongId) {
        LocalDateTime now = LocalDateTime.now();
        long createdAt = Long.parseLong(now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));

        MatchWaitVo matchWaitVo = MatchWaitVo.builder()
                .mongId(mongId)
                .deviceId(deviceId)
                .build();

        redisTemplate.opsForZSet().add(KEY, matchWaitVo, createdAt);

        return matchWaitVo;
    }

    public MatchWaitVo deleteMatch(String deviceId, Long mongId) {
        MatchWaitVo matchWaitVo = MatchWaitVo.builder()
                .mongId(mongId)
                .deviceId(deviceId)
                .build();

        redisTemplate.opsForZSet().remove(KEY, matchWaitVo);

        return matchWaitVo;
    }

    public Set<MatchWaitVo> findMatch() {

        Set<MatchWaitVo> matchWaitVoSet = redisTemplate.opsForZSet().range(KEY, 0, 1);

        if (matchWaitVoSet == null || matchWaitVoSet.isEmpty()) {
             throw new NotFoundException(MatchErrorCode.NOT_FOUND_MATCH);
        }

        if (matchWaitVoSet.size() == 1) {
            MatchWaitVo matchWaitVo = matchWaitVoSet.iterator().next();

            LocalDateTime now = LocalDateTime.now();
            long createdAt = Objects.requireNonNull(redisTemplate.opsForZSet().score(KEY, matchWaitVo)).longValue();
            long expiredAt = Long.parseLong(now.minusSeconds(15).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));

            if (createdAt > expiredAt) {
                throw new NotFoundException(MatchErrorCode.NOT_FOUND_MATCH);
            }

            // 봇 생성
            matchWaitVoSet.add(MatchWaitVo.builder()
                    .mongId(-1L)
                    .deviceId("BOT-DEVICE-ID")
                    .isBot(true)
                    .build());
        }

        return matchWaitVoSet.stream()
                .peek(matchWaitVo -> redisTemplate.opsForZSet().remove(KEY, matchWaitVo))
                .map(matchWaitVo -> matchWaitVo.toBuilder()
                        .playerId(UUID.randomUUID().toString().replace("-", ""))
                        .build())
                .collect(Collectors.toSet());
    }
}
