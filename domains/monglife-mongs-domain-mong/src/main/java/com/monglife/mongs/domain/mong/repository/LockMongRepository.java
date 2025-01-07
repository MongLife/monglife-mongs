package com.monglife.mongs.domain.mong.repository;

import com.monglife.mongs.domain.mong.entity.MongEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface LockMongRepository extends JpaRepository<MongEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<MongEntity> findByMongIdAndMetaIsActiveIsTrue(Long mongId);
}