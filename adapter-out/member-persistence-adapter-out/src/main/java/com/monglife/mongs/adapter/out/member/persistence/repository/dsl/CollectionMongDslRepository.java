package com.monglife.mongs.adapter.out.member.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.member.persistence.entity.CollectionMongEntity;

import java.util.List;

public interface CollectionMongDslRepository {

    List<CollectionMongEntity> findByAccountId(Long accountId);
}
