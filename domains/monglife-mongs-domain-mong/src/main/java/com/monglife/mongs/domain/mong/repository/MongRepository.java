package com.monglife.mongs.domain.mong.repository;

import com.monglife.mongs.domain.mong.entity.MongEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MongRepository extends JpaRepository<MongEntity, Long> {

    List<MongEntity> findByAccountIdAndMetaIsActiveIsTrue(Long accountId);

    Optional<MongEntity> findByMongIdAndMetaIsActiveIsTrue(Long mongId);
}
