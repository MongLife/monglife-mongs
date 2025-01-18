package com.monglife.mongs.domain.mong.repositoryCustom;

import com.monglife.mongs.domain.mong.entity.MongEntity;

import java.util.Optional;

public interface LockMongDslRepository {

    Optional<MongEntity> findByMongIdAndMetaIsActiveIsTrue(Long mongId);
}
