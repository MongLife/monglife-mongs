package com.mongs.play.domain.collection.repository;

import com.mongs.play.domain.collection.entity.MapCollection;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MapCollectionRepository extends JpaRepository<MapCollection, Long> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    List<MapCollection> findByAccountId(Long accountId);
    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<MapCollection> findByAccountIdAndCode(Long accountId, String code);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void deleteByAccountIdAndCode(Long accountId, String code);
}
