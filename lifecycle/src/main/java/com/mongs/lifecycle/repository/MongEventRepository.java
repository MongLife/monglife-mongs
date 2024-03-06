package com.mongs.lifecycle.repository;

import com.mongs.lifecycle.code.MongEventCode;
import com.mongs.lifecycle.entity.MongEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MongEventRepository extends MongoRepository<MongEvent, String> {
    Optional<MongEvent> findTopByExpiredAtBeforeOrderByCreatedAt(LocalDateTime now);
    List<MongEvent> findByExpiredAtBeforeOrderByCreatedAt(LocalDateTime now);
    Optional<MongEvent> findByMongIdAndEventCode(Long mongId, MongEventCode eventCode);
    List<MongEvent> findByMongId(Long mongId);
    void deleteByMongIdAndEventCode(Long mongId, MongEventCode eventCode);
    void deleteByMongId(Long mongId);
}
