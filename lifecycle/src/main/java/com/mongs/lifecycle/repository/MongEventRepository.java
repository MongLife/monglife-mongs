package com.mongs.lifecycle.repository;

import com.mongs.lifecycle.code.EventStatusCode;
import com.mongs.lifecycle.code.MongEventCode;
import com.mongs.lifecycle.entity.MongEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MongEventRepository extends MongoRepository<MongEvent, String> {
    Optional<MongEvent> findByIdAndStatusCodeNot(String id, EventStatusCode statusCode);
    List<MongEvent> findByMongIdAndStatusCode(Long mongId, EventStatusCode statusCode);
}
