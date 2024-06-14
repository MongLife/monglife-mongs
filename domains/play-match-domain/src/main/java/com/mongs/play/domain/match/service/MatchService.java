package com.mongs.play.domain.match.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.play.core.error.domain.MatchErrorCode;
import com.mongs.play.core.exception.common.InvalidException;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.match.vo.MatchWaitVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final String KEY = "match";
    private final RedisTemplate<String, MatchWaitVo> redisTemplate;

    public MatchWaitVo addMatch(String deviceId, Long mongId) {
        MatchWaitVo matchWaitVo = MatchWaitVo.builder()
                .playerId(UUID.randomUUID().toString().replace("-", ""))
                .mongId(mongId)
                .deviceId(deviceId)
                .build();

        LocalDateTime now = LocalDateTime.now();
        redisTemplate.opsForZSet().add(KEY, matchWaitVo, Long.parseLong(now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))));

        return matchWaitVo;
    }

    public Set<MatchWaitVo> findMatch() {

        Set<MatchWaitVo> matchWaitVoSet = redisTemplate.opsForZSet().range(KEY, 0, 1);

        if (matchWaitVoSet == null || matchWaitVoSet.size() < 2) {
            throw new NotFoundException(MatchErrorCode.NOT_FOUND_MATCH);
        }

        return matchWaitVoSet.stream()
                .peek(matchWaitVo -> redisTemplate.opsForZSet().remove(KEY, matchWaitVo))
                .collect(Collectors.toSet());
    }
}
