package com.monglife.mongs.adapter.out.mong.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.mong.persistence.entity.RandomDrawEntity;

import java.util.List;

public interface RandomDrawDslRepository {

    List<RandomDrawEntity> findNotDrawByAccountId(Long accountId);
}
