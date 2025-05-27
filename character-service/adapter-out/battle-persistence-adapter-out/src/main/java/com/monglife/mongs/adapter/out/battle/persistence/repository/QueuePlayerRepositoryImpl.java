package com.monglife.mongs.adapter.out.battle.persistence.repository;

import com.monglife.mongs.adapter.out.battle.persistence.entity.QueuePlayerEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;

@Repository
public class QueuePlayerRepositoryImpl implements QueuePlayerRepository {

    private static final String QUEUE_PLAYER_KEY = "QUEUE_PLAYER_KEY";

    private final RedisTemplate<String, QueuePlayerEntity> redisTemplate;


    public QueuePlayerRepositoryImpl(@Qualifier("battleRedisTemplate") RedisTemplate<String, QueuePlayerEntity> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Set<QueuePlayerEntity> findAll() {
        return redisTemplate.opsForZSet().range(QUEUE_PLAYER_KEY, 0, -1);
    }

    @Override
    public Optional<QueuePlayerEntity> findByMongIdAndAccountIdAndDeviceId(Long mongId, Long accountId, String deviceId) {

        Set<QueuePlayerEntity> queuePlayerEntities = redisTemplate.opsForZSet().range(QUEUE_PLAYER_KEY, 0, -1);

        if (queuePlayerEntities != null) {
            return queuePlayerEntities.stream()
                    .filter(queuePlayer -> queuePlayer.getMongId().equals(mongId))
                    .filter(queuePlayer -> queuePlayer.getAccountId().equals(accountId))
                    .filter(queuePlayer -> queuePlayer.getDeviceId().equals(deviceId))
                    .findFirst();
        }

        return Optional.empty();
    }

    @Override
    public Set<QueuePlayerEntity> findByCount(Integer count) {
        return redisTemplate.opsForZSet().range(QUEUE_PLAYER_KEY, 0, count - 1);
    }

    @Override
    public QueuePlayerEntity save(QueuePlayerEntity queuePlayerEntity) {

        if (redisTemplate.opsForZSet().score(QUEUE_PLAYER_KEY, queuePlayerEntity) != null) {
            redisTemplate.opsForZSet().remove(QUEUE_PLAYER_KEY, queuePlayerEntity);
        }

        double score = Double.parseDouble(queuePlayerEntity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));

        redisTemplate.opsForZSet().addIfAbsent(QUEUE_PLAYER_KEY, queuePlayerEntity, score);

        return queuePlayerEntity;
    }

    @Override
    public void delete(QueuePlayerEntity queuePlayerEntity) {
        redisTemplate.opsForZSet().remove(QUEUE_PLAYER_KEY, queuePlayerEntity);
    }

    @Override
    public void deleteAll() {
        redisTemplate.delete(QUEUE_PLAYER_KEY);
    }
}
