package com.mongs.play.domain.match.service;

import com.mongs.play.core.error.domain.MatchErrorCode;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.match.vo.MatchVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final String KEY = "match";
    private final RedisTemplate<String, String> redisTemplate;

    public MatchVo addMatch(Long mongId) {

        LocalDateTime now = LocalDateTime.now();
        redisTemplate.opsForZSet().add(KEY, mongId.toString(), Long.parseLong(now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))));

        return MatchVo.builder()
                .mongId(mongId)
                .build();
    }

    public Set<MatchVo> getMatch() {

        Set<String> zSet = redisTemplate.opsForZSet().range(KEY, 0, 1);

        if (zSet == null) {
            throw new NotFoundException(MatchErrorCode.NOT_FOUND_MATCH);
        }

        Set<Long> matchSet = zSet.stream()
                .map(Long::parseLong)
                .collect(Collectors.toSet());

        if (matchSet.size() < 2) {
            throw new NotFoundException(MatchErrorCode.NOT_FOUND_MATCH);
        }

        return matchSet.stream()
                .map(mongId -> {
                    redisTemplate.opsForZSet().remove(KEY, mongId.toString());
                    return MatchVo.builder().mongId(mongId).build();
                })
                .collect(Collectors.toSet());
    }
}
