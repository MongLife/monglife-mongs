package com.monglife.mongs.adapter.out.mong.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.mong.persistence.entity.MongEntity;

import java.util.Optional;

public interface LockMongDslRepository {

    Optional<MongEntity> findByMongIdAndMetaIsActiveIsTrue(Long mongId);
}
