package com.monglife.mongs.domain.mong.repositoryCustom;

import com.monglife.mongs.domain.mong.entity.MongEntity;

import java.util.List;
import java.util.Optional;

public interface MongDslRepository {

    Optional<MongEntity> findByMongIdAndAccountIdAndMetaIsActiveIsTrue(Long mongId, Long accountId);

    Optional<MongEntity> findByMongIdAndMetaIsActiveIsTrue(Long mongId);

    List<MongEntity> findByAccountIdAndMetaIsActiveIsTrue(Long accountId);
}
