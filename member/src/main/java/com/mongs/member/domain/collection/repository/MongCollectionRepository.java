package com.mongs.member.domain.collection.repository;

import com.mongs.member.domain.collection.entity.MongCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MongCollectionRepository extends JpaRepository<MongCollection, Long> {
    List<MongCollection> findByAccountId(Long memberId);
    Optional<MongCollection> findByAccountIdAndCode(Long memberId, String code);
    void deleteByAccountIdAndCode(Long memberId, String code);
}
