package com.mongs.play.domain.collection.repository;

import com.mongs.play.domain.collection.entity.MapCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MapCollectionRepository extends JpaRepository<MapCollection, Long> {
    List<MapCollection> findByAccountId(Long accountId);
    Optional<MapCollection> findByAccountIdAndCode(Long accountId, String code);
    void deleteByAccountIdAndCode(Long accountId, String code);
}
