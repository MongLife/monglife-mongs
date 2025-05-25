package com.monglife.mongs.adapter.out.battle.persistence.repository;

import com.monglife.mongs.adapter.out.battle.persistence.entity.QueuePlayerEntity;

import java.util.Optional;
import java.util.Set;

public interface QueuePlayerRepository {

    Set<QueuePlayerEntity> findAll();

    Optional<QueuePlayerEntity> findByMongIdAndAccountIdAndDeviceId(Long mongId, Long accountId, String deviceId);

    Set<QueuePlayerEntity> findByCount(Integer count);

    QueuePlayerEntity save(QueuePlayerEntity queuePlayerEntity);

    void delete(QueuePlayerEntity queuePlayerEntity);

    void deleteAll();
}
