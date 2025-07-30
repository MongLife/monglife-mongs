package com.monglife.mongs.adapter.out.member.persistence.repository;

import com.monglife.mongs.adapter.out.member.persistence.entity.CollectionMongEntity;
import com.monglife.mongs.adapter.out.member.persistence.repository.dsl.CollectionMongDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionMongRepository extends JpaRepository<CollectionMongEntity, Long>, CollectionMongDslRepository {

    Boolean existsByAccountIdAndComnCode(Long accountId, String mongCode);
}
