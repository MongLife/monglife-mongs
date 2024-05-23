package com.mongs.play.domain.collection.repository;

import com.mongs.play.domain.collection.entity.MongCollection;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MongCollectionRepository extends JpaRepository<MongCollection, Long> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    List<MongCollection> findByAccountId(Long accountId);
    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<MongCollection> findByAccountIdAndCode(Long accountId, String code);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void deleteByAccountIdAndCode(Long accountId, String code);
}
