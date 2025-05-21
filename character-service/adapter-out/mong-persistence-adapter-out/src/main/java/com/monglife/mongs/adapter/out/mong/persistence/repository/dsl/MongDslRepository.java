package com.monglife.mongs.adapter.out.mong.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.mong.persistence.entity.MongEntity;

import java.util.List;
import java.util.Optional;

public interface MongDslRepository {

    Optional<MongEntity> findByMongIdWithLock(Long mongId);
}
