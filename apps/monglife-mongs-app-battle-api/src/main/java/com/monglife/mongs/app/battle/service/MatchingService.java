package com.monglife.mongs.app.battle.service;

import com.monglife.mongs.app.battle.domain.MatchingEntity;
import com.monglife.mongs.app.battle.dto.etc.FindMatchingDto;
import com.monglife.mongs.app.battle.global.exception.NotExistsWaitMatchingException;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class MatchingService {

    private static final Integer MATCHING_MAX_SECONDS = 15;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    private static final String MATCHING_ENTITY_KEY = "matching";

    private final RedisTemplate<String, MatchingEntity> redisTemplate;

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
    public Set<FindMatchingDto> findWaitMatching() {

        // 대기열에서 일자를 기준으로 최근 순 2명까지 조회
        Set<MatchingEntity> matchingEntitySet = redisTemplate.opsForZSet().range(MATCHING_ENTITY_KEY, 0, 1);

        // 대기열이 비었으면 throw
        if (matchingEntitySet == null || matchingEntitySet.isEmpty()) {
            throw new NotExistsWaitMatchingException();
        }

        // 생성된 매칭 플레이어 저장 Set
        Set<FindMatchingDto> findWaitMatchingDtoSet;

        if (matchingEntitySet.size() == 1) {
            // 매칭 대기열이 1명인 경우
            MatchingEntity matchingEntity = matchingEntitySet.iterator().next();

            LocalDateTime createdAt = LocalDateTime.now();
            long createdScore = Objects.requireNonNull(redisTemplate.opsForZSet().score(MATCHING_ENTITY_KEY, matchingEntity)).longValue();
            long expiredScore = Long.parseLong(createdAt.minusSeconds(MATCHING_MAX_SECONDS).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));

            // 최소 대기 시간을 초과하지 않았으면 다시 대기열로 복귀
            if (createdScore > expiredScore) {
                throw new NotExistsWaitMatchingException();
            }

            findWaitMatchingDtoSet = new HashSet<>();

            // 플레이어 생성
            FindMatchingDto playerFindMatchingDto = FindMatchingDto.of(matchingEntity);
            findWaitMatchingDtoSet.add(playerFindMatchingDto);
            redisTemplate.opsForZSet().remove(MATCHING_ENTITY_KEY, playerFindMatchingDto);

            // 봇 플레이어 생성
            FindMatchingDto botFindMatchingDto = FindMatchingDto.builder()
                    .mongId(0L)
                    .deviceId(UUID.randomUUID().toString().replace("-", ""))
                    .accountId(0L)
                    .isBot(Boolean.TRUE)
                    .build();
            findWaitMatchingDtoSet.add(botFindMatchingDto);

        } else {
            // 매칭 대기열이 2명 이상인 경우
            findWaitMatchingDtoSet = matchingEntitySet.stream()
                    .peek(matchingEntity -> redisTemplate.opsForZSet().remove(MATCHING_ENTITY_KEY, matchingEntity))
                    .map(FindMatchingDto::of)
                    .collect(Collectors.toSet());
        }

        return findWaitMatchingDtoSet;
    }
}
