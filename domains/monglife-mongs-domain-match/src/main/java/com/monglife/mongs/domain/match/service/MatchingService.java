package com.monglife.mongs.domain.match.service;

import com.monglife.mongs.domain.match.entity.MatchingEntity;
import com.monglife.mongs.domain.match.vo.FindMatchingVo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MatchingService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    private static final String MATCHING_ENTITY_KEY = "matching";

    @Value("${application.service.matching.max-player}")
    private Integer MAX_PLAYER;

    @Value("${application.service.matching.max-matching-seconds}")
    private Integer MAX_MATCHING_SECONDS;

    private final RedisTemplate<String, MatchingEntity> redisTemplate;

    public MatchingService(@Qualifier("matchRedisTemplate") RedisTemplate<String, MatchingEntity> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 배틀 매칭 대기열 등록
     * @param accountId 계정 ID
     * @param deviceId 장치 ID
     * @param mongId 몽 ID
     */
    public void createWaitMatching(Long accountId, String deviceId, Long mongId) {

        LocalDateTime createdAt = LocalDateTime.now();

        MatchingEntity matchingEntity = MatchingEntity.builder()
                .mongId(mongId)
                .deviceId(deviceId)
                .accountId(accountId)
                .isBot(Boolean.FALSE)
                .build();

        long createdScore = Long.parseLong(createdAt.format(DATE_FORMATTER));

        redisTemplate.opsForZSet().addIfAbsent(MATCHING_ENTITY_KEY, matchingEntity, createdScore);
    }

    /**
     * 배틀 매칭 대기열 삭제
     * @param accountId 계정 ID
     * @param deviceId 장치 ID
     * @param mongId 몽 ID
     */
    public void deleteWaitMatching(Long accountId, String deviceId, Long mongId) {

        MatchingEntity matchingEntity = MatchingEntity.builder()
                .mongId(mongId)
                .deviceId(deviceId)
                .accountId(accountId)
                .isBot(Boolean.FALSE)
                .build();

        redisTemplate.opsForZSet().remove(MATCHING_ENTITY_KEY, matchingEntity);
    }

    /**
     * 대기열 조회 후 매칭 생성
     * @return 매칭 성사 된 대기열 Dto 목록
     */
    public Set<FindMatchingVo> findWaitMatching() {

        // 대기열에서 일자를 기준으로 최근 순 MAX_PLAYER 명까지 조회
        Set<MatchingEntity> matchingEntitySet = redisTemplate.opsForZSet().range(MATCHING_ENTITY_KEY, 0, MAX_PLAYER);

        // 생성된 매칭 플레이어 저장 Set
        Set<FindMatchingVo> findWaitMatchingDtoSet = new HashSet<>();

        if (matchingEntitySet != null && !matchingEntitySet.isEmpty()) {
            if (matchingEntitySet.size() == 1) {
                // 매칭 대기열이 1 명인 경우
                MatchingEntity matchingEntity = matchingEntitySet.iterator().next();

                LocalDateTime createdAt = LocalDateTime.now();
                long createdScore = Objects.requireNonNull(redisTemplate.opsForZSet().score(MATCHING_ENTITY_KEY, matchingEntity)).longValue();
                long expiredScore = Long.parseLong(createdAt.minusSeconds(MAX_MATCHING_SECONDS).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));

                // 대기 시간 초과인 경우
                if (createdScore <= expiredScore) {
                    // 플레이어 생성
                    FindMatchingVo playerFindMatchingVo = FindMatchingVo.of(matchingEntity);
                    findWaitMatchingDtoSet.add(playerFindMatchingVo);
                    redisTemplate.opsForZSet().remove(MATCHING_ENTITY_KEY, playerFindMatchingVo);
                }
            } else {
                // 매칭 대기열 -> 2명 이상인 경우
                findWaitMatchingDtoSet = matchingEntitySet.stream()
                        .peek(matchingEntity -> redisTemplate.opsForZSet().remove(MATCHING_ENTITY_KEY, matchingEntity))
                        .map(FindMatchingVo::of)
                        .collect(Collectors.toSet());
            }
        }

        if (!findWaitMatchingDtoSet.isEmpty()) {
            // 봇 플레이어 생성
            for (int index = findWaitMatchingDtoSet.size(); index < MAX_PLAYER; index++) {
                FindMatchingVo botFindMatchingVo = FindMatchingVo.builder()
                        .mongId(0L)
                        .deviceId(UUID.randomUUID().toString().replace("-", ""))
                        .accountId(0L)
                        .isBot(Boolean.TRUE)
                        .build();
                findWaitMatchingDtoSet.add(botFindMatchingVo);
            }
        }

        return findWaitMatchingDtoSet;
    }
}
