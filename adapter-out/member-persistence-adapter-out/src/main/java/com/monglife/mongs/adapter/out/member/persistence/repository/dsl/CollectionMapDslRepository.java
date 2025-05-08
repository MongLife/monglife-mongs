package com.monglife.mongs.adapter.out.member.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.member.persistence.entity.CollectionMapEntity;

import java.util.List;

public interface CollectionMapDslRepository {

    List<CollectionMapEntity> findByAccountId(Long accountId);
}
