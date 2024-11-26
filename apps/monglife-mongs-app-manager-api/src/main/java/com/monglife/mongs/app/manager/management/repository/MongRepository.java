package com.monglife.mongs.app.manager.management.repository;

import com.monglife.mongs.app.manager.management.domain.MongEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MongRepository extends JpaRepository<MongEntity, Long> {

    List<MongEntity> findByAccountIdAndIsActiveIsTrue(Long accountId);

    Optional<MongEntity> findByAccountIdAndMongIdAndIsActiveIsTrue(Long accountId, Long mongId);
}
