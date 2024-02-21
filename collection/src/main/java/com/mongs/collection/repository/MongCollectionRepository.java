package com.mongs.collection.repository;

import com.mongs.collection.entity.MongCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongCollectionRepository extends JpaRepository<MongCollection, Long> {
    List<MongCollection> findByMemberId(Long memberId);
}
