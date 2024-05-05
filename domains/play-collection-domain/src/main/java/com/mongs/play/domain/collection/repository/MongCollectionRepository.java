package com.mongs.play.domain.collection.repository;

import com.mongs.play.domain.collection.entity.MongCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MongCollectionRepository extends JpaRepository<MongCollection, Long> {
    List<MongCollection> findByAccountId(Long accountId);
    Optional<MongCollection> findByAccountIdAndCode(Long accountId, String code);
    void deleteByAccountIdAndCode(Long accountId, String code);
}
